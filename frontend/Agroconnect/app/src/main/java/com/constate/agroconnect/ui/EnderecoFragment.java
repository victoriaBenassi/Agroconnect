package com.constate.agroconnect.ui;

import static android.content.Context.MODE_PRIVATE;

import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.constate.agroconnect.R;
import com.constate.agroconnect.adapter.EnderecoAdapter;
import com.constate.agroconnect.databinding.FragmentEnderecoBinding;
import com.constate.agroconnect.model.Endereco;
import com.constate.agroconnect.service.EnderecoApiService;

import java.util.ArrayList;
import java.util.List;

public class EnderecoFragment extends Fragment {
    private RecyclerView recyclerView;
    private FragmentEnderecoBinding binding;
    private EnderecoApiService enderecoApiService;
    private ArrayList<Endereco> listaEndereco;
    private EnderecoAdapter enderecoAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentEnderecoBinding.inflate(inflater, container, false);
        View view = binding.getRoot();
        enderecoApiService = new EnderecoApiService();
        listaEndereco = new ArrayList<>();

        inicializarRecyclerView();
        visualizarEnderecos();
        voltarPerfil();
        buttonAdicionarEndereco();

        return view;
    }

    private void inicializarRecyclerView(){
        RecyclerView recyclerViewCarrinho = binding.recyclerViewEnderecos;

        recyclerViewCarrinho.setLayoutManager(new LinearLayoutManager(getContext()));

        enderecoAdapter = new EnderecoAdapter(listaEndereco, getContext(),this);
        recyclerViewCarrinho.setAdapter(enderecoAdapter);
        binding.recyclerViewEnderecos.setVisibility(View.VISIBLE);
    }

    private void visualizarEnderecos(){
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("MyAppPrefs", MODE_PRIVATE);
        String token = sharedPreferences.getString("token", null);

        binding.recyclerViewEnderecos.setVisibility(View.GONE);
        binding.txtNenhumEndereco.setVisibility(View.GONE);

        if (token != null){
            new Thread(() -> {
                List<Endereco> enderecos = enderecoApiService.visualizarEnderecos(token);
                if (enderecos != null && !enderecos.isEmpty()){
                    requireActivity().runOnUiThread(() -> {
                        binding.recyclerViewEnderecos.setVisibility(View.VISIBLE);
                        listaEndereco.clear();
                        listaEndereco.addAll(enderecos);
                        enderecoAdapter.notifyDataSetChanged();
                    });
                } else {
                    requireActivity().runOnUiThread(() -> {
                        binding.recyclerViewEnderecos.setVisibility(View.GONE);
                        binding.txtNenhumEndereco.setVisibility(View.VISIBLE);
                    });
                }
            }).start();
        }
    }

    public void excluirEndereco(int enderecoId){
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("MyAppPrefs", MODE_PRIVATE);
        String token = sharedPreferences.getString("token", null);

        if (token != null) {
            new Thread(() -> {
                boolean sucesso = enderecoApiService.excluirEndereco(token, enderecoId);

                requireActivity().runOnUiThread(() -> {
                    if (sucesso) {
                        for (Endereco endereco : listaEndereco) {
                            if (endereco.getId_endereco() == enderecoId) {
                                listaEndereco.remove(endereco);
                                break;
                            }
                        }
                        enderecoAdapter.notifyDataSetChanged();

                    }
                });
            }).start();
        }
    }

    private void voltarPerfil(){
        binding.iconVoltarPerfil.setOnClickListener(v -> {
            NavController navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment);
            navController.navigate(R.id.nav_perfil);
        });
    }

    private void buttonAdicionarEndereco(){
        binding.fabAdicionar.setOnClickListener(v -> {
            NavController navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment);
            navController.navigate(R.id.adicionarEnderecoFragment);
        });
    }

}