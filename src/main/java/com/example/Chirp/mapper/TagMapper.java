package com.example.Chirp.mapper;

import com.example.Chirp.dto.TagResponseDTO;
import com.example.Chirp.models.Tag;

public class TagMapper {
    public static TagResponseDTO tagToTagResponseDTO(Tag tag){
        return TagResponseDTO.builder()
                .tag(tag.getTag())
                .id(tag.getId())
                .createdAt(tag.getCreatedAt())
                .updatedAt(tag.getCreatedAt())
                .build();
    }
}
