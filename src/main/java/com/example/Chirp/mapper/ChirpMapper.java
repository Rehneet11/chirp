package com.example.Chirp.mapper;

import com.example.Chirp.dto.ChirpResponseDTO;
import com.example.Chirp.models.Chirp;

public class ChirpMapper {
    public static ChirpResponseDTO chirpToChirpResponseDTO(Chirp chirp){
        return ChirpResponseDTO.builder()
                .id(chirp.getId())
                .content(chirp.getContent())
                .chirpType(chirp.getChirpType())
                .likes(chirp.getLikes())
                .replies(chirp.getReplies())
                .trendingScore(chirp.getTrendingScore())
                .views(chirp.getViews())
                .rechirps(chirp.getRechirps())
                .rechirpedChirpId(chirp.getRechirpedChirpId())
                .parentChirpId(chirp.getParentChirpId())
                .chirperId(chirp.getChirperId())
                .tagIds(chirp.getTagIds())
                .createdAt(chirp.getCreatedAt())
                .updatedAt(chirp.getUpdatedAt())
                .build();
    }
}
