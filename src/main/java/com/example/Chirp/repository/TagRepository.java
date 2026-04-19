package com.example.Chirp.repository;

import com.example.Chirp.models.Chirp;
import com.example.Chirp.models.Tag;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TagRepository extends ReactiveMongoRepository<Tag,String>  {

}
