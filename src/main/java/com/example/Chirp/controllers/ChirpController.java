package com.example.Chirp.controllers;

import com.example.Chirp.dto.ChirpRequestDTO;
import com.example.Chirp.dto.ChirpResponseDTO;
import com.example.Chirp.models.ChirpElasticDocument;
import com.example.Chirp.services.IChirpService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/chirps")
@Tag(name = "Chirps", description = "Create, read, search, and interact with chirps")
public class ChirpController {

    private final IChirpService chirpService;

    public ChirpController(IChirpService chirpService) {
        this.chirpService = chirpService;
    }

    @PostMapping("/create_chirp")
    @Operation(summary = "Create a chirp", description = "Creates a new chirp, reply, or rechirp depending on the payload.")
    @ApiResponse(responseCode = "200", description = "Chirp created successfully")
    public Mono<ChirpResponseDTO> createChirp(@Valid @RequestBody ChirpRequestDTO chirpRequestDTO){
        return chirpService.createChirp(chirpRequestDTO)
                .doOnSuccess(response -> System.out.println("Chirping done Successfully: " + response))
                .doOnError(error -> System.out.println("Error doing chirp: " + error));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get chirp by ID")
    public Mono<ChirpResponseDTO> getChirpById(@PathVariable String id){
        return chirpService.getChirpById(id)
                .doOnSuccess(response -> System.out.println("Chirping done Successfully: " + response))
                .doOnError(error -> System.out.println("Error doing chirp: " + error));
    }

    @GetMapping("")
    @Operation(summary = "List chirps", description = "Returns chirps using cursor-based pagination.")
    public Flux<ChirpResponseDTO> getAllChirps(
            @Parameter(description = "Cursor returned from a previous page") 
            @RequestParam(required = false) String cursor,
            @Parameter(description = "Number of chirps to return", example = "10")
            @RequestParam(defaultValue = "10") int size
    ){
        return chirpService.getAllChirps(cursor,size)
                .doOnError(error -> System.out.println("Error doing chirp: " + error));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete chirp by ID")
    public Mono<Void> deleteChirpById(@PathVariable String id){
        return  chirpService.deleteChirpById(id)
                .doOnSuccess(response -> System.out.println("Chirping Deleted Successfully: " + response))
                .doOnError(error -> System.out.println("Error Deleting chirp: " + error));
    }

    @GetMapping("/search")
    @Operation(summary = "Search chirps", description = "Searches persisted chirps by keyword with page-based pagination.")
    public Flux<ChirpResponseDTO> searchChirpsByKeywords(
            @RequestParam String keyword,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size){
        return chirpService.searchChirpsByKeywords(keyword, page, size)
                .doOnError(error -> System.out.println("Error doing chirp: " + error));

    }

    @GetMapping("/elasticsearch")
    @Operation(summary = "Search chirps in Elasticsearch",
            description = "Runs a search against the Elasticsearch chirp index.",
            responses = @ApiResponse(responseCode = "200", description = "Search results",
                    content = @Content(array = @ArraySchema(schema = @Schema(implementation = ChirpElasticDocument.class)))))
    public Flux<ChirpElasticDocument> searchChirpsByElasticSearch(@RequestParam String keywords){
        return chirpService.searchChirpsByElasticSearch(keywords);
    }


    @GetMapping("/tag/{tagId}")
    @Operation(summary = "Get chirps by tag")
    public Flux<ChirpResponseDTO> getChirpsByTag(
            @PathVariable String tagId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size){
        return chirpService.getChirpsByTag(tagId, page, size)
                .doOnError(error -> System.out.println("Chirp doesn't Exist"+ error));
    }

    @PatchMapping("/likes/{id}")
    @Operation(summary = "Like a chirp")
    public Mono<Void> likeChirpById(@PathVariable String id){
        return chirpService.likeChirpById(id)
                .doOnSuccess(response -> System.out.println("Chirping Liked Successfully: " + response))
                .doOnError(error -> System.out.println("Error Liking chirp: " + error));
    }

    @PostMapping("/rechirp/{id}")
    @Operation(summary = "Rechirp an existing chirp")
    public Mono<ChirpResponseDTO> rechirpAChirpById(@PathVariable String id){
        return chirpService.rechirpAChirpById(id)
                .doOnSuccess(response -> System.out.println("Rechirped Successfully: " + response))
                .doOnError(error -> System.out.println("Error During Rechirping : " + error));
    }

    @PostMapping("/reply")
    @Operation(summary = "Reply to a chirp")
    public Mono<ChirpResponseDTO> replyToAChirp(@Valid @RequestBody ChirpRequestDTO chirpRequestDTO){
        return chirpService.replyToAChirp(chirpRequestDTO)
                .doOnSuccess(response-> System.out.println("Replied to Chirp Successfully " + response))
                .doOnError(error -> System.out.println("Error Replying to a Chirp " + error));
    }
}
