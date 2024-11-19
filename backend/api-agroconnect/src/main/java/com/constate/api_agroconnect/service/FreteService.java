package com.constate.api_agroconnect.service;

import org.springframework.stereotype.Service;

@Service
public class FreteService {

    public double calcularValor(String uf){
        return switch (uf.toUpperCase()) {
            case "SP" -> 7.00;
            case "RJ" -> 10.00;
            default -> 30.00; // para outras regioes
        };
    }
}
