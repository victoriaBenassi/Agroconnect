package com.constate.agroconnect.service;

import com.constate.agroconnect.enums.StatusPedido;
import com.constate.agroconnect.model.Endereco;
import com.constate.agroconnect.model.Pedido;
import com.constate.agroconnect.model.Produto;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class PedidoApiService {
    private static final String BASE_URL = "http://10.0.2.2:8080/pedido";

    public static Pedido criarPedido(Pedido pedido, String token){
        try{
            URL url = new URL(BASE_URL + "/criar");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Authorization", "Bearer " + token);
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setDoOutput(true);

            JSONObject jsonPedido = new JSONObject();
            jsonPedido.put("dataPedido", pedido.getDataPedido());
            jsonPedido.put("status", pedido.getStatus().name());

            JSONArray produtosArray = new JSONArray();
            for (Produto produto : pedido.getProdutoPedido()) {
                JSONObject produtoJson = new JSONObject();
                produtoJson.put("nome", produto.getNome());
                produtoJson.put("valorUnitario", produto.getValor());
                produtoJson.put("quantidade", produto.getQuantidade());
                produtosArray.put(produtoJson);
            }
            jsonPedido.put("produtoPedido", produtosArray);
            jsonPedido.put("valorTotal", pedido.getValorTotal());

            try (OutputStream os = connection.getOutputStream()) {
                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
                writer.write(jsonPedido.toString());
                writer.flush();
            }

            int response = connection.getResponseCode();
            if (response == HttpURLConnection.HTTP_OK) {
                BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                StringBuilder result = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    result.append(line);
                }
                reader.close();
                System.out.println("Resposta da API: " + result.toString());
                return pedido;
            } else {
                System.out.println("Erro ao criar pedido " + response);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    public static ArrayList<Pedido> visualizarPedido(String token){
        try {
            URL url = new URL(BASE_URL + "/visualizar");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Authorization", "Bearer " + token);


            int responseCode = connection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK){
                BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                StringBuilder result = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null){
                    result.append(line);
                }
                reader.close();

                System.out.println("Resposta da API: " + result.toString());

                JSONArray jsonResponse = new JSONArray(result.toString());
                ArrayList<Pedido> pedidos = new ArrayList<>();

                for (int i = 0; i< jsonResponse.length(); i++){
                    JSONObject jsonPedido = jsonResponse.getJSONObject(i);
                    Pedido pedido = new Pedido();
                    pedido.setDataPedido(LocalDate.parse(jsonPedido.getString("dataPedido")));
                    pedido.setStatus(StatusPedido.valueOf(jsonPedido.getString("status")));
                    pedido.setValorTotal(jsonPedido.getDouble("valorTotal"));

                    ArrayList<Produto> produtos = new ArrayList<>();
                    JSONArray produtosArray = jsonPedido.getJSONArray("produtoPedido");
                    for (int j = 0; j < produtosArray.length(); j++) {
                        JSONObject produtoJson = produtosArray.getJSONObject(j);
                        Produto produto = new Produto();
                        produto.setNome(produtoJson.getString("nome"));
                        produto.setValor(produtoJson.getDouble("valorUnitario"));
                        produto.setQuantidade(produtoJson.getInt("quantidade"));
                        produtos.add(produto);
                    }
                    pedido.setProdutoPedido(produtos);
                    pedidos.add(pedido);
                }
                return pedidos;
            } else {
                System.out.println("Erro ao visualizar pedidos: " + responseCode);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}
