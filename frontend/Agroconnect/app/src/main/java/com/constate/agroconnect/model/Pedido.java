package com.constate.agroconnect.model;

import com.constate.agroconnect.enums.StatusPedido;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Pedido {
    private Integer id_pedido;
    private LocalDate dataPedido;
    private ArrayList<Produto> produtoPedido;
    private StatusPedido status = StatusPedido.ENVIADO;
    private double valorTotal;
    private Usuario usuario;

    public Integer getId_pedido() {
        return id_pedido;
    }

    public void setId_pedido(Integer id_pedido) {
        this.id_pedido = id_pedido;
    }

    public LocalDate getDataPedido() {
        return dataPedido;
    }

    public void setDataPedido(LocalDate dataPedido) {
        this.dataPedido = dataPedido;
    }

    public ArrayList<Produto> getProdutoPedido() {
        return produtoPedido;
    }

    public void setProdutoPedido(ArrayList<Produto> produtoPedido) {
        this.produtoPedido = produtoPedido;
    }

    public StatusPedido getStatus() {
        return status;
    }

    public void setStatus(StatusPedido status) {
        this.status = status;
    }

    public double getValorTotal() {
        return valorTotal;
    }

    public void setValorTotal(double valorTotal) {
        this.valorTotal = valorTotal;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }
}
