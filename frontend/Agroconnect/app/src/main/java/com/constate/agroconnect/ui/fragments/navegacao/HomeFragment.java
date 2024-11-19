package com.constate.agroconnect.ui.fragments.navegacao;

import android.graphics.Color;
import android.os.Bundle;
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
        binding.txtListaProdutos.setBackgroundColor(Color.parseColor("#5AAF3F"));
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

    private void configurarCategorias() {
        binding.buttonCategoriaTodos.setOnClickListener(v -> {
            listaProduto.clear();
            listaProduto.addAll(Produtos.getTodos());
            produtoAdapter.notifyDataSetChanged();
            binding.txtListaProdutos.setText("Todos");
            binding.txtListaProdutos.setBackgroundColor(Color.parseColor("#5AAF3F"));
            binding.recyclerViewProdutos.setVisibility(View.VISIBLE);
        });

        binding.buttonCategoriaLegumes.setOnClickListener(v -> {
            listaProduto.clear();
            listaProduto.addAll(Produtos.getLegumes());
            produtoAdapter.notifyDataSetChanged();
            binding.txtListaProdutos.setText("Legumes");
            binding.txtListaProdutos.setBackgroundColor(Color.parseColor("#D92727"));
            binding.recyclerViewProdutos.setVisibility(View.VISIBLE);
        });

        binding.buttonCategoriaFrutas.setOnClickListener(v -> {
            listaProduto.clear();
            listaProduto.addAll(Produtos.getFrutas());
            produtoAdapter.notifyDataSetChanged();
            binding.txtListaProdutos.setText("Frutas");
            binding.txtListaProdutos.setBackgroundColor(Color.parseColor("#3357D9"));
            binding.recyclerViewProdutos.setVisibility(View.VISIBLE);
        });

        binding.buttonCategoriaVerduras.setOnClickListener(v -> {
            listaProduto.clear();
            listaProduto.addAll(Produtos.getVerduras());
            produtoAdapter.notifyDataSetChanged();
            binding.txtListaProdutos.setText("Verduras");
            binding.txtListaProdutos.setBackgroundColor(Color.parseColor("#FFEB3B"));
            binding.recyclerViewProdutos.setVisibility(View.VISIBLE);
        });
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
