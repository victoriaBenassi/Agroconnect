package com.constate.agroconnect.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.constate.agroconnect.R;
import com.constate.agroconnect.databinding.ProdutoCarrinhoBinding;
import com.constate.agroconnect.databinding.ProdutoItemBinding;
import com.constate.agroconnect.model.Carrinho;
import com.constate.agroconnect.model.Produto;

import java.util.ArrayList;

public class CarrinhoAdapter extends RecyclerView.Adapter<CarrinhoAdapter.CarrinhoViewHolder> {
    private final ArrayList<Produto> listaProdutosCarrinho;
    private final Context context;
    private OnCarrinhoChangeListener listener;

    public CarrinhoAdapter(ArrayList<Produto> listaProdutosCarrinho, Context context, OnCarrinhoChangeListener listener) {
        this.listaProdutosCarrinho = listaProdutosCarrinho;
        this.context = context;
        this.listener = listener;
    }

    @NonNull
    @Override
    public CarrinhoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ProdutoCarrinhoBinding produtoCarrinho;
        produtoCarrinho = ProdutoCarrinhoBinding.inflate(LayoutInflater.from(context), parent, false);
        return new CarrinhoViewHolder(produtoCarrinho);
    }

    @Override
    public void onBindViewHolder(@NonNull CarrinhoViewHolder holder, int position) {
        Produto produto = listaProdutosCarrinho.get(position);
        holder.binding.imgViewProdutoCarrinho.setImageResource(listaProdutosCarrinho.get(position).getImg_produto());
        holder.binding.txtNomeProdutoCarrinho.setText(listaProdutosCarrinho.get(position).getNome());
        holder.binding.txtPrecoCarrinho.setText(String.format("R$ %.2f",listaProdutosCarrinho.get(position).getValor()));
        holder.binding.txtQuantidadeProdutoCarrinho.setText(String.valueOf(listaProdutosCarrinho.get(position).getQuantidade()));

        holder.binding.buttonAumentar.setOnClickListener(v -> {
            Carrinho.getInstance().adicionarProduto(produto);
            holder.binding.txtQuantidadeProdutoCarrinho.setText(String.valueOf(produto.getQuantidade()));
            notifyItemChanged(position);
            listener.onCarrinhoChanged();
        });

        holder.binding.buttonDiminuir.setOnClickListener(v -> {
            Carrinho.getInstance().diminuirQuantidade(produto.getId_produto());
            holder.binding.txtQuantidadeProdutoCarrinho.setText(String.valueOf(produto.getQuantidade()));
            notifyItemChanged(position);
            listener.onCarrinhoChanged();
        });
    }

    @Override
    public int getItemCount() {
        return listaProdutosCarrinho.size();
    }

    static class CarrinhoViewHolder extends RecyclerView.ViewHolder{
        private ProdutoCarrinhoBinding binding;

        public CarrinhoViewHolder(ProdutoCarrinhoBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }

    public interface OnCarrinhoChangeListener {
        void onCarrinhoChanged();
    }

}
