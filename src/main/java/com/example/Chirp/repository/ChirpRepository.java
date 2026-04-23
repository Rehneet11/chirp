package com.example.Chirp.repository;

import com.example.Chirp.models.Chirp;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;

@Repository
public interface ChirpRepository extends ReactiveMongoRepository<Chirp,String> {
    @Query("{'content':{$options: 'i' , $regex: ?0}}")
    Flux<Chirp> findByContentContainingIgnoreCaseAndIsDeletedFalse(String keywords, Pageable pageable);

    Flux<Chirp> findByTagIdsAndIsDeletedFalse(String tagId, Pageable pageable);

    Flux<Chirp> findByCreatedAtGreaterThanAndIsDeletedFalseOrderByCreatedAtDesc(LocalDateTime cursor, Pageable pageable);

    Flux<Chirp> findTop10AndIsDeletedFalseByOrderByCreatedAtDesc();

    Flux<Chirp> findByParentChirpIdOrRechirpedChirpId(String parentChirpId, String rechirpedChirpId);

    Mono<Chirp> findByIdAndIsDeletedFalse(String id);

    @Query("{ 'createdAt': { '$gt': ?0 }, 'isDeleted': false, 'chirpType': CHIRP }")
    Flux<Chirp> findTrendingChirps(LocalDateTime cursor, Pageable pageable);
}
