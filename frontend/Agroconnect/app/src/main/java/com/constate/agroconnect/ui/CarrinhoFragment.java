package com.constate.agroconnect.ui;

import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.constate.agroconnect.R;
import com.constate.agroconnect.adapter.CarrinhoAdapter;
import com.constate.agroconnect.adapter.ProdutoAdapter;
import com.constate.agroconnect.databinding.FragmentCarrinhoBinding;
import com.constate.agroconnect.databinding.FragmentHomeBinding;
import com.constate.agroconnect.listaItens.Produtos;
import com.constate.agroconnect.model.Carrinho;
import com.constate.agroconnect.model.Produto;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;

public class CarrinhoFragment extends Fragment  implements  CarrinhoAdapter.OnCarrinhoChangeListener{

    private FragmentCarrinhoBinding binding;
    private CarrinhoAdapter carrinhoAdapter;
    private ArrayList<Produto> produtosCarrinho = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding= FragmentCarrinhoBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        inicializarRecyclerView();
        voltarHome();
        atualizarTotal();
        continuarPedido();

        return view;
    }

    private void inicializarRecyclerView(){
        RecyclerView recyclerViewCarrinho = binding.recyclerViewCarrinho;

        recyclerViewCarrinho.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerViewCarrinho.setHasFixedSize(true);

        carrinhoAdapter = new CarrinhoAdapter(produtosCarrinho, getContext(),this);
        recyclerViewCarrinho.setAdapter(carrinhoAdapter);

        produtosCarrinho.addAll(Carrinho.getInstance().getProdutosCarrinho());
        carrinhoAdapter.notifyDataSetChanged();

        if (produtosCarrinho.isEmpty()){
            binding.recyclerViewCarrinho.setVisibility(View.GONE);
            binding.txtNenhumProdutoCarrinho.setVisibility(View.VISIBLE);
        } else{
            binding.recyclerViewCarrinho.setVisibility(View.VISIBLE);
            binding.txtNenhumProdutoCarrinho.setVisibility(View.GONE);
        }

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleCallback);
        itemTouchHelper.attachToRecyclerView(recyclerViewCarrinho);
    }

    ItemTouchHelper.SimpleCallback simpleCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {

        @Override
        public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
            return false;
        }
        @Override
        public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
            int position = viewHolder.getAdapterPosition();
            Produto produtoRemovido = produtosCarrinho.get(position);

            Carrinho.getInstance().excluirProduto(produtoRemovido.getId_produto());
            produtosCarrinho.remove(position);
            carrinhoAdapter.notifyItemRemoved(position);
            atualizarTotal();
        }
    };

        public void onCarrinhoChanged(){
        atualizarTotal();
    }

    private void voltarHome(){
        binding.iconMais.setOnClickListener(v -> {
            NavController navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment);
            navController.navigate(R.id.nav_home);
        });
    }

    private void atualizarTotal() {
        double total = Carrinho.getInstance().calcularSubtotal();
        binding.txtValorTotalPreco.setText(String.format("R$ %.2f", total));
    }

    private void continuarPedido(){
        binding.buttonContinuar.setOnClickListener(v -> {
            ArrayList<Produto> produtosSelecionados = new ArrayList<>(Carrinho.getInstance().getProdutosCarrinho());

            Bundle bundle = new Bundle();
            bundle.putParcelableArrayList("listaProdutos", produtosSelecionados);

            if (produtosSelecionados != null && !produtosSelecionados.isEmpty()) {
                NavController navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment);
                navController.navigate(R.id.resumoPedidoFragment, bundle); // Passa o bundle aqui
            } else {
                Snackbar.make(binding.getRoot(), "Adicione produtos ao carrinho para continuar.", Snackbar.LENGTH_SHORT)
                        .setAction("Selecione", v1 -> {
                            NavController navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment);
                            navController.navigate(R.id.nav_home);
                        })
                        .show();
            }


        });
    }

}