package com.constate.agroconnect.service;

import com.constate.agroconnect.model.Endereco;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class EnderecoApiService {
    private static final String BASE_URL = "http://10.0.2.2:8080/enderecos";

    public static List<Endereco> visualizarEnderecos(String token) {
        try {
            URL url = new URL(BASE_URL + "/visualizar");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Authorization", "Bearer " + token);

            int responseCode = connection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                StringBuilder result = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    result.append(line);
                }
                reader.close();

                System.out.println("Resposta da API: " + result.toString());

                JSONArray jsonResponse = new JSONArray(result.toString());
                List<Endereco> enderecos = new ArrayList<>();

                for(int i=0; i<jsonResponse.length(); i++){
                    JSONObject jsonEndereco = jsonResponse.getJSONObject(i);
                    Endereco endereco = new Endereco();
                    endereco.setId_endereco(Integer.valueOf(jsonEndereco.getString("id_endereco")));
                    endereco.setCep(jsonEndereco.getString("cep"));
                    endereco.setLogradouro(jsonEndereco.getString("logradouro"));
                    endereco.setNumero(jsonEndereco.getString("numero"));
                    endereco.setComplemento(jsonEndereco.getString("complemento"));
                    endereco.setBairro(jsonEndereco.getString("bairro"));
                    endereco.setLocalidade(jsonEndereco.getString("localidade"));
                    endereco.setUf(jsonEndereco.getString("uf"));
                    enderecos.add(endereco);
                }

                return enderecos;
            } else {
                System.out.println("Erro ao visualizar endereços: " + responseCode);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public Endereco adicionarEndereco(Endereco endereco, String token){
        try{
            URL url = new URL(BASE_URL + "/adicionar");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Authorization", "Bearer " + token);
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setDoOutput(true);

            JSONObject jsonEndereco = new JSONObject();
            jsonEndereco.put("logradouro", endereco.getLogradouro());
            jsonEndereco.put("bairro", endereco.getBairro());
            jsonEndereco.put("numero", endereco.getNumero());
            jsonEndereco.put("complemento", endereco.getComplemento());
            jsonEndereco.put("localidade", endereco.getLocalidade());
            jsonEndereco.put("uf", endereco.getUf());
            jsonEndereco.put("cep", endereco.getCep());

            try (OutputStream os = connection.getOutputStream()) {
                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
                writer.write(jsonEndereco.toString());
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
                return endereco;
            } else {
                System.out.println("Erro ao adicionar endereco" + response);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    public static boolean excluirEndereco(String token, int enderecoId) {
        try {
            URL url = new URL(BASE_URL + "/excluir/" + enderecoId);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("DELETE");
            connection.setRequestProperty("Authorization", "Bearer " + token);

            int responseCode = connection.getResponseCode();
            return responseCode == HttpURLConnection.HTTP_OK;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
}