package com.constate.api_agroconnect.controller;

import com.constate.api_agroconnect.dto.ResponseDTO;
import com.constate.api_agroconnect.model.Usuario;
import com.constate.api_agroconnect.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/usuario")
public class UsuarioController {

    @Autowired
    UsuarioService usuarioService;

    @GetMapping("/info")
    public ResponseEntity<Usuario> getUsuario(){
        Usuario usuario = (Usuario) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Usuario usuarioInfo = usuarioService.usuarioPorId(usuario.getId_usuario());
        return ResponseEntity.ok(usuarioInfo);
    }

    @PatchMapping("/atualizar")
    public ResponseEntity<ResponseDTO> atualizarUsuario(@RequestBody Usuario usuarioAtualizado){
        Usuario usuarioAutenticado = (Usuario) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Usuario usuarioExistente = usuarioService.usuarioPorId(usuarioAutenticado.getId_usuario());

        ResponseDTO response = usuarioService.atualizarUsuario(usuarioExistente, usuarioAtualizado);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/excluir")
    public ResponseEntity<String> excluirUsuario(){
        Usuario usuario = (Usuario) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        usuarioService.excluirUsuario(usuario.getId_usuario());
        return ResponseEntity.ok("Usuario deletado com sucesso.");
    }
}
