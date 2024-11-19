package com.constate.agroconnect.ui;

import static android.app.ProgressDialog.show;

import android.content.Context;
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
import com.constate.agroconnect.databinding.FragmentAdicionarEnderecoBinding;
import com.constate.agroconnect.databinding.FragmentEnderecoBinding;
import com.constate.agroconnect.model.Endereco;
import com.constate.agroconnect.model.Usuario;
import com.constate.agroconnect.service.EnderecoApiService;
import com.constate.agroconnect.service.ViaCepService;
import com.google.android.material.snackbar.Snackbar;

public class AdicionarEnderecoFragment extends Fragment {
    private FragmentAdicionarEnderecoBinding binding;
    private EnderecoApiService enderecoApiService;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentAdicionarEnderecoBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        enderecoApiService = new EnderecoApiService();

        binding.buttonSalvarEndereco.setOnClickListener(v -> {adicionarEndereco();});
        voltarMeusEnderecos();

        binding.editCepEndereco.setOnFocusChangeListener((v, hasFocus) -> {
            if (!hasFocus) {
                String cep = binding.editCepEndereco.getText().toString();
                if (cep.length() == 8) {
                    buscarEnderecoPorCep(cep);
                }
            }
        });

        return view;
    }

    private void adicionarEndereco(){
        if ((validarCampos())){
            Endereco enderecoNovo = new Endereco();
            enderecoNovo.setLogradouro(binding.editLogradouroEndereco.getText().toString());
            enderecoNovo.setNumero(binding.editNumeroEndereco.getText().toString());
            enderecoNovo.setComplemento(binding.editComplementoEndereco.getText().toString());
            enderecoNovo.setBairro(binding.editBairroEndereco.getText().toString());
            enderecoNovo.setLocalidade(binding.editLocalidadeEndereco.getText().toString());
            enderecoNovo.setUf(binding.editUfEndereco.getText().toString());
            enderecoNovo.setCep(binding.editCepEndereco.getText().toString());

            new Thread(() -> {
                SharedPreferences sharedPreferences = requireActivity().getSharedPreferences("MyAppPrefs", Context.MODE_PRIVATE);
                String token = sharedPreferences.getString("token", null);
                if (token != null) {
                    Endereco endereco = enderecoApiService.adicionarEndereco(enderecoNovo, token);

                    requireActivity().runOnUiThread(() -> {
                        if (endereco != null) {
                            Snackbar.make(binding.getRoot(), "Endereço adicionado com sucesso!", Snackbar.LENGTH_SHORT).show();
                            NavController navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment);
                            navController.navigate(R.id.enderecoFragment);
                        } else {
                            Snackbar.make(binding.getRoot(), "Erro ao adicionar o endereço.", Snackbar.LENGTH_SHORT).show();
                        }
                    });
                } else {
                    requireActivity().runOnUiThread(() -> {
                        Snackbar.make(binding.getRoot(), "Token de autenticação não encontrado.", Snackbar.LENGTH_SHORT).show();
                    });
                }
            }).start();
        }
    }

    private boolean validarCampos() {
        boolean camposValidos = true;

        // Validação de cep
        if (binding.editCepEndereco.getText().toString().trim().isEmpty()) {
            binding.inputCepEndereco.setError("O CEP é obrigatório.");
            camposValidos = false;
        } else {
            binding.inputCepEndereco.setError(null);
        }

        // Validação de Logradouro
        if (binding.editLogradouroEndereco.getText().toString().trim().isEmpty()) {
            binding.inputLogradouroEndereco.setError("O logradouro é obrigatório.");
            camposValidos = false;
        } else {
            binding.inputLogradouroEndereco.setError(null);
        }

        // Validação de Bairro
        if (binding.editBairroEndereco.getText().toString().trim().isEmpty()) {
            binding.inputBairroEndereco.setError("O bairro é obrigatório.");
            camposValidos = false;
        } else {
            binding.inputBairroEndereco.setError(null);
        }

        // Validação de Localidade
        if (binding.editLocalidadeEndereco.getText().toString().trim().isEmpty()) {
            binding.inputCidadeEndereco.setError("A cidade é obrigatória.");
            camposValidos = false;
        } else {
            binding.inputCidadeEndereco.setError(null);
        }

        // Validação de UF
        if (binding.editUfEndereco.getText().toString().trim().isEmpty()) {
            binding.inputEstadoEndereco.setError("O estado é obrigatório.");
            camposValidos = false;
        } else {
            binding.inputEstadoEndereco.setError(null);
        }

        return camposValidos;
    }

    private void buscarEnderecoPorCep(String cep){
        new Thread(() -> {
            try {
                Endereco endereco = ViaCepService.buscarEnderecoPorCep(cep);

                if (endereco != null) {
                    requireActivity().runOnUiThread(() -> {
                        binding.editLogradouroEndereco.setText(endereco.getLogradouro());
                        binding.editBairroEndereco.setText(endereco.getBairro());
                        binding.editLocalidadeEndereco.setText(endereco.getLocalidade());
                        binding.editUfEndereco.setText(endereco.getUf());

                        binding.inputCepEndereco.setError(null);
                    });
                } else {
                    requireActivity().runOnUiThread(() -> {
                        binding.inputCepEndereco.setError("Digite um CEP válido.");
                        Snackbar.make(binding.getRoot(), "CEP não encontrado ou inválido", Snackbar.LENGTH_SHORT).show();
                    });
                }
            } catch (Exception e){
                System.out.println("Erro :" + e.getMessage());
                Toast.makeText(requireContext(), "Erro ao buscar o CEP", Toast.LENGTH_SHORT).show();
            }
        }).start();
    }

    private void voltarMeusEnderecos(){
        binding.buttonVoltarMeusEnderecos.setOnClickListener(v -> {
            NavController navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment);
            navController.navigate(R.id.enderecoFragment);
        });
    }
}