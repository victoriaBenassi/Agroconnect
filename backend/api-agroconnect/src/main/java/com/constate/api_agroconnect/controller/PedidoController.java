package com.constate.api_agroconnect.controller;

import com.constate.api_agroconnect.model.Endereco;
import com.constate.api_agroconnect.model.Pedido;
import com.constate.api_agroconnect.model.Usuario;
import com.constate.api_agroconnect.service.PedidoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/pedido")
public class PedidoController {

    @Autowired
    private PedidoService pedidoService;

    @PostMapping("/criar")
    public ResponseEntity<Pedido> criarPedido(@RequestBody Pedido pedido){
        Usuario usuario = (Usuario) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        Pedido novoPedido = pedidoService.criarPedido(usuario.getId_usuario(), pedido);
        return ResponseEntity.ok(novoPedido);
    }

    @GetMapping("/visualizar")
    public ResponseEntity<List<Pedido>> visualizarPedido(){
        Usuario usuario = (Usuario) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        List <Pedido> pedido = pedidoService.visualizarPedido(usuario.getId_usuario());

        if (pedido == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(pedido);
    }

    @DeleteMapping("/excluir/{pedidoId}")
    public ResponseEntity<String> excluir(@PathVariable Integer pedidoId){
        try {
            Usuario usuario = (Usuario) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            pedidoService.excluirPedido(usuario.getId_usuario(), pedidoId);
            return ResponseEntity.ok("Pedido excluído com sucesso.");
        } catch (RuntimeException e) {
            return ResponseEntity.status(400).body(e.getMessage());
        }
    }
}
