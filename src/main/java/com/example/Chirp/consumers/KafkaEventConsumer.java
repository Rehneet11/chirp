package com.example.Chirp.consumers;

import com.example.Chirp.config.KafkaConfig;
import com.example.Chirp.events.ViewCountEvent;
import com.example.Chirp.repository.ChirpRepository;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.stereotype.Service;

@Service
public class KafkaEventConsumer {

    private final ChirpRepository chirpRepository;

    public KafkaEventConsumer(ChirpRepository chirpRepository) {
        this.chirpRepository = chirpRepository;
    }

    @KafkaListener(
            topics = KafkaConfig.TOPIC_NAME,
            groupId = "view-count-consumer",
            containerFactory = "kafkaListenerContainerFactory"
    )
    public void subscribeViewPointEvent(ViewCountEvent viewCountEvent){
        chirpRepository.findById(viewCountEvent.getChirpId())
                .flatMap(chirp -> {
                    chirp.setViews(chirp.getViews()+1);
                    chirp.setTrendingScore(chirp.getTrendingScore()+1);
                    return chirpRepository.save(chirp);
                })
                .doOnSuccess(chirp -> System.out.println("View Count Updated for: " + chirp.getId()))
                .doOnError(error -> System.out.println("Error Updating view count "+ error))
                .subscribe();
    }
}
