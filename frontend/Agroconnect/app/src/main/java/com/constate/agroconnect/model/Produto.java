package com.constate.agroconnect.model;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

public class Produto implements Parcelable {
    private int id_produto;
    private int img_produto;
    private String nome;
    private double valor;
    private int quantidade;

    public Produto(){}

    public Produto(int id, int img_produto, String nome, Double valor) {
        this.id_produto = id;
        this.img_produto = img_produto;
        this.nome = nome;
        this.valor = valor;
        this.quantidade = 1;
    }

    public void aumentarQuantidade(){
        quantidade++;
    }

    public void diminuirQuantidade(){
        if(quantidade > 0 ){
            quantidade--;
        }
    }

    public int getId_produto() {
        return id_produto;
    }

    public void setId_produto(int id_produto) {
        this.id_produto = id_produto;
    }

    public int getImg_produto() {
        return img_produto;
    }

    public void setImg_produto(int img_produto) {
        this.img_produto = img_produto;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public double getValor() {
        return valor;
    }

    public void setValor(Double valor) {
        this.valor = valor;
    }

    public int getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(int quantidade) {
        this.quantidade = quantidade;
    }

    // Métodos do Parcelable
    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel parcel, int flags) {
        parcel.writeInt(id_produto);
        parcel.writeInt(img_produto);
        parcel.writeString(nome);
        parcel.writeDouble(valor);
        parcel.writeInt(quantidade);
    }

    protected Produto(Parcel in) {
        id_produto = in.readInt();
        img_produto = in.readInt();
        nome = in.readString();
        valor = in.readDouble();
        quantidade = in.readInt();
    }

    public static final Creator<Produto> CREATOR = new Creator<Produto>() {
        @Override
        public Produto createFromParcel(Parcel in) {
            return new Produto(in);
        }

        @Override
        public Produto[] newArray(int size) {
            return new Produto[size];
        }
    };
}
