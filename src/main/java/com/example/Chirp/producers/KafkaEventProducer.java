package com.example.Chirp.producers;

import com.example.Chirp.config.KafkaConfig;
import com.example.Chirp.events.ViewCountEvent;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;


@Service
public class KafkaEventProducer {
    private final KafkaTemplate<String,Object> kafkaTemplate;

    public KafkaEventProducer(KafkaTemplate<String, Object> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public Void publishViewCountEvent(ViewCountEvent viewCountEvent){
        kafkaTemplate.send(KafkaConfig.TOPIC_NAME,viewCountEvent.getChirpId(),viewCountEvent)
                .whenComplete((result,err) -> {
                    if (err != null) {
                        System.out.println("Error Publishing view count event : " + err.getMessage());
                    }

                });
        return null;
    }
}
