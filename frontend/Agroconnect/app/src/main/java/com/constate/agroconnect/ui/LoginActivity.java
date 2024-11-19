package com.constate.agroconnect.ui;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.constate.agroconnect.MainActivity;
import com.constate.agroconnect.R;
import com.constate.agroconnect.databinding.ActivityLoginBinding;
import com.constate.agroconnect.dto.ResponseDTO;
import com.constate.agroconnect.service.AuthApiService;
import com.google.android.material.snackbar.Snackbar;

public class LoginActivity extends AppCompatActivity {

    private ActivityLoginBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.loginButton.setOnClickListener(v -> login());
    }

    private void login(){
        if (validarCampos()){
            String email = binding.editEmailLogin.getText().toString();
            String senha = binding.editSenhaLogin.getText().toString();

            new Thread(() ->
            {
                try {
                    ResponseDTO response = AuthApiService.login(email, senha);

                    //Armazanando o token em SharedPreferences
                    SharedPreferences sharedPreferences = getSharedPreferences("MyAppPrefs", MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("token", response.getToken());
                    editor.apply();

                    runOnUiThread(() -> {
                        Toast.makeText(LoginActivity.this, "Bem-vindo(a) ", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(LoginActivity.this, MainActivity.class));
                        finish();
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                    runOnUiThread(() -> Snackbar.make(binding.getRoot(), "E-mail ou senha inválidos", Snackbar.LENGTH_SHORT).show());
                }
            }).start();
        }
    }

    public void cadastrar(View view) {
        startActivity(new Intent(LoginActivity.this, CadastroActivity.class));
    }

    private boolean validarCampos() {
        boolean camposValidos = true;

        // Validação de email
        if (binding.editEmailLogin.getText().toString().trim().isEmpty()) {
            binding.inputEmailLogin.setError("O E-mail é obrigatório.");
            camposValidos = false;
        } else {
            binding.inputEmailLogin.setError(null);
        }

        // Validação de senha
        if (binding.editSenhaLogin.getText().toString().trim().isEmpty()) {
            binding.inputSenhaLogin.setError("A senha é obrigatório.");
            camposValidos = false;
        } else {
            binding.inputSenhaLogin.setError(null);
        }

        return camposValidos;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        binding = null;
    }
}