package com.constate.api_agroconnect.controller;

import com.constate.api_agroconnect.dto.CadastrarDto;
import com.constate.api_agroconnect.dto.LoginDto;
import com.constate.api_agroconnect.dto.ResponseDTO;
import com.constate.api_agroconnect.model.Usuario;
import com.constate.api_agroconnect.repository.UsuarioRepository;
import com.constate.api_agroconnect.securit.TokenService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private TokenService tokenService;

    @PostMapping("/login")
    public ResponseEntity login(@RequestBody LoginDto body ){
        Usuario usuario = this.usuarioRepository.findByEmail(body.email()).orElseThrow(() -> new RuntimeException("Usuario não encontrado"));
        if (passwordEncoder.matches( body.senha(), usuario.getSenha())){
            String token = this.tokenService.gerarToken(usuario);

            return ResponseEntity.ok(new ResponseDTO(usuario.getId_usuario(), token));
        }
        return ResponseEntity.badRequest().build();
    }

    @PostMapping("/cadastrar")
    public ResponseEntity cadastrar(@RequestBody CadastrarDto body){
        Optional<Usuario> usuario = this.usuarioRepository.findByEmail(body.email());

        if (usuario.isPresent()) {
            return ResponseEntity.badRequest().body("Este e-mail já está em uso.");
        }

        Usuario novoUsuario = new Usuario();
        novoUsuario.setSenha(passwordEncoder.encode(body.senha()));
        novoUsuario.setEmail(body.email());
        novoUsuario.setNome(body.nome());
        novoUsuario.setSobrenome(body.sobrenome());
        this.usuarioRepository.save(novoUsuario);

        String token = this.tokenService.gerarToken(novoUsuario);
        return ResponseEntity.ok(new ResponseDTO(novoUsuario.getId_usuario(), token));
    }
}
