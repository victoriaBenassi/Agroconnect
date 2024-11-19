package com.constate.agroconnect.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.constate.agroconnect.databinding.FragmentResumoPedidoBinding;
import com.constate.agroconnect.databinding.ItensPedidoBinding;
import com.constate.agroconnect.databinding.ProdutoCarrinhoBinding;
import com.constate.agroconnect.model.Produto;

import java.util.ArrayList;


public class ResumoPedidoAdapter extends RecyclerView.Adapter<ResumoPedidoAdapter.ResumoPedidoViewHolder> {
    private final ArrayList<Produto> listaProdutoPedido;
    private final Context context;

    public ResumoPedidoAdapter(ArrayList<Produto> listaProdutoPedido, Context context) {
        this.listaProdutoPedido = listaProdutoPedido;
        this.context = context;
    }

    @NonNull
    @Override
    public ResumoPedidoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItensPedidoBinding itensPedido;
        itensPedido = ItensPedidoBinding.inflate(LayoutInflater.from(context), parent, false);
        return new ResumoPedidoViewHolder(itensPedido);
    }

    @Override
    public void onBindViewHolder(@NonNull ResumoPedidoViewHolder holder, int position) {
        Produto produto = listaProdutoPedido.get(position);
        holder.binding.txtQuantidadeItensPedido.setText(String.valueOf(produto.getQuantidade()));
        holder.binding.txtProdutoItensPedido.setText(String.valueOf(produto.getNome()));
    }

    @Override
    public int getItemCount() {
        return listaProdutoPedido.size();
    }

    static class ResumoPedidoViewHolder extends RecyclerView.ViewHolder{
        private ItensPedidoBinding binding;

        public ResumoPedidoViewHolder(ItensPedidoBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
