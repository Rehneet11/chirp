package com.example.Chirp.services;

import com.example.Chirp.dto.TagRequestDTO;
import com.example.Chirp.dto.TagResponseDTO;
import com.example.Chirp.models.Tag;
import reactor.core.publisher.Mono;

public interface ITagService {
    public Mono<TagResponseDTO> createTag(TagRequestDTO tagRequestDTO);
}
