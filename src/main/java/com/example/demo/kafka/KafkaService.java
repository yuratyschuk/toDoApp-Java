package com.example.demo.kafka;

import com.example.demo.model.Task;
import com.example.demo.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
public class KafkaService {

    private final KafkaTemplate<Object, User> template;

    @Value("${kafka.topicName}")
    private String userTopicName;

    @Autowired
    public KafkaService(KafkaTemplate<Object, User> template) {
        this.template = template;
    }

    public void sendMessageAboutUser(User user) {
        template.send(userTopicName, user);
    }

  
}
