package com.example.Chirp.dto;

import com.example.Chirp.enums.ChirpType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ChirpResponseDTO {
    @Schema(example = "681f6ab8f0f3e278cb41f001")
    private String id;
    private String content;
    private int likes;
    private int replies;
    private int rechirps;
    private int views;
    private int trendingScore;
    private String rechirpedChirpId;
    private String parentChirpId;
    private String chirperId;
    private List<String> tagIds;
    private ChirpType chirpType;
    @Schema(example = "2026-05-12T16:42:13")
    private LocalDateTime createdAt;
    @Schema(example = "2026-05-12T16:45:42")
    private LocalDateTime updatedAt;
}
