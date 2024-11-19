package com.constate.api_agroconnect.model;

import com.constate.api_agroconnect.enums.StatusPedido;
import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "tb_pedido")
@Getter
@Setter
public class Pedido {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id_pedido;

    @NotNull
    private LocalDate dataPedido;

    @ElementCollection
    private List<Produto> produtoPedido;

    @NotNull
    private StatusPedido status = StatusPedido.ENVIADO;

    @NotNull
    private double valorTotal;

    @ManyToOne
    @JsonBackReference
    private Usuario usuario;


}
