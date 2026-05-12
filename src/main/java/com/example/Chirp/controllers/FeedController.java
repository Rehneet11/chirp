package com.example.Chirp.controllers;

import com.example.Chirp.dto.ChirpResponseDTO;
import com.example.Chirp.services.IChirpService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

@RestController
@RequestMapping("/api/feed")
@Tag(name = "Feed", description = "Read global and trending chirp feeds")
public class FeedController {
    private final IChirpService chirpService;

    public FeedController(IChirpService chirpService) {
        this.chirpService = chirpService;
    }

    @GetMapping("/global")
    @Operation(summary = "Get global feed")
    public Flux<ChirpResponseDTO> getAllChirps(
            @RequestParam(required = false) String cursor,
            @RequestParam(defaultValue = "10") int size
    ){
        return chirpService.getAllChirps(cursor,size)
                .doOnError(error -> System.out.println("Error doing chirp: " + error));
    }

    @GetMapping("/trending")
    @Operation(summary = "Get trending feed")
    public Flux<ChirpResponseDTO> getTrendingChirps(@RequestParam(required = false) String cursor,
                                                    @RequestParam(defaultValue = "10") int size){
        return chirpService.getTrendingChirps(cursor,size)
                .doOnError(error -> System.out.println("Something went wrong " + error ));
    }
}
