package com.example.Chirp.services;

import com.example.Chirp.models.Chirp;
import com.example.Chirp.models.ChirpElasticDocument;

public interface IChirpIndexService {
    void createChirpIndex(Chirp chirp);
}
