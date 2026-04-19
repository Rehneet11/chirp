package com.example.Chirp.dto;

import com.example.Chirp.enums.ChirpType;
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
    private String id;
    private String content;
    private String parentChirpId;
    private String chirperId;
    private List<String> tagIds;
    private ChirpType chirpType;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
