package com.example.Chirp.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.time.LocalDateTime;

@Data
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TagResponseDTO {
    @Schema(example = "681f6ab8f0f3e278cb41f010")
    private String id;
    private String tag;
    @Schema(example = "2026-05-12T16:42:13")
    private LocalDateTime createdAt;
    @Schema(example = "2026-05-12T16:45:42")
    private LocalDateTime updatedAt;
}
