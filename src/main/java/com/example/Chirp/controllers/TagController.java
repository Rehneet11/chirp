package com.example.Chirp.controllers;

import com.example.Chirp.dto.TagRequestDTO;
import com.example.Chirp.dto.TagResponseDTO;
import com.example.Chirp.services.ITagService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/tags")
public class TagController {
    private final ITagService tagService;

    public TagController(ITagService tagService) {
        this.tagService = tagService;
    }
    @PostMapping("/create_tag")
    public Mono<TagResponseDTO> createTag(@RequestBody TagRequestDTO tagRequestDTO){
        return tagService.createTag(tagRequestDTO);
    }
}
