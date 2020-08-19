package com.example.demo.kafka;

import com.example.demo.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class KafkaService {

    private final KafkaTemplate<Object, User> template;

    @Value("${kafka.topicName}")
    private String topicName;

    @Autowired
    public KafkaService(KafkaTemplate<Object, User> template) {
        this.template = template;
    }

    public void sendMessage(User user) {
        template.send(topicName, user);
    }

}
