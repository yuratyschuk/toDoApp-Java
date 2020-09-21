package com.example.demo.jwt;

import io.jsonwebtoken.Clock;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class ClockImpl implements Clock {

    @Override
    public Date now() {
        return new Date();
    }
}
