package com.example.Entity;

import java.io.Serializable;

/**
 * Created by chenlu on 2017/11/24.
 */

public class JwtAuthenticationResponse implements Serializable{
    private static final long serialVersionUID = -3886250558987261760L;
    private final String token;
    public JwtAuthenticationResponse(String token) {
        this.token = token;
    }

    public String getToken() {
        return this.token;
    }
}
