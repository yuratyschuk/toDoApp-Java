package com.example.demo.jms;

import com.example.demo.model.User;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

@Component
public class JmsReceiver {

    @JmsListener(destination = "mail", containerFactory = "myFactory")
    public void receiveMessage(User user) {
        System.out.println("Da: " + user);
    }

}
