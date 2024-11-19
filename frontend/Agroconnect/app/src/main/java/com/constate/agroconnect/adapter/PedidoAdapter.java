package com.constate.agroconnect.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.constate.agroconnect.databinding.ItensHistoricoPedidoBinding;
import com.constate.agroconnect.model.Pedido;
import com.constate.agroconnect.model.Produto;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Locale;

public class PedidoAdapter extends RecyclerView.Adapter<PedidoAdapter.PedidoViewHolder> {
    private final ArrayList<Pedido> listaPedidos;
    private final Context context;

    public PedidoAdapter(ArrayList<Pedido> listaPedidos, Context context) {
        this.listaPedidos = listaPedidos;
        this.context = context;
    }

    @NonNull
    @Override
    public PedidoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItensHistoricoPedidoBinding itensHistorico;
        itensHistorico = ItensHistoricoPedidoBinding.inflate(LayoutInflater.from(context), parent, false);
        return new PedidoViewHolder(itensHistorico);
    }

    @Override
    public void onBindViewHolder(@NonNull PedidoViewHolder holder, int position) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd MMMM yyyy", Locale.forLanguageTag("pt-BR"));
        String dataFormatada = listaPedidos.get(position).getDataPedido().format(formatter);
        holder.binding.txtDataPedido.setText(dataFormatada);
        holder.binding.txtStatusPedido.setText(listaPedidos.get(position).getStatus().name());
        holder.binding.txtValorPedido.setText(String.format("R$ %.2f",listaPedidos.get(position).getValorTotal()));

        holder.binding.recyclerViewProdutos.setLayoutManager(new LinearLayoutManager(context));
        holder.binding.recyclerViewProdutos.setAdapter(new ResumoPedidoAdapter(listaPedidos.get(position).getProdutoPedido(), context));
        holder.binding.recyclerViewProdutos.setHasFixedSize(false);

    }

    @Override
    public int getItemCount() {
        return listaPedidos.size();
    }

    static class PedidoViewHolder extends RecyclerView.ViewHolder {
        private ItensHistoricoPedidoBinding binding;

        public PedidoViewHolder(ItensHistoricoPedidoBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
