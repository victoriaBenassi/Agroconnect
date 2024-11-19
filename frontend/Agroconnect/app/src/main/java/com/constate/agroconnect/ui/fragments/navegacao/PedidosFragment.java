package com.constate.agroconnect.ui.fragments.navegacao;

import static android.content.Context.MODE_PRIVATE;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.constate.agroconnect.R;
import com.constate.agroconnect.adapter.EnderecoAdapter;
import com.constate.agroconnect.adapter.PedidoAdapter;
import com.constate.agroconnect.databinding.FragmentPedidosBinding;
import com.constate.agroconnect.model.Endereco;
import com.constate.agroconnect.model.Pedido;
import com.constate.agroconnect.service.PedidoApiService;
import com.constate.agroconnect.ui.LoginActivity;

import java.util.ArrayList;
import java.util.List;

public class PedidosFragment extends Fragment {
    private RecyclerView recyclerView;
    private FragmentPedidosBinding binding;
    private PedidoApiService pedidoApiService;
    private ArrayList<Pedido> listaPedido;
    private PedidoAdapter pedidoAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentPedidosBinding.inflate(inflater, container, false);
        View view = binding.getRoot();
        pedidoApiService = new PedidoApiService();
        listaPedido = new ArrayList<>();

        inicializarRecyclerView();
        visualizarPedidos();

        return view;
    }

    private void inicializarRecyclerView(){
        RecyclerView recyclerViewPedidos = binding.recyclerViewPedidos;

        recyclerViewPedidos.setLayoutManager(new LinearLayoutManager(getContext()));

        pedidoAdapter = new PedidoAdapter(listaPedido, getContext());
        recyclerViewPedidos.setAdapter(pedidoAdapter);
        binding.recyclerViewPedidos.setVisibility(View.VISIBLE);
    }

    private void visualizarPedidos(){
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("MyAppPrefs", MODE_PRIVATE);
        String token = sharedPreferences.getString("token", null);

        if (token == null || token.isEmpty()) {
            Intent intent = new Intent(getActivity(), LoginActivity.class);
            startActivity(intent);
            requireActivity().finish();
            return;
        }

        new Thread(() -> {
            List<Pedido> pedidos = pedidoApiService.visualizarPedido(token);
            if (pedidos != null && !pedidos.isEmpty()){
                pedidos.sort((p1, p2) -> p2.getDataPedido().compareTo(p1.getDataPedido()));

                requireActivity().runOnUiThread(() -> {
                    binding.recyclerViewPedidos.setVisibility(View.VISIBLE);
                    listaPedido.clear();
                    listaPedido.addAll(pedidos);
                    pedidoAdapter.notifyDataSetChanged();
                });
            } else {
                requireActivity().runOnUiThread(() -> {
                    binding.recyclerViewPedidos.setVisibility(View.GONE);
                    binding.txtNenhumPedido.setVisibility(View.VISIBLE);
                });
            }
        }).start();
    }
}