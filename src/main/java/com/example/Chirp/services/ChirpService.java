package com.example.Chirp.services;

import com.example.Chirp.dto.ChirpRequestDTO;
import com.example.Chirp.dto.ChirpResponseDTO;
import com.example.Chirp.mapper.ChirpMapper;
import com.example.Chirp.models.Chirp;
import com.example.Chirp.repository.ChirpRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.sql.SQLOutput;
import java.time.LocalDateTime;

@Service
public class ChirpService implements  IChirpService{
    private final ChirpRepository chirpRepository;

    public ChirpService(ChirpRepository chirpRepository) {
        this.chirpRepository = chirpRepository;
    }

    @Override
    public Mono<ChirpResponseDTO> createChirp(ChirpRequestDTO chirpRequestDTO) {
        Chirp chirp = Chirp.builder()
                .content(chirpRequestDTO.getContent())
                .chirpType(chirpRequestDTO.getChirpType())
                .chirperId(chirpRequestDTO.getChirperId())
                .tagIds(chirpRequestDTO.getTagIds())
                .parentChirpId(chirpRequestDTO.getParentChirpId())
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        return chirpRepository.save(chirp)
                .map(ChirpMapper::chirpToChirpResponseDTO)
                .doOnSuccess(response -> System.out.println("Chirping done Successfully: " + response))
                .doOnError(error -> System.out.println("Error doing chirp: " + error));
    }

    @Override
    public Mono<ChirpResponseDTO> getChirpById(String id) {
        return chirpRepository.findById(id)
                .map(ChirpMapper::chirpToChirpResponseDTO)
                .doOnError(error -> System.out.println("Chirp doesn't exist "+error))
                .doOnSuccess(response -> System.out.println("Chirp Fetched Successfully"));
    }

    @Override
    public Flux<ChirpResponseDTO> getAllChirps() {
        return chirpRepository.findAll()
                .map(ChirpMapper::chirpToChirpResponseDTO)
                .doOnError(error -> System.out.println("Chirp doesn't exist "+error));
    }

    @Override
    public Mono<Void> deleteChirpById(String id) {
        return chirpRepository.deleteById(id)
                .doOnSuccess(response -> System.out.println("Chirp Deleted Successfully"))
                .doOnError(error -> System.out.println("Error Deleting Chirp"));
    }

    @Override
    public Flux<ChirpResponseDTO> searchChirpsByKeywords(String keyword, int page, int size) {
        Pageable pageable = PageRequest.of(page,size);
        return chirpRepository.findByContentContainingIgnoreCase(keyword,pageable)
                .map(ChirpMapper::chirpToChirpResponseDTO)
                .doOnError(error -> System.out.println("Chirp doesn't exist "+error));
    }

    @Override
    public Flux<ChirpResponseDTO> getChirpsByTag(String tagId, int page, int size) {
        Pageable pageable = PageRequest.of(page,size);
        return chirpRepository.findByTagIds(tagId,pageable)
                .map(ChirpMapper::chirpToChirpResponseDTO)
                .doOnError(error -> System.out.println("Chirp doesn't Exist"+error));
    }
}
