package com.constate.api_agroconnect.controller;

import com.constate.api_agroconnect.model.Endereco;
import com.constate.api_agroconnect.model.Usuario;
import com.constate.api_agroconnect.service.EnderecoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/enderecos")
public class EnderecoController {
    @Autowired
    private EnderecoService enderecoService;

    @PostMapping("/adicionar")
    public ResponseEntity<Endereco> cadastrarEndereco(@RequestBody Endereco endereco){

        Usuario usuario = (Usuario) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Endereco novoEndereco = enderecoService.cadastrarEndereco(usuario.getId_usuario(), endereco);

        return ResponseEntity.ok(novoEndereco);
    }

    @GetMapping("/visualizar")
    public ResponseEntity<List<Endereco>> visualizarEndereco(){
        Usuario usuario = (Usuario) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        List <Endereco> endereco = enderecoService.visualizarEndereco(usuario.getId_usuario());

        if (endereco == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(endereco);
    }

    @DeleteMapping("/excluir/{enderecoId}")
    public ResponseEntity<String> excluirEndereco(@PathVariable Integer enderecoId) {
        try {
            Usuario usuario = (Usuario) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            enderecoService.excluirEndereco(usuario.getId_usuario(), enderecoId);
            return ResponseEntity.ok("Endereço excluído com sucesso.");
        } catch (RuntimeException e) {
            return ResponseEntity.status(400).body(e.getMessage());
        }
    }
}
