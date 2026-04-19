package com.example.Chirp.dto;

import lombok.*;

import java.time.LocalDateTime;

@Data
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TagResponseDTO {
    private String id;
    private String tag;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
