package com.constate.api_agroconnect.model;

import jakarta.persistence.Embeddable;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Embeddable
public class Produto {

    private String nome;
    private double valorUnitario;
    private int quantidade;
}
