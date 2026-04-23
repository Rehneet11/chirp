package com.example.Chirp.services;

import com.example.Chirp.dto.ChirpRequestDTO;
import com.example.Chirp.dto.ChirpResponseDTO;
import com.example.Chirp.enums.ChirpType;
import com.example.Chirp.events.ViewCountEvent;
import com.example.Chirp.mapper.ChirpMapper;
import com.example.Chirp.models.Chirp;
import com.example.Chirp.models.ChirpElasticDocument;
import com.example.Chirp.producers.KafkaEventProducer;
import com.example.Chirp.repository.ChirpDocumentRepository;
import com.example.Chirp.repository.ChirpRepository;
import com.example.Chirp.utils.CursorUtils;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;

@Service
public class ChirpService implements  IChirpService{
    private final ChirpRepository chirpRepository;

    private final KafkaEventProducer kafkaEventProducer;

    private final IChirpIndexService chirpIndexService;

    private final ChirpDocumentRepository chirpDocumentRepository;

    public ChirpService(ChirpRepository chirpRepository, KafkaEventProducer kafkaEventProducer, IChirpIndexService chirpIndexService, ChirpDocumentRepository chirpDocumentRepository) {
        this.chirpRepository = chirpRepository;
        this.kafkaEventProducer = kafkaEventProducer;
        this.chirpIndexService = chirpIndexService;
        this.chirpDocumentRepository = chirpDocumentRepository;
    }

    @Override
    public Mono<ChirpResponseDTO> createChirp(ChirpRequestDTO chirpRequestDTO) {
        Chirp chirp = Chirp.builder()
                .content(chirpRequestDTO.getContent())
                .chirpType(ChirpType.CHIRP)
                .chirperId(chirpRequestDTO.getChirperId())
                .tagIds(chirpRequestDTO.getTagIds())
                .likes(0)
                .trendingScore(0)
                .rechirps(0)
                .views(0)
                .parentChirpId(chirpRequestDTO.getParentChirpId())
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        return chirpRepository.save(chirp)
                .map(savedChirp ->{
                    chirpIndexService.createChirpIndex(savedChirp);
                    return ChirpMapper.chirpToChirpResponseDTO(savedChirp);
                })
                .doOnSuccess(response -> System.out.println("Chirping done Successfully: " + response))
                .doOnError(error -> System.out.println("Error doing chirp: " + error));
    }

    @Override
    public Mono<ChirpResponseDTO> getChirpById(String id) {
        return chirpRepository.findByIdAndIsDeletedFalse(id)
                .map(ChirpMapper::chirpToChirpResponseDTO)
                .doOnError(error -> System.out.println("Chirp doesn't exist "+error))
                .doOnSuccess(response -> {
                    System.out.println("Chirp Fetched Successfully");
                    ViewCountEvent viewCountEvent = new ViewCountEvent(id);
                    kafkaEventProducer.publishViewCountEvent(viewCountEvent);
                });
    }

    @Override
    public Flux<ChirpResponseDTO> getAllChirps(String cursor, int size) {
        Pageable pageable =PageRequest.of(0,size);
        if(!CursorUtils.isValidCursor(cursor)){
            return chirpRepository.findTop10AndIsDeletedFalseByOrderByCreatedAtDesc()
                    .take(size)
                    .map(ChirpMapper::chirpToChirpResponseDTO)
                    .doOnError(error -> System.out.println("Chirp doesn't exist "+error));
        }
        else{
            LocalDateTime cursorTimeStamp = CursorUtils.parseCursor(cursor);
            return chirpRepository.findByCreatedAtGreaterThanAndIsDeletedFalseOrderByCreatedAtDesc(cursorTimeStamp,pageable)
                    .map(ChirpMapper::chirpToChirpResponseDTO)
                    .doOnError(error -> System.out.println("Chirp doesn't exist "+error));
        }
    }

    @Override
    public Mono<Void> deleteChirpById(String id) {
        return chirpRepository.findById(id)
                .flatMap(chirp -> {
                    chirp.setDeleted(true);
                    chirp.setDeletedAt(LocalDateTime.now());

                    // Soft delete original
                    return chirpRepository.save(chirp)
                            .flatMap(saved ->
                                    // Soft delete all replies and rechirps
                                    chirpRepository.findByParentChirpIdOrRechirpedChirpId(id, id)
                                            .flatMap(child -> {
                                                child.setDeleted(true);
                                                child.setDeletedAt(LocalDateTime.now());
                                                return chirpRepository.save(child);
                                            })
                                            .then()
                            );
                })
                .doOnSuccess(v -> System.out.println("Chirp and children soft deleted"))
                .doOnError(error -> System.out.println("Error deleting chirp: " + error));
    }

