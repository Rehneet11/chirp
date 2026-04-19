package com.example.Chirp.repository;

import com.example.Chirp.dto.ChirpResponseDTO;
import com.example.Chirp.models.Chirp;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public interface ChirpRepository extends ReactiveMongoRepository<Chirp,String> {
    Flux<Chirp> findByContentContainingIgnoreCase(String keywords, Pageable pageable);
    Flux<Chirp> findByTagIds(String tagId, Pageable pageable);

}
