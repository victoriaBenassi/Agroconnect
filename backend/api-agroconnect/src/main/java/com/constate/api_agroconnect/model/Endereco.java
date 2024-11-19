package com.constate.api_agroconnect.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;

@Entity
@Table(name = "tb_endereco")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Endereco {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id_endereco;

    @NotNull
    @Size(min = 8, max = 8)
    @Column(nullable = false)
    private String cep;

    @NotNull
    @Size(max = 30)
    @Column(nullable = false)
    private String logradouro;

    @Size(max = 10)
    private String numero;

    @Size(max = 30)
    private String complemento;

    @NotNull
    @Size(max = 30)
    private String bairro;

    @NotNull
    @Size(max = 30)
    private String localidade;

    @NotNull
    @Size(min = 2, max = 2)
    private String uf;

    @ManyToOne
    @JsonBackReference
    private Usuario usuario;
}