    @Override
    public Flux<ChirpResponseDTO> searchChirpsByKeywords(String keyword, int page, int size) {
        Pageable pageable = PageRequest.of(page,size);
        return chirpRepository.findByContentContainingIgnoreCaseAndIsDeletedFalse(keyword,pageable)
                .map(ChirpMapper::chirpToChirpResponseDTO)
                .doOnError(error -> System.out.println("Chirp doesn't exist "+error));
    }

    @Override
    public Flux<ChirpResponseDTO> getChirpsByTag(String tagId, int page, int size) {
        Pageable pageable = PageRequest.of(page,size);
        return chirpRepository.findByTagIdsAndIsDeletedFalse(tagId,pageable)
                .map(ChirpMapper::chirpToChirpResponseDTO)
                .doOnError(error -> System.out.println("Chirp doesn't Exist"+error));
    }

    @Override
    public Mono<Void> likeChirpById(String id) {
        return chirpRepository.findById(id)
                .flatMap(chirp -> {
                    chirp.setLikes(chirp.getLikes()+1);
                    chirp.setTrendingScore(chirp.getTrendingScore()+2);
                    return chirpRepository.save(chirp);
                })
                .doOnSuccess(response -> System.out.println("Chirp Liked Successfully " + response))
                .doOnError(error -> System.out.println("Error Liking Chirp " + error))
                .then();

    }

    @Override
    public Mono<ChirpResponseDTO> rechirpAChirpById(String id) {
        return chirpRepository.findById(id)
                .flatMap(chirp -> {
                    Chirp rechirp= Chirp.builder()
                            .tagIds(chirp.getTagIds())
                            .rechirps(0)
                            .chirpType(ChirpType.RECHIRP)
                            .likes(0)
                            .views(0)
                            .content(chirp.getContent())
                            .rechirpedChirpId(chirp.getId())
                            .parentChirpId(null)
                            .createdAt(LocalDateTime.now())
                            .updatedAt(LocalDateTime.now())
                            .build();
                    chirp.setRechirps(chirp.getRechirps()+1);
                    chirp.setTrendingScore(chirp.getTrendingScore()+10);
                    return chirpRepository.save(chirp)
                            .flatMap(savedChirp -> chirpRepository.save(rechirp))
                            .map(ChirpMapper::chirpToChirpResponseDTO);
                })
                .doOnSuccess(response -> System.out.println("Rechirped Successfully " + response))
                .doOnError(error -> System.out.println("Error During Rechirp " + error));
    }

    @Override
    public Mono<ChirpResponseDTO> replyToAChirp(ChirpRequestDTO chirpRequestDTO) {
        return chirpRepository.findById(chirpRequestDTO.getParentChirpId())
                .flatMap(chirp -> {
                    Chirp reply = Chirp.builder()
                            .tagIds(chirpRequestDTO.getTagIds())
                            .content(chirpRequestDTO.getContent())
                            .parentChirpId(chirpRequestDTO.getParentChirpId())
                            .chirperId(chirpRequestDTO.getChirperId())
                            .likes(0)
                            .replies(0)
                            .rechirps(0)
                            .views(0)
                            .chirpType(ChirpType.REPLY)
                            .createdAt(LocalDateTime.now())
                            .updatedAt(LocalDateTime.now())
                            .build();
                    chirp.setReplies(chirp.getReplies()+1);
                    chirp.setTrendingScore(chirp.getTrendingScore()+5);
                    return chirpRepository.save(chirp)
                            .flatMap(savedChirp -> chirpRepository.save(reply))
                            .map(ChirpMapper::chirpToChirpResponseDTO);
                })
                .doOnSuccess(response -> System.out.println("Replied to Chirp Successfully :" + response))
                .doOnError(error ->System.out.println("Error Replying to a Chirp " + error));
    }

    @Override
    public Flux<ChirpElasticDocument> searchChirpsByElasticSearch(String keywords) {
        return chirpDocumentRepository.findByContentContainingIgnoreCase(keywords);
    }

    @Override
    public Flux<ChirpResponseDTO> getTrendingChirps(String cursor, int size) {
        Pageable pageable = PageRequest.of(0, size,
                Sort.by(Sort.Direction.DESC, "trendingScore")
                        .and(Sort.by(Sort.Direction.DESC, "createdAt")));

        LocalDateTime cursorTime = CursorUtils.isValidCursor(cursor)
                ? CursorUtils.parseCursor(cursor)
                : LocalDateTime.now().minusHours(24);

        return chirpRepository.findTrendingChirps(cursorTime, pageable)
                .map(ChirpMapper::chirpToChirpResponseDTO);
    }
}
