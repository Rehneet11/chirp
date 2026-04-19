package com.example.Chirp.services;

import com.example.Chirp.dto.TagRequestDTO;
import com.example.Chirp.dto.TagResponseDTO;
import com.example.Chirp.mapper.TagMapper;
import com.example.Chirp.models.Tag;
import com.example.Chirp.repository.TagRepository;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;

@Service
public class TagService implements ITagService{
    private final TagRepository tagRepository;

    public TagService(TagRepository tagRepository) {
        this.tagRepository = tagRepository;
    }

    @Override
    public Mono<TagResponseDTO> createTag(TagRequestDTO tagRequestDTO) {
        Tag tag = Tag.builder()
                .tag(tagRequestDTO.getTag())
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
        return tagRepository.save(tag)
                .map(TagMapper::tagToTagResponseDTO)
                .doOnSuccess(response -> System.out.println("Tag Created Successfully: " + response))
                .doOnError(error -> System.out.println("Error creating Tag: " + error));
    }
}
