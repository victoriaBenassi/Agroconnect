package com.constate.api_agroconnect.service;

import com.constate.api_agroconnect.model.Endereco;
import com.constate.api_agroconnect.model.Pedido;
import com.constate.api_agroconnect.model.Usuario;
import com.constate.api_agroconnect.repository.PedidoRepository;
import com.constate.api_agroconnect.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class PedidoService {

    @Autowired
    private PedidoRepository pedidoRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    public Pedido criarPedido(Integer usuarioId, Pedido pedidoNovo){
        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        pedidoNovo.setUsuario(usuario);
        pedidoNovo.setDataPedido(LocalDate.now());
        return pedidoRepository.save(pedidoNovo);
    }

    public List<Pedido> visualizarPedido(Integer usuarioId){
        List<Pedido> pedidos = pedidoRepository.findByUsuarioId(usuarioId);

        if (pedidos == null) {
            throw new RuntimeException("Pedidos não encontrados para o usuário");
        }

        return pedidos;
    }

    public void excluirPedido(Integer usuarioId, Integer pedidoId){
        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        Pedido pedido = pedidoRepository.findById(pedidoId)
                .orElseThrow(() -> new RuntimeException("Pedido não encontrado"));

        pedido.setUsuario(null);
        pedidoRepository.delete(pedido);
    }
}
