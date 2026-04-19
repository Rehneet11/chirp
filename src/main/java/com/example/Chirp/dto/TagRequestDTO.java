package com.example.Chirp.dto;

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
public class TagRequestDTO {
    @NotBlank(message = "Tag can't be blank")
    @Size(min = 3, max = 25, message = "Tag should be at least 3 characters long")
    private String tag;
}
