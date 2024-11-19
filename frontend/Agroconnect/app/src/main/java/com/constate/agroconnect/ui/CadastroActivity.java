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
import com.constate.agroconnect.databinding.ActivityCadastroBinding;
import com.constate.agroconnect.dto.ResponseDTO;
import com.constate.agroconnect.model.Usuario;
import com.constate.agroconnect.service.AuthApiService;
import com.google.android.material.snackbar.Snackbar;

import java.net.HttpURLConnection;
import java.net.URL;

public class CadastroActivity extends AppCompatActivity {
    private ActivityCadastroBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCadastroBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.criarContaButton.setOnClickListener(v -> cadastrar());
        voltarLogin();
    }

    private void cadastrar(){
        if (validarCampos()) {
            String nome = binding.editNomeCadastro.getText().toString();
            String sobrenome = binding.editSobrenomeCadastro.getText().toString();
            String email = binding.editEmailCadastro.getText().toString();
            String senha = binding.editSenhaCadastro.getText().toString();
            String confirmacaoSenha = binding.editConfirmacaoSenhaCadastro.getText().toString();

            if (!binding.editSenhaCadastro.getText().toString().equals(binding.editConfirmacaoSenhaCadastro.getText().toString())) {
                binding.inputSenhaCadastro.setError("As senhas não coincidem.");
                binding.inputConfirmacaoSenhaCadastro.setError("As senhas não coincidem.");
                return;
            }

            Usuario usuario = new Usuario();
            usuario.setNome(nome);
            usuario.setSobrenome(sobrenome);
            usuario.setEmail(email);
            usuario.setSenha(senha);

            new Thread(() ->
            {
                try {
                    ResponseDTO response = AuthApiService.cadastrar(usuario);

                    //Armazanando o token em SharedPreferences
                    SharedPreferences sharedPreferences = getSharedPreferences("MyAppPrefs", MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("token", response.getToken());
                    editor.apply();

                    runOnUiThread(() -> {
                        Toast.makeText(CadastroActivity.this, "Bem-vindo(a) " + nome, Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(CadastroActivity.this, MainActivity.class));
                        finish();
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                    runOnUiThread(() -> {
                        Snackbar.make(binding.getRoot(), "Erro ao fazer Cadastro", Snackbar.LENGTH_SHORT).show();
                        if (e.getMessage() != null) {
                            if (e.getMessage().contains("Este e-mail já está em uso")) {
                                binding.inputEmailCadastro.setError("Este e-mail já está em uso.");
                            } else {
                                Snackbar.make(binding.getRoot(), e.getMessage(), Snackbar.LENGTH_LONG).show();
                            }
                        }
                    });
                }
            }).start();
        }
    }

    private boolean validarCampos() {
        boolean camposValidos = true;
        // Validação de nome
        if (binding.editNomeCadastro.getText().toString().trim().isEmpty()) {
            binding.inputNomeCadastro.setError("O Nome é obrigatório.");
            camposValidos = false;
        } else {
            binding.inputNomeCadastro.setError(null);
        }
        // Validação de sobrenome
        if (binding.editSobrenomeCadastro.getText().toString().trim().isEmpty()) {
            binding.inputSobrenomeCadastro.setError("O Sobrenome é obrigatório.");
            camposValidos = false;
        } else {
            binding.inputSobrenomeCadastro.setError(null);
        }
        // Validação de email
        if (binding.editEmailCadastro.getText().toString().trim().isEmpty()) {
            binding.inputEmailCadastro.setError("O E-mail é obrigatório.");
            camposValidos = false;
        } else {
            binding.inputEmailCadastro.setError(null);
        }
        // Validação de senha
        if (binding.editSenhaCadastro.getText().toString().trim().isEmpty()) {
            binding.inputSenhaCadastro.setError("A senha é obrigatório.");
            camposValidos = false;
        } else {
            binding.inputSenhaCadastro.setError(null);
        }
        // Validação de senha
        if (binding.editConfirmacaoSenhaCadastro.getText().toString().trim().isEmpty()) {
            binding.inputConfirmacaoSenhaCadastro.setError("A confirmação da senha é obrigatório.");
            camposValidos = false;
        } else {
            binding.inputConfirmacaoSenhaCadastro.setError(null);
        }
        return camposValidos;
    }

    public void voltarLogin() {
        binding.txtVoltarLogin.setOnClickListener(v -> {
            startActivity(new Intent(CadastroActivity.this, LoginActivity.class));
        });
    }
}