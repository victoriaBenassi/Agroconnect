package com.constate.agroconnect.ui.fragments.navegacao;

import android.graphics.Color;
import android.os.Bundle;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;

import com.constate.agroconnect.R;
import com.constate.agroconnect.adapter.ProdutoAdapter;
import com.constate.agroconnect.databinding.FragmentHomeBinding;
import com.constate.agroconnect.listaItens.Produtos;
import com.constate.agroconnect.model.Produto;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;
    private ProdutoAdapter produtoAdapter;
    private ArrayList<Produto> listaProduto = new ArrayList<>();
    private List<Produto> listaProdutoFiltrados = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        binding= FragmentHomeBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        RecyclerView recyclerViewProdutos = binding.recyclerViewProdutos;
        recyclerViewProdutos.setLayoutManager(new GridLayoutManager(getContext(), 2));
        recyclerViewProdutos.setHasFixedSize(true);

        produtoAdapter = new ProdutoAdapter(listaProduto, getContext());
        recyclerViewProdutos.setAdapter(produtoAdapter);
        binding.recyclerViewProdutos.setVisibility(View.VISIBLE);

        listaProduto.addAll(Produtos.getTodos());
        listaProdutoFiltrados.addAll(listaProduto);
        produtoAdapter.notifyDataSetChanged();

        configurarCategorias();
        configurarPesquisa();
        voltarAoTopo();
        abrirCarrinho();

        return view;
    }

    private void buttonTodos(){
        binding.buttonCategoriaTodos.setOnClickListener(v -> {
            resetarCategorias();
            listaProduto.clear();
            listaProduto.addAll(Produtos.getTodos());
            produtoAdapter.notifyDataSetChanged();
            binding.txtListaProdutos.setText("Todos");
            binding.buttonCategoriaTodos.setTextColor(Color.WHITE);
            binding.recyclerViewProdutos.setVisibility(View.VISIBLE);
            binding.buttonCategoriaTodos.setBackgroundResource(R.drawable.button_enabled);
        });
    }

    private void buttonLegumes(){
        binding.buttonCategoriaLegumes.setOnClickListener(v -> {
            resetarCategorias();
            listaProduto.clear();
            listaProduto.addAll(Produtos.getLegumes());
            produtoAdapter.notifyDataSetChanged();
            binding.txtListaProdutos.setText("Legumes");
            binding.buttonCategoriaLegumes.setBackgroundResource(R.drawable.button_enabled);
            binding.buttonCategoriaLegumes.setTextColor(Color.WHITE);
            binding.recyclerViewProdutos.setVisibility(View.VISIBLE);
        });
    }

    private void buttonFrutas(){
        binding.buttonCategoriaFrutas.setOnClickListener(v -> {
            resetarCategorias();
            listaProduto.clear();
            listaProduto.addAll(Produtos.getFrutas());
            produtoAdapter.notifyDataSetChanged();
            binding.txtListaProdutos.setText("Frutas");
            binding.buttonCategoriaFrutas.setTextColor(Color.WHITE);
            binding.buttonCategoriaFrutas.setBackgroundResource(R.drawable.button_enabled);
            binding.recyclerViewProdutos.setVisibility(View.VISIBLE);
        });
    }

    private void buttonVerduras(){
        binding.buttonCategoriaVerduras.setOnClickListener(v -> {
            resetarCategorias();
            listaProduto.clear();
            listaProduto.addAll(Produtos.getVerduras());
            produtoAdapter.notifyDataSetChanged();
            binding.txtListaProdutos.setText("Verduras");
            binding.buttonCategoriaVerduras.setTextColor(Color.WHITE);
            binding.buttonCategoriaVerduras.setBackgroundResource(R.drawable.button_enabled);
            binding.recyclerViewProdutos.setVisibility(View.VISIBLE);
        });
    }

    private void configurarCategorias() {
        buttonTodos();
        buttonLegumes();
        buttonFrutas();
        buttonVerduras();
    }

    private void resetarCategorias() {
        // Define a cor padrão para todos os botões
        int corTextoPadrao = ContextCompat.getColor(requireContext(), R.color.edit_texto);

        binding.buttonCategoriaTodos.setTextColor(corTextoPadrao);
        binding.buttonCategoriaTodos.setBackgroundResource(R.drawable.button_disabled);

        binding.buttonCategoriaLegumes.setTextColor(corTextoPadrao);
        binding.buttonCategoriaLegumes.setBackgroundResource(R.drawable.button_disabled);

        binding.buttonCategoriaFrutas.setTextColor(corTextoPadrao);
        binding.buttonCategoriaFrutas.setBackgroundResource(R.drawable.button_disabled);

        binding.buttonCategoriaVerduras.setTextColor(corTextoPadrao);
        binding.buttonCategoriaVerduras.setBackgroundResource(R.drawable.button_disabled);
    }

    private void configurarPesquisa(){
        binding.searchViewPesquisa.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }
            @Override
            public boolean onQueryTextChange(String novoTexto) {
                binding.txtListaProdutos.setVisibility(View.GONE);
                    filtrarProdutos(novoTexto);
                    return true;
                }
            });
    }

    private void filtrarProdutos(String query){
        listaProduto.clear();

        if (TextUtils.isEmpty(query)){
            listaProduto.addAll(listaProdutoFiltrados);
        } else {
            for (Produto produto : listaProdutoFiltrados) {
                if (produto.getNome().toLowerCase().contains(query.toLowerCase())) {
                    listaProduto.add(produto);
                }
            }
        }
        produtoAdapter.notifyDataSetChanged();
    }

    private void voltarAoTopo(){
        binding.fabTopo.setOnClickListener(v-> binding.nestedScrollView.smoothScrollTo(0, 0));
    }

    private void abrirCarrinho(){
        binding.buttonCarrinho.setOnClickListener(v -> {
            NavController navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment);
            navController.navigate(R.id.carrinhoFragment);
        });
    }
}
