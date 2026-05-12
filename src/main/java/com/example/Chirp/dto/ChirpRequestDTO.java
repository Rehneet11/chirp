package com.example.Chirp.dto;

import com.example.Chirp.enums.ChirpType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ChirpRequestDTO {
    @Schema(description = "Chirp body text", example = "Building Chirp one reactive endpoint at a time.")
    @NotBlank(message = "Chirp can't be Blank")
    @Size(min=10,max=1000,message = "Chirp Longer Buddy")
    private String content;
    @Schema(description = "Parent chirp ID when creating a reply", example = "681f6ab8f0f3e278cb41f001")
    private String parentChirpId;
    @Schema(description = "Author ID of the chirp", example = "user_123")
    private String chirperId;
    @Schema(description = "Type of chirp being created")
    private ChirpType chirpType;
    @Schema(description = "Tag IDs associated with the chirp")
    private List<String> tagIds;
}
