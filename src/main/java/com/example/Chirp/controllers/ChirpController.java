package com.example.Chirp.controllers;

import com.example.Chirp.dto.ChirpRequestDTO;
import com.example.Chirp.dto.ChirpResponseDTO;
import com.example.Chirp.services.IChirpService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/chirps")
public class ChirpController {

    private final IChirpService chirpService;

    public ChirpController(IChirpService chirpService) {
        this.chirpService = chirpService;
    }

    @PostMapping("/create_chirp")
    public Mono<ChirpResponseDTO> createChirp(@RequestBody ChirpRequestDTO chirpRequestDTO){
        return chirpService.createChirp(chirpRequestDTO)
                .doOnSuccess(response -> System.out.println("Chirping done Successfully: " + response))
                .doOnError(error -> System.out.println("Error doing chirp: " + error));
    }

    @GetMapping("/{id}")
    public Mono<ChirpResponseDTO> getChirpById(@PathVariable String id){
        return chirpService.getChirpById(id)
                .doOnSuccess(response -> System.out.println("Chirping done Successfully: " + response))
                .doOnError(error -> System.out.println("Error doing chirp: " + error));
    }

    @GetMapping("")
    public Flux<ChirpResponseDTO> getAllChirps(){
        return chirpService.getAllChirps()
                .doOnError(error -> System.out.println("Error doing chirp: " + error));
    }

    @DeleteMapping("/{id}")
    public Mono<Void> deleteChirpById(@PathVariable String id){
        return  chirpService.deleteChirpById(id)
                .doOnSuccess(response -> System.out.println("Chirping Deleted Successfully: " + response))
                .doOnError(error -> System.out.println("Error Deleting chirp: " + error));
    }

    @GetMapping("/search")
    public Flux<ChirpResponseDTO> searchChirpsByKeywords(
            @RequestParam String keyword,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size){
        return chirpService.searchChirpsByKeywords(keyword, page, size)
                .doOnError(error -> System.out.println("Error doing chirp: " + error));

    }

    @GetMapping("/tag/{tagId}")
    public Flux<ChirpResponseDTO> getChirpsByTag(
            @PathVariable String tagId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size){
        return chirpService.getChirpsByTag(tagId, page, size)
                .doOnError(error -> System.out.println("Chirp doesn't Exist"+ error));
    }
}
