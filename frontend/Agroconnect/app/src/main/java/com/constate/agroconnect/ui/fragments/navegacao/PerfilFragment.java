package com.constate.agroconnect.ui.fragments.navegacao;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.security.keystore.UserPresenceUnavailableException;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.constate.agroconnect.R;
import com.constate.agroconnect.databinding.FragmentPerfilBinding;
import com.constate.agroconnect.databinding.FragmentResumoPedidoBinding;
import com.constate.agroconnect.model.Usuario;
import com.constate.agroconnect.service.UsuarioApiService;
import com.constate.agroconnect.ui.LoginActivity;

public class PerfilFragment extends Fragment {

    private FragmentPerfilBinding binding;
    private UsuarioApiService usuarioApiService;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding= FragmentPerfilBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        usuarioApiService = new UsuarioApiService();

        perfil();
        dadosDaConta();
        enderecos();
        ativarModoDark();
        sair();

        return view;
    }

    private void perfil() {
        new Thread(() -> {
            SharedPreferences sharedPreferences = requireActivity().getSharedPreferences("MyAppPrefs", Context.MODE_PRIVATE);
            String token = sharedPreferences.getString("token", null);

            if (token != null) {
                Usuario usuario = usuarioApiService.obterInformacoesUsuario(token);

                requireActivity().runOnUiThread(() -> {
                    if (usuario != null) {
                        String nomePerfil = usuario.getNome() + " " + usuario.getSobrenome();
                        binding.txtNomeUsuario.setText(nomePerfil);
                        binding.txtEmailUsuario.setText(usuario.getEmail());
                    } else {
                        binding.txtNomeUsuario.setText("Erro ao carregar nome");
                        binding.txtEmailUsuario.setText("Erro ao carregar email");
                    }
                });
            } else {
                requireActivity().runOnUiThread(() -> {
                    binding.txtNomeUsuario.setText("Usuário não logado");
                    binding.txtEmailUsuario.setText("");
                });
            }
        }).start();
    }

    private void dadosDaConta(){
        binding.txtDadosConta.setOnClickListener(v -> {
            NavController navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment);
            navController.navigate(R.id.dadosContaFragment);
        });
    }

    private void enderecos(){
        binding.txtMeusEnderecos.setOnClickListener(v -> {
            NavController navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment);
            navController.navigate(R.id.enderecoFragment);
        });
    }

    private void ativarModoDark(){
        //recupera preferencia
        SharedPreferences sharedPreferences = requireActivity().getSharedPreferences("AppSettings", Context.MODE_PRIVATE);
        boolean isDarkMode = sharedPreferences.getBoolean("isDarkMode", false);
        // Inicializa de acordo com a preferência salva
        binding.swModoDark.setChecked(isDarkMode);

        binding.swModoDark.setOnCheckedChangeListener((buttonView, isChecked) -> {
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putBoolean("isDarkMode", isChecked);
            editor.apply();

            if (isChecked){
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES); // Modo escuro
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO); // Modo claro
            }
        });
    }

    private void sair(){
        binding.buttonSair.setOnClickListener(v-> {
            Intent intent = new Intent(getActivity(), LoginActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            getActivity().finish();
        });
    }
}