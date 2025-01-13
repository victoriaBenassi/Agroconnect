package com.constate.agroconnect.ui;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.constate.agroconnect.R;
import com.constate.agroconnect.databinding.FragmentDadosContaBinding;
import com.constate.agroconnect.databinding.FragmentPerfilBinding;
import com.constate.agroconnect.dto.ResponseDTO;
import com.constate.agroconnect.model.Usuario;
import com.constate.agroconnect.service.UsuarioApiService;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

public class DadosContaFragment extends Fragment {
    private FragmentDadosContaBinding binding;
    private UsuarioApiService usuarioApiService;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentDadosContaBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        usuarioApiService = new UsuarioApiService();
        dadosDaConta();
        atualizarUsuario();
        confirmarExclusão();
        voltarPerfil();

        return view;
    }
    private void voltarPerfil(){
        binding.buttonVoltarPerfil.setOnClickListener(v -> {
            NavController navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment);
            navController.navigate(R.id.nav_perfil);
        });
    }

    private void dadosDaConta(){
        new Thread(() -> {
            SharedPreferences sharedPreferences = requireActivity().getSharedPreferences("MyAppPrefs", Context.MODE_PRIVATE);
            String token = sharedPreferences.getString("token", null);

            if (token != null) {
                Usuario usuario = usuarioApiService.obterInformacoesUsuario(token);

                requireActivity().runOnUiThread(() -> {
                    if (usuario != null) {
                        binding.editNomePerfil.setText(usuario.getNome());
                        binding.editSobrenomePerfil.setText(usuario.getSobrenome());
                        binding.editEmailPerfil.setText(usuario.getEmail());

                    } else {
                        binding.editNomePerfil.setText("Erro ao carregar nome");
                        binding.editSobrenomePerfil.setText("Erro ao carregar sobrenome");
                        binding.editEmailPerfil.setText("Erro ao carregar email");
                    }
                });
            } else {
                redirecionarParaLogin();
            }
        }).start();
    }
    private void redirecionarParaLogin() {
        Intent intent = new Intent(getActivity(), LoginActivity.class);
        startActivity(intent);
        requireActivity().finish();
    }

    private void atualizarUsuario(){
        binding.buttonSalvarAlteracao.setOnClickListener(v -> {
            Usuario usuarioAtualizado = new Usuario();
            usuarioAtualizado.setNome(binding.editNomePerfil.getText().toString());
            usuarioAtualizado.setSobrenome(binding.editSobrenomePerfil.getText().toString());
            usuarioAtualizado.setEmail(binding.editEmailPerfil.getText().toString());

            new Thread(() -> {
                SharedPreferences sharedPreferences = requireActivity().getSharedPreferences("MyAppPrefs", Context.MODE_PRIVATE);
                String token = sharedPreferences.getString("token", null);
                if (token != null) {
                    try {
                        ResponseDTO response = usuarioApiService.atualizarUsuario(token, usuarioAtualizado);

                        requireActivity().runOnUiThread(() -> {
                            if (response != null) {
                                Toast.makeText(getActivity(), "Usuário atualizado com sucesso.", Toast.LENGTH_SHORT).show();

                                // Atualiza os campos com os dados atualizados
                                binding.editNomePerfil.setText(usuarioAtualizado.getNome());
                                binding.editSobrenomePerfil.setText(usuarioAtualizado.getSobrenome());
                                binding.editEmailPerfil.setText(usuarioAtualizado.getEmail());

                                // armazena o novo token no SharedPreferences
                                SharedPreferences.Editor editor = sharedPreferences.edit();
                                editor.putString("token", response.getToken());
                                editor.apply();
                            } else {
                                Toast.makeText(getActivity(), "Erro ao atualizar usuário.", Toast.LENGTH_SHORT).show();
                            }
                        });

                    } catch (Exception e) {
                        e.printStackTrace();
                        requireActivity().runOnUiThread(() -> {
                            Toast.makeText(getActivity(), "Erro ao atualizar usuário.", Toast.LENGTH_SHORT).show();
                        });
                    }
                }else{
                    requireActivity().runOnUiThread(() -> {
                        Toast.makeText(getActivity(), "Token inválido. Redirecionando para login.", Toast.LENGTH_SHORT).show();
                        redirecionarParaLogin();
                    });
                }
            }).start();
        });
    }

    private void confirmarExclusão(){
        binding.txtExcluirConta.setOnClickListener(v -> {
            new MaterialAlertDialogBuilder(getContext())
                    .setMessage("Tem certeza que deseja excluir sua conta?")
                    .setNegativeButton("Cancelar", (dialog, which) -> {
                        dialog.dismiss();
                    })
                    .setPositiveButton("Confirmar", (dialog, which) -> {
                        excluirContaApi();
                    })
                    .show();
        });
    }

    private void excluirContaApi(){
        new Thread(() -> {
            SharedPreferences sharedPreferences = requireActivity().getSharedPreferences("MyAppPrefs", Context.MODE_PRIVATE);
            String token = sharedPreferences.getString("token", null);

            if (token != null) {
                boolean response = usuarioApiService.excluirUsuario(token);
                requireActivity().runOnUiThread(() -> {
                    if (response) {
                        Toast.makeText(getActivity(), "Conta excluída com sucesso!", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(getActivity(), LoginActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                        getActivity().finish();
                    } else {
                        Toast.makeText(getActivity(), "Erro ao excluir conta.", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }).start();
    }
}