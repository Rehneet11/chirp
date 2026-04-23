package com.example.Chirp.controllers;

import com.example.Chirp.dto.ChirpRequestDTO;
import com.example.Chirp.dto.ChirpResponseDTO;
import com.example.Chirp.models.ChirpElasticDocument;
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
    public Flux<ChirpResponseDTO> getAllChirps(
            @RequestParam(required = false) String cursor,
            @RequestParam(defaultValue = "10") int size
    ){
        return chirpService.getAllChirps(cursor,size)
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

    @GetMapping("/elasticsearch")
    public Flux<ChirpElasticDocument> searchChirpsByElasticSearch(@RequestParam String keywords){
        return chirpService.searchChirpsByElasticSearch(keywords);
    }


    @GetMapping("/tag/{tagId}")
    public Flux<ChirpResponseDTO> getChirpsByTag(
            @PathVariable String tagId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size){
        return chirpService.getChirpsByTag(tagId, page, size)
                .doOnError(error -> System.out.println("Chirp doesn't Exist"+ error));
    }

    @PatchMapping("/likes/{id}")
    public Mono<Void> likeChirpById(@PathVariable String id){
        return chirpService.likeChirpById(id)
                .doOnSuccess(response -> System.out.println("Chirping Liked Successfully: " + response))
                .doOnError(error -> System.out.println("Error Liking chirp: " + error));
    }

    @PostMapping("/rechirp/{id}")
    public Mono<ChirpResponseDTO> rechirpAChirpById(@PathVariable String id){
        return chirpService.rechirpAChirpById(id)
                .doOnSuccess(response -> System.out.println("Rechirped Successfully: " + response))
                .doOnError(error -> System.out.println("Error During Rechirping : " + error));
    }

    @PostMapping("/reply")
    public Mono<ChirpResponseDTO> replyToAChirp(@RequestBody ChirpRequestDTO chirpRequestDTO){
        return chirpService.replyToAChirp(chirpRequestDTO)
                .doOnSuccess(response-> System.out.println("Replied to Chirp Successfully " + response))
                .doOnError(error -> System.out.println("Error Replying to a Chirp " + error));
    }
}
