package com.constate.agroconnect.ui;

import static android.content.Context.MODE_PRIVATE;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
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
import com.constate.agroconnect.adapter.ResumoPedidoAdapter;
import com.constate.agroconnect.databinding.BottomSheetRevisaoPedidoBinding;
import com.constate.agroconnect.databinding.FragmentCarrinhoBinding;
import com.constate.agroconnect.databinding.FragmentResumoPedidoBinding;
import com.constate.agroconnect.model.Carrinho;
import com.constate.agroconnect.model.Endereco;
import com.constate.agroconnect.model.Pedido;
import com.constate.agroconnect.model.Produto;
import com.constate.agroconnect.service.EnderecoApiService;
import com.constate.agroconnect.service.FreteApiService;
import com.constate.agroconnect.service.PedidoApiService;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;

public class ResumoPedidoFragment extends Fragment {

    private RecyclerView recyclerViewPedidos;
    private ResumoPedidoAdapter resumoPedidoAdapter;
    private ArrayList<Produto> listaProduto;
    private FragmentResumoPedidoBinding binding;
    private List<Endereco> listaEnderecos;
    private Endereco enderecoSelecionado;
    private String metodoSelecionado;
    private double valorTotal;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding= FragmentResumoPedidoBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        listaEnderecos = new ArrayList<>();

        inicializarRecyclerView();
        voltarCarrinho();
        voltarHome();
        atualizarValor();
        selecionarFormaPagamento();
        selecionarEnderecoEntrega();

        if (binding.txtEnderecoEntrega != null && binding.txtAdicionarEnderecoEntrega != null) {
            binding.buttonFinalizar.setOnClickListener(v -> {
                if (validarCampos()) {
                    showBottonSheetDialog();
                }
            });
        }

