package com.constate.agroconnect.service;

import com.constate.agroconnect.dto.ResponseDTO;
import com.constate.agroconnect.model.Usuario;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

public class AuthApiService {
    private static final String BASE_URL = "http://10.0.2.2:8080/auth";

    public static ResponseDTO login(String email, String senha) throws Exception {
        return enviarRequest("/login", criarLoginJson(email,senha));
    }

    public static ResponseDTO cadastrar(Usuario usuario) throws Exception{
        return enviarRequest("/cadastrar", criarCadastroJson(usuario));
    }

    private static JSONObject criarLoginJson(String email, String senha) throws JSONException {
        JSONObject jsonLogin = new JSONObject();
        jsonLogin.put("email", email);
        jsonLogin.put("senha", senha);
        return jsonLogin;
    }

    private static JSONObject criarCadastroJson(Usuario usuario) throws JSONException {
        // Cria um objeto JSON com os dados de cadastro do usuario.
        JSONObject jsonUsuario = new JSONObject();
        jsonUsuario.put("nome", usuario.getNome());
        jsonUsuario.put("sobrenome", usuario.getSobrenome());
        jsonUsuario.put("email", usuario.getEmail());
        jsonUsuario.put("senha", usuario.getSenha());
        return jsonUsuario;
    }

    private static ResponseDTO enviarRequest(String endpoint, JSONObject json) throws Exception{
        URL url = new URL(BASE_URL + endpoint);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("POST");
        connection.setRequestProperty("Content-Type", "application/json");
        connection.setDoOutput(true);

        try (BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(connection.getOutputStream()))){
            writer.write(json.toString());
            writer.flush();
        }

        StringBuilder result = new StringBuilder();
        int respostaCode = connection.getResponseCode();
        if (respostaCode != HttpURLConnection.HTTP_OK) {
            throw new Exception("Falha na conexão: "+ respostaCode + "\nDetalhes: " + result.toString());
        }

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()))){
            String line;
            while((line = reader.readLine()) != null){
                result.append(line);
            }
        }
        JSONObject responseJson = new JSONObject(result.toString());
        return new ResponseDTO(responseJson.getInt("id"), responseJson.getString("token"));
    }
}
