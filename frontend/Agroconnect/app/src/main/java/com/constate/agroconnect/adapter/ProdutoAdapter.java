package com.constate.agroconnect.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.constate.agroconnect.databinding.ProdutoItemBinding;
import com.constate.agroconnect.model.Carrinho;
import com.constate.agroconnect.model.Produto;

import java.util.ArrayList;

public class ProdutoAdapter extends RecyclerView.Adapter<ProdutoAdapter.ProdutoViewHolder> {

    private final Context context;
    private final ArrayList<Produto> listaProdutos;

    public ProdutoAdapter(ArrayList<Produto> listaProdutos, Context context) {
        this.listaProdutos = listaProdutos;
        this.context = context;
    }

    @NonNull
    @Override
    public ProdutoViewHolder onCreateViewHolder(@NonNull ViewGroup parente, int viewType){
        ProdutoItemBinding listaItem;
        listaItem = ProdutoItemBinding.inflate(LayoutInflater.from(context), parente, false);
        return new ProdutoViewHolder(listaItem);
    }

    @Override
    public int getItemCount() {
        return listaProdutos.size();
    }

    @Override
    public void onBindViewHolder(@NonNull ProdutoAdapter.ProdutoViewHolder holder, int position) {
        Produto produto = listaProdutos.get(position);
        holder.binding.imgViewProduto.setImageResource(listaProdutos.get(position).getImg_produto());
        holder.binding.txtNomeProduto.setText(listaProdutos.get(position).getNome());
        holder.binding.txtPreco.setText(String.format("R$ %.2f",listaProdutos.get(position).getValor()));

        holder.binding.buttonAdicionarCarrinho.setOnClickListener(v -> {
            Carrinho.getInstance().adicionarProduto(produto);
            Toast.makeText(context, produto.getNome()+ " adicionado ao carrinho", Toast.LENGTH_SHORT).show();
        });
    }

    static class ProdutoViewHolder extends RecyclerView.ViewHolder{
        private ProdutoItemBinding binding;

        public ProdutoViewHolder(ProdutoItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
