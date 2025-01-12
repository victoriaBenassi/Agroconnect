package com.constate.agroconnect.service;

import android.content.Context;
import android.content.SharedPreferences;

import com.constate.agroconnect.model.Usuario;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.FormatFlagsConversionMismatchException;

public class UsuarioApiService {
    private static final String BASE_URL = "http://10.0.2.2:8080/usuario";

    public Usuario obterInformacoesUsuario(String token) {
        try {
            URL url = new URL(BASE_URL + "/info");
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
                JSONObject jsonResponse = new JSONObject(result.toString());

                Usuario usuario = new Usuario();
                usuario.setNome(jsonResponse.getString("nome"));
                usuario.setSobrenome(jsonResponse.getString("sobrenome"));
                usuario.setEmail(jsonResponse.getString("email"));

                return usuario;
            } else {
                System.out.println("Erro ao obter informações do usuário: " + responseCode);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    public Usuario atualizarUsuario(Context context, String token, Usuario usuarioAtualizado) {
        try {
            URL url = new URL(BASE_URL + "/atualizar");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("PATCH");
            connection.setRequestProperty("Authorization", "Bearer " + token);
            connection.setDoOutput(true);
            connection.setRequestProperty("Content-Type", "application/json");

            JSONObject jsonInput = new JSONObject();
            if (usuarioAtualizado.getNome() != null) {
                jsonInput.put("nome", usuarioAtualizado.getNome());
            }
            if (usuarioAtualizado.getSobrenome() != null) {
                jsonInput.put("sobrenome", usuarioAtualizado.getSobrenome());
            }
            if (usuarioAtualizado.getEmail() != null) {
                jsonInput.put("email", usuarioAtualizado.getEmail());
            }

            try (OutputStream os = connection.getOutputStream()) {
                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
                writer.write(jsonInput.toString());
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
                JSONObject jsonAtualizado = new JSONObject(result.toString());

                String novoToken = null;
                if(jsonAtualizado.has("token")){
                    novoToken = jsonAtualizado.getString("token");

                    SharedPreferences sharedPreferences = context.getSharedPreferences("MyAppPrefs", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("token", novoToken);
                    editor.apply();
                }

                Usuario usuario = new Usuario();
                usuario.setNome(jsonAtualizado.getString("nome"));
                usuario.setSobrenome(jsonAtualizado.getString("sobrenome"));
                usuario.setEmail(jsonAtualizado.getString("email"));
                return usuario;
            } else {
                System.out.println("Erro ao atualizar informações do usuario " + response);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    public boolean excluirUsuario(String token) {
        try {
            URL url = new URL(BASE_URL + "/excluir");
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
