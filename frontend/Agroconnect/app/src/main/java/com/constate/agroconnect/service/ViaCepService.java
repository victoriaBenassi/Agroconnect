package com.constate.agroconnect.service;

import com.constate.agroconnect.model.Endereco;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class ViaCepService {

    public static Endereco buscarEnderecoPorCep(String cep) throws Exception {
        if (cep != null && !cep.isEmpty() && cep.length() == 8) {
            try {
                URL url = new URL("https://viacep.com.br/ws/" + cep + "/json/");
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");

                int responseCode = urlConnection.getResponseCode();
                if (responseCode == HttpURLConnection.HTTP_OK) {
                    BufferedReader reader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                    StringBuilder result = new StringBuilder();
                    String line;
                    while ((line = reader.readLine()) != null) {
                        result.append(line);
                    }
                    reader.close();

                    JSONObject jsonObject = new JSONObject(result.toString());

                    if (jsonObject.has("erro") && jsonObject.getBoolean("erro")) {
                        System.out.println("CEP não encontrado ou inválido.");
                    } else {
                        // Extraindo os campos do endereço
                        String logradouro = jsonObject.optString("logradouro", "Não disponível");
                        String bairro = jsonObject.optString("bairro", "Não disponível");
                        String localidade = jsonObject.optString("localidade", "Não disponível"); // Cidade
                        String uf = jsonObject.optString("uf", "Não disponível");

                        Endereco endereco = new Endereco();
                        endereco.setLogradouro(logradouro);
                        endereco.setBairro(bairro);
                        endereco.setLocalidade(localidade);
                        endereco.setUf(uf);

                        return endereco;
                    }
                } else {
                    System.out.println("Erro ao conectar com o serviço ViaCEP. Código de resposta: " + responseCode);
                    return null;
                }
            } catch (Exception e) {
                System.out.println("Erro ao buscar o CEP: " + e.getMessage());
            }
        } else {
            System.out.println("CEP inválido. O CEP deve ter 8 caracteres.");
            return null;
        }
        return null;
    }
}
