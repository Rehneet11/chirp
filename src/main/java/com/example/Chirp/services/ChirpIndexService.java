package com.example.Chirp.services;

import com.example.Chirp.models.Chirp;
import com.example.Chirp.models.ChirpElasticDocument;
import com.example.Chirp.repository.ChirpDocumentRepository;
import org.springframework.stereotype.Service;

@Service
public class ChirpIndexService implements IChirpIndexService{
    private final ChirpDocumentRepository chirpDocumentRepository;

    public ChirpIndexService(ChirpDocumentRepository chirpDocumentRepository) {
        this.chirpDocumentRepository = chirpDocumentRepository;
    }

    @Override
    public void createChirpIndex(Chirp chirp) {
        ChirpElasticDocument document = ChirpElasticDocument.builder()
                .id(chirp.getId())
                .content(chirp.getContent())
                .build();
        chirpDocumentRepository.save(document);
    }
}
