package com.constate.api_agroconnect.controller;

import com.constate.api_agroconnect.service.FreteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/frete")
public class FreteController {

    @Autowired
    private FreteService freteService;

    @GetMapping("/calcular")
    public ResponseEntity<Double> calcularFrete(@RequestParam String uf){
        double valorFrete = freteService.calcularValor(uf);
        return ResponseEntity.ok(valorFrete);
    }
}
