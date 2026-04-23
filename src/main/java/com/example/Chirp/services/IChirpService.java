package com.example.Chirp.services;

import com.example.Chirp.dto.ChirpRequestDTO;
import com.example.Chirp.dto.ChirpResponseDTO;
import com.example.Chirp.models.ChirpElasticDocument;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface IChirpService {
    public Mono<ChirpResponseDTO> createChirp(ChirpRequestDTO chirpRequestDTO);
    public Mono<ChirpResponseDTO> getChirpById(String id);
    public Flux<ChirpResponseDTO> getAllChirps(String cursor, int size);
    public Mono<Void> deleteChirpById(String id);
    public Flux<ChirpResponseDTO> searchChirpsByKeywords( String keyword, int page, int size);
    public Flux<ChirpResponseDTO> getChirpsByTag(String tagId,int page, int size);
    public Mono<Void> likeChirpById(String id);
    public Mono<ChirpResponseDTO> rechirpAChirpById(String id);
    public Mono<ChirpResponseDTO> replyToAChirp(ChirpRequestDTO chirpRequestDTO);
    public Flux<ChirpElasticDocument> searchChirpsByElasticSearch(String keywords);
    public Flux<ChirpResponseDTO> getTrendingChirps(String cursor, int size);
}