        return view;
    }

    private void inicializarRecyclerView(){
        recyclerViewPedidos = binding.recyclerViewProdutosPedidos;
        recyclerViewPedidos.setLayoutManager(new LinearLayoutManager(getContext()));

        listaProduto = getArguments().getParcelableArrayList("listaProdutos");

        if (listaProduto != null){
            resumoPedidoAdapter = new ResumoPedidoAdapter(listaProduto, getContext());
            recyclerViewPedidos.setAdapter(resumoPedidoAdapter);
        }
    }

    private void voltarCarrinho(){
        binding.voltarCarrinho.setOnClickListener(v ->{
            NavController navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment);
            navController.navigate(R.id.carrinhoFragment);
        });
    }

    private void voltarHome(){
        binding.txtAdicionarProdutos.setOnClickListener(v ->{
            NavController navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment);
            navController.navigate(R.id.nav_home);
        });
    }

    private void selecionarFormaPagamento() {
        binding.txtAdicionarFormaPagamento.setOnClickListener(v -> {
            showPagamentoDialogo();
        });
    }

    private void showPagamentoDialogo(){
        String[] opcoesPagamento = {"Debito", "Credito", "Pix" };

        new MaterialAlertDialogBuilder(getContext())
                .setTitle("Pague na entrega:")
                .setItems(opcoesPagamento, (dialog, which) -> {
                    metodoSelecionado = opcoesPagamento[which];
                binding.txtAdicionarFormaPagamento.setText(metodoSelecionado);
                })
                .setNegativeButton("Cancelar", null)
                .show();
    }

    private void selecionarEnderecoEntrega(){
        binding.txtAdicionarEnderecoEntrega.setOnClickListener(v -> {
            getEnderecosDoUsuario();
        });
    }

    private void showEnderecoDialog(){
        if (listaEnderecos != null && !listaEnderecos.isEmpty()) {
            String [] enderecoString = listaEnderecos.stream()
                    .map(endereco -> endereco.getLogradouro() + ", " + endereco.getNumero())
                    .toArray(String[]::new);

            new MaterialAlertDialogBuilder(getContext())
                    .setTitle("Selecione um endereço:")
                    .setItems(enderecoString, (dialog, which) -> {
                        enderecoSelecionado = listaEnderecos.get(which);

                        binding.txtAdicionarEnderecoEntrega.setText(enderecoSelecionado.getLogradouro()+ ", "+ enderecoSelecionado.getNumero());

                        String uf = enderecoSelecionado.getUf();
                        if (uf != null && !uf.isEmpty()){
                            calcularFrete(uf);
                        }
                    })
                    .setNegativeButton("Cancelar", null)
                    .show();
        }
    }

    private void showBottonSheetDialog(){
        BottomSheetDialog dialog = new BottomSheetDialog(requireContext());
        BottomSheetRevisaoPedidoBinding sheetBinding = BottomSheetRevisaoPedidoBinding.inflate(LayoutInflater.from(getContext()), null, false);

        String enderecoTexto = enderecoSelecionado.getLogradouro() + enderecoSelecionado.getNumero();
        sheetBinding.txtEnderecoEntrega.setText(enderecoTexto);

        String enderecoLocalidadeUf = enderecoSelecionado.getLocalidade() + "-" + enderecoSelecionado.getUf();
        sheetBinding.txtLocalidadeUf.setText(enderecoLocalidadeUf);

        sheetBinding.txtFomaPagamentoSelecionada.setText(metodoSelecionado);
        sheetBinding.txtvalorTotalRevisaoPedido.setText(String.format("R$ %.2f", valorTotal));

        sheetBinding.buttonAlterarPedido.setOnClickListener(v -> {
            dialog.dismiss();
            NavController navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment);
            navController.navigate(R.id.carrinhoFragment);
        });

        sheetBinding.buttonFinalizarRevisaoPedido.setOnClickListener(v -> {
            dialog.dismiss();
            criarPedido();
            NavController navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment);
            navController.navigate(R.id.nav_pedidos);
        });

        dialog.setContentView(sheetBinding.getRoot());
        dialog.show();
    }

    private void getEnderecosDoUsuario() {
        new Thread(() -> {
            SharedPreferences sharedPreferences = getActivity().getSharedPreferences("MyAppPrefs", MODE_PRIVATE);
            String token = sharedPreferences.getString("token", null);

            if (token != null) {
                List<Endereco> resultado = EnderecoApiService.visualizarEnderecos(token);

                requireActivity().runOnUiThread( () -> {
                    if (resultado != null && !resultado.isEmpty()){
                        listaEnderecos = resultado;
                        showEnderecoDialog();
                    } else {
                        Snackbar.make(binding.getRoot(), "Nenhum endereço encontrado!", Snackbar.LENGTH_SHORT)
                                .setAction("Adicione", v1 -> {
                                    NavController navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment);
                                    navController.navigate(R.id.adicionarEnderecoFragment);
                                })
                                .show();
                    }
                });
            }
        }).start();
    }

    private void calcularFrete(String uf){
        new Thread(() -> {
            SharedPreferences sharedPreferences = getActivity().getSharedPreferences("MyAppPrefs", MODE_PRIVATE);
            String token = sharedPreferences.getString("token", null);

            if (token != null) {
                double valorFrete = FreteApiService.calcularFrete(token, uf);

                requireActivity().runOnUiThread(() -> {
                    if (valorFrete > 0){
                        binding.valorTaxaEntrega.setText(String.format("R$ %.2f", valorFrete));
                        atualizarValor(valorFrete);
                    } else {
                        Snackbar.make(binding.getRoot(), "Erro ao calcular o frete!", Snackbar.LENGTH_SHORT).show();
                    }
                });
            }
        }).start();
    }

    private void atualizarValor(){
        double subtotal = Carrinho.getInstance().calcularSubtotal();

        binding.valorSubtotal.setText(String.format("R$ %.2f", subtotal));
        binding.txtValorTotalPrecoPedido.setText(String.format("R$ %.2f", subtotal));
    }

    private void atualizarValor(double valorFrete){
        double subtotal = Carrinho.getInstance().calcularSubtotal();
        valorTotal = subtotal + valorFrete;

        binding.valorSubtotal.setText(String.format("R$ %.2f", subtotal));
        binding.txtValorTotalPrecoPedido.setText(String.format("R$ %.2f", valorTotal));
    }

    private void criarPedido(){
        Pedido pedidoNovo = new Pedido();
        pedidoNovo.setProdutoPedido(listaProduto);
        pedidoNovo.setValorTotal(valorTotal);

        new Thread(() -> {
            SharedPreferences sharedPreferences = requireActivity().getSharedPreferences("MyAppPrefs", Context.MODE_PRIVATE);
            String token = sharedPreferences.getString("token", null);
            if (token != null) {
                Pedido pedido = PedidoApiService.criarPedido(pedidoNovo, token);

                requireActivity().runOnUiThread(() -> {
                    if (pedido != null) {
                        NavController navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment);
                        navController.navigate(R.id.nav_pedidos);
                    } else {
                        Snackbar.make(binding.getRoot(), "Erro ao realizar pedido.", Snackbar.LENGTH_SHORT).show();
                    }
                });
            } else {
                requireActivity().runOnUiThread(() -> {
                    Snackbar.make(binding.getRoot(), "Token de autenticação não encontrado.", Snackbar.LENGTH_SHORT).show();
                });
            }
        }).start();
    }

    private boolean validarCampos() {
        if (metodoSelecionado == null || metodoSelecionado.isEmpty()) {
            Snackbar.make(binding.getRoot(), "Por favor, selecione uma forma de pagamento.", Snackbar.LENGTH_SHORT).show();
            return false;
        }

        if (enderecoSelecionado == null) {
            Snackbar.make(binding.getRoot(), "Por favor, selecione um endereço de entrega.", Snackbar.LENGTH_SHORT)
                    .show();
            return false;
        }

        if (listaProduto == null || listaProduto.isEmpty()) {
            Snackbar.make(binding.getRoot(), "O pedido está vazio. Adicione produtos ao carrinho.", Snackbar.LENGTH_SHORT)
                    .setAction("Adicione", v1 -> {
                        NavController navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment);
                        navController.navigate(R.id.nav_home);
                    })

                    .show();
            return false;
        }

        return true;
    }
}