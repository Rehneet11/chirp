package com.example.Chirp.controllers;

import com.example.Chirp.dto.TagRequestDTO;
import com.example.Chirp.dto.TagResponseDTO;
import com.example.Chirp.services.ITagService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/tags")
@Tag(name = "Tags", description = "Create and manage chirp tags")
public class TagController {
    private final ITagService tagService;

    public TagController(ITagService tagService) {
        this.tagService = tagService;
    }
    @PostMapping("/create_tag")
    @Operation(summary = "Create a tag")
    public Mono<TagResponseDTO> createTag(@Valid @RequestBody TagRequestDTO tagRequestDTO){
        return tagService.createTag(tagRequestDTO);
    }
}
