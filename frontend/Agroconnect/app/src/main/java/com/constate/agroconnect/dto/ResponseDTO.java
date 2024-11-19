package com.constate.agroconnect.dto;

public class ResponseDTO {
    private Integer id;
    private String token;

    // Construtor
    public ResponseDTO(Integer id, String token) {
        this.id = id;
        this.token = token;
    }

    // Getters
    public Integer getId() {
        return id;
    }

    public String getToken() {
        return token;
    }
}
