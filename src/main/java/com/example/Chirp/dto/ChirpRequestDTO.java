package com.example.Chirp.dto;

import com.example.Chirp.enums.ChirpType;
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
    @NotBlank(message = "Chirp can't be Blank")
    @Size(min=10,max=1000,message = "Chirp Longer Buddy")
    private String content;
    private String parentChirpId;
    private String chirperId;
    private ChirpType chirpType;
    private List<String> tagIds;
}
