package com.example.demo.kafka.config;

import com.example.demo.model.Task;
import com.example.demo.model.User;
import com.fasterxml.jackson.databind.JsonSerializer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.LongSerializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.support.converter.StringJsonMessageConverter;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class KafkaProducerConfig {

    @Value("${kafka.server}")
    private String kafkaServer;

    @Value("${kafka.group.id}")
    private String kafkaProducerId;

    @Bean
    public ProducerFactory<Integer, User> producerConfigs() {
        Map<String, Object> properties = new HashMap<>();

        properties.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaServer);
        properties.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        properties.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
        properties.put(ProducerConfig.ENABLE_IDEMPOTENCE_CONFIG, "true");
        properties.put(ProducerConfig.RETRIES_CONFIG, Integer.toString(Integer.MAX_VALUE));
        properties.put(ProducerConfig.ACKS_CONFIG, "all");
        properties.put(ProducerConfig.MAX_IN_FLIGHT_REQUESTS_PER_CONNECTION, "5");
        properties.put(ProducerConfig.COMPRESSION_TYPE_CONFIG, "snappy");
        properties.put(ProducerConfig.LINGER_MS_CONFIG, "20");
        properties.put(ProducerConfig.BATCH_SIZE_CONFIG, Integer.toString(32 * 1024));
        return new DefaultKafkaProducerFactory<>(properties);
    }


    @Bean
    public KafkaTemplate<Integer, User> kafkaTemplate() {
        return new KafkaTemplate<>(producerConfigs());
    }
}
