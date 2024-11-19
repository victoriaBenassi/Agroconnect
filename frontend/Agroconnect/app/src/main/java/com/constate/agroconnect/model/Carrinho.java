package com.constate.agroconnect.model;

import java.util.ArrayList;
import java.util.stream.Collectors;
import java.util.stream.DoubleStream;

public class Carrinho {
    private static Carrinho instance;
    private ArrayList<Produto> produtoCarrinho;

    private Carrinho(){
        produtoCarrinho = new ArrayList<>();
    }

    public static Carrinho getInstance() {
        if (instance == null) {
            instance = new Carrinho();
        }
        return instance;
    }
    public ArrayList<Produto> getProdutosCarrinho() {
        return produtoCarrinho;
    }


    public void adicionarProduto(Produto produto){
        for(Produto p : produtoCarrinho){
            if (p.getNome().equals(produto.getNome())){
                p.aumentarQuantidade();
                return;
            }
        }
        produtoCarrinho.add(produto);
    }

    public void diminuirQuantidade(int id) {
        for (Produto produto : produtoCarrinho) {
            if(produto.getId_produto() == id) {
                produto.diminuirQuantidade();
                if(produto.getQuantidade() <= 0) {
                    produtoCarrinho.remove(produto);
                }
                return;
            }
        }
    }

    public void excluirProduto(int id) {
        for (Produto produto : produtoCarrinho) {
            if (produto.getId_produto() == id) {
                produtoCarrinho.remove(produto);
                break;
            }
        }
    }

    public int quantidadeTotal() {
        return produtoCarrinho.stream().mapToInt(produto -> produto.getQuantidade()).sum();
    }

    public double calcularSubtotal(){
        double total = produtoCarrinho.stream().mapToDouble(p -> p.getValor() * p.getQuantidade()).sum();
        return total;
    }

    public void limparCarrinho() {
        produtoCarrinho.clear();
    }
}
