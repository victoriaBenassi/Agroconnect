package com.constate.agroconnect.service;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class FreteApiService {
    private static final String BASE_URL = "http://10.0.2.2:8080/frete/calcular?uf=";

    public static double calcularFrete(String token, String uf) {
        try {
            URL url = new URL(BASE_URL + uf);
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

                double valorFrete = Double.parseDouble(result.toString());

                return valorFrete;
            } else {
                System.out.println("Erro ao obter informações do frete: " + responseCode);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0.0;
    }
}
