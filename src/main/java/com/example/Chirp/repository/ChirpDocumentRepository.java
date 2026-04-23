package com.example.Chirp.repository;

import com.example.Chirp.models.ChirpElasticDocument;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.data.elasticsearch.repository.ReactiveElasticsearchRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

import java.util.List;
@Repository
public interface ChirpDocumentRepository extends ReactiveElasticsearchRepository<ChirpElasticDocument,String> {
        Flux<ChirpElasticDocument> findByContentContainingIgnoreCase(String content);
}
