package com.example.Chirp.controllers;

import com.example.Chirp.dto.ChirpResponseDTO;
import com.example.Chirp.services.IChirpService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

@RestController
@RequestMapping("/api/feed")
public class FeedController {
    private final IChirpService chirpService;

    public FeedController(IChirpService chirpService) {
        this.chirpService = chirpService;
    }

    @GetMapping("/global")
    public Flux<ChirpResponseDTO> getAllChirps(
            @RequestParam(required = false) String cursor,
            @RequestParam(defaultValue = "10") int size
    ){
        return chirpService.getAllChirps(cursor,size)
                .doOnError(error -> System.out.println("Error doing chirp: " + error));
    }

    @GetMapping("/trending")
    public Flux<ChirpResponseDTO> getTrendingChirps(@RequestParam(required = false) String cursor,
                                                    @RequestParam(defaultValue = "10") int size){
        return chirpService.getTrendingChirps(cursor,size)
                .doOnError(error -> System.out.println("Something went wrong " + error ));
    }
}
