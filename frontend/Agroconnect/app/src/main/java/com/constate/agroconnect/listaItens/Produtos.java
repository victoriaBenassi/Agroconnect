package com.constate.agroconnect.listaItens;

import com.constate.agroconnect.R;
import com.constate.agroconnect.model.Produto;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.stream.Collectors;

public class Produtos {

    public static ArrayList<Produto> getTodos() {
        ArrayList<Produto> todosProdutos = new ArrayList<>();
        todosProdutos.addAll(getLegumes());
        todosProdutos.addAll(getFrutas());
        todosProdutos.addAll(getVerduras());

        todosProdutos.sort(Comparator.comparing(Produto::getNome));
        return todosProdutos;
    }

    public static ArrayList<Produto> getLegumes() {
        ArrayList<Produto> legumeLista = new ArrayList<>();

        legumeLista.add(new Produto(1, R.drawable.abobora, "Abóbora", 8.90));
        legumeLista.add(new Produto(2, R.drawable.batata, "Batata", 1.95));
        legumeLista.add(new Produto(3, R.drawable.cenoura, "Cenoura", 1.90));
        legumeLista.add(new Produto(4 ,R.drawable.chuchu, "Chuchu", 1.80));
        legumeLista.add(new Produto(5, R.drawable.pimentao, "Pimentão", 7.15));
        legumeLista.add(new Produto(6, R.drawable.beterraba, "Beterraba", 1.85));

        legumeLista.sort(Comparator.comparing(Produto::getNome));
        return legumeLista;
    }

    public static ArrayList<Produto> getFrutas() {
        ArrayList<Produto> frutaLista = new ArrayList<>();

        frutaLista.add(new Produto(7, R.drawable.abacaxi, "Abacaxi", 6.00));
        frutaLista.add(new Produto(8, R.drawable.abacate, "Abacate", 6.50));
        frutaLista.add(new Produto(9, R.drawable.banana, "Banana", 1.00));
        frutaLista.add(new Produto(10, R.drawable.laranja, "Laranja", 2.00));
        frutaLista.add(new Produto(11, R.drawable.kiwi, "Kiwi", 4.30));
        frutaLista.add(new Produto(12, R.drawable.mexerica, "Mexerica", 2.45));

        frutaLista.sort(Comparator.comparing(Produto::getNome));
        return frutaLista;
    }

    public static ArrayList<Produto> getVerduras() {
        ArrayList<Produto> verduraLista = new ArrayList<>();

        verduraLista.add(new Produto(13, R.drawable.alface, "Alface", 3.05));
        verduraLista.add(new Produto(14, R.drawable.agriao, "Agrião", 5.50));
        verduraLista.add(new Produto(15, R.drawable.alecrim, "Alecrim", 2.80));
        verduraLista.add(new Produto(16, R.drawable.brocolis, "Brócolis", 9.00));
        verduraLista.add(new Produto(17, R.drawable.couve_flor, "Couve-flor", 10.90));
        verduraLista.add(new Produto(18, R.drawable.rabanete, "Rabanete", 3.45));

        verduraLista.sort(Comparator.comparing(Produto::getNome));
        return verduraLista;
    }
}
