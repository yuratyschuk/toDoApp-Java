package com.example.demo.jwt;

import java.io.Serializable;

public class JwtTokenResponse implements Serializable {

    private static final long serialVersionUID = 8317676219297719109L;

    private String token;

    public JwtTokenResponse() {

    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getToken() {
        return this.token;
    }
}
