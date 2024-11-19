package com.constate.agroconnect.adapter;

import static android.app.PendingIntent.getActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.constate.agroconnect.databinding.ItemEnderecoBinding;
import com.constate.agroconnect.model.Endereco;
import com.constate.agroconnect.service.EnderecoApiService;
import com.constate.agroconnect.ui.EnderecoFragment;

import java.util.ArrayList;

public class EnderecoAdapter extends RecyclerView.Adapter<EnderecoAdapter.EnderecoViewHolder> {
    private final ArrayList<Endereco> listaEndereco;
    private final Context context;
    private EnderecoFragment enderecoFragment;

    public EnderecoAdapter(ArrayList<Endereco> listaEndereco, Context context, EnderecoFragment enderecoFragment) {
        this.listaEndereco = listaEndereco;
        this.context = context;
        this.enderecoFragment = enderecoFragment;
    }

    @NonNull
    @Override
    public EnderecoAdapter.EnderecoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemEnderecoBinding itemEndereco;
        itemEndereco = ItemEnderecoBinding.inflate(LayoutInflater.from(context), parent, false);
        return new EnderecoViewHolder(itemEndereco);
    }

    @Override
    public void onBindViewHolder(@NonNull EnderecoViewHolder holder, int position) {
        Endereco endereco = listaEndereco.get(position);
        holder.binding.txtLogradouro.setText(listaEndereco.get(position).getLogradouro());
        holder.binding.txtNumero.setText(listaEndereco.get(position).getNumero());
        holder.binding.txtBairro.setText(listaEndereco.get(position).getBairro());
        holder.binding.txtLocalidade.setText(listaEndereco.get(position).getLocalidade());
        holder.binding.txtUf.setText(listaEndereco.get(position).getUf());
        holder.binding.txtCep.setText(listaEndereco.get(position).getCep());
        holder.binding.txtComplemento.setText(listaEndereco.get(position).getComplemento());

        holder.binding.buttonExcluirEndereco.setOnClickListener(v -> {
            enderecoFragment.excluirEndereco(endereco.getId_endereco());
        });
    }

    @Override
    public int getItemCount() {
        return listaEndereco.size();
    }

    static class EnderecoViewHolder extends RecyclerView.ViewHolder{
        private ItemEnderecoBinding binding;

        public EnderecoViewHolder(ItemEnderecoBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
