package com.example.Chirp.services;

import com.example.Chirp.dto.ChirpRequestDTO;
import com.example.Chirp.dto.ChirpResponseDTO;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface IChirpService {
    public Mono<ChirpResponseDTO> createChirp(ChirpRequestDTO chirpRequestDTO);
    public Mono<ChirpResponseDTO> getChirpById(String id);
    public Flux<ChirpResponseDTO> getAllChirps();
    public Mono<Void> deleteChirpById(String id);
    public Flux<ChirpResponseDTO> searchChirpsByKeywords( String keyword, int page, int size);
    public Flux<ChirpResponseDTO> getChirpsByTag(String tagId,int page, int size);
}
