package com.example.demo.jms;

import com.example.demo.model.User;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;

@Service
public class JmsService {

    JmsTemplate jmsTemplate;

    JmsService(JmsTemplate jmsTemplate) {
        this.jmsTemplate = jmsTemplate;
    }

    public void send(User user) {
        jmsTemplate.convertAndSend("mail", user);
    }
}
