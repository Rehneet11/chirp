package com.example.Chirp.models;

import com.example.Chirp.enums.ChirpType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "chirps")
public class Chirp {
    @Id
    private String id;

    private ChirpType chirpType;

    @NotBlank(message = "Chirp can't be Blank")
    @Size(min=10,max=1000,message = "Chirp Longer Buddy")
    private String content;

    @Indexed
    private String parentChirpId;

    private String chirperId;

    private int likes;

    private int replies;

    private int views;

    private String rechirpedChirpId;

    private int trendingScore;
    private int rechirps;

    private boolean isDeleted = false;
    private LocalDateTime deletedAt;

    private List<String> tagIds;

    @CreatedDate
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime updatedAt;
}
