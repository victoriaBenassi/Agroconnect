package com.constate.api_agroconnect.service;

import com.constate.api_agroconnect.dto.CadastrarDto;
import com.constate.api_agroconnect.dto.ResponseDTO;
import com.constate.api_agroconnect.model.Usuario;
import com.constate.api_agroconnect.repository.UsuarioRepository;
import com.constate.api_agroconnect.securit.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UsuarioService {
    @Autowired
    UsuarioRepository usuarioRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private TokenService tokenService;

    public Usuario usuarioPorId(Integer id){
        return usuarioRepository.findById(id)
                .orElseThrow(()-> new RuntimeException("Usuario não encontrado"));
    }

    public ResponseDTO atualizarUsuario(Usuario usuarioExistente, Usuario usuarioAtualizado){
        if (usuarioAtualizado.getNome() != null) {
            usuarioExistente.setNome(usuarioAtualizado.getNome());
        }
        if (usuarioAtualizado.getSobrenome() != null) {
            usuarioExistente.setSobrenome(usuarioAtualizado.getSobrenome());
        }
        if (usuarioAtualizado.getEmail() != null) {
            usuarioExistente.setEmail(usuarioAtualizado.getEmail());
        }

        // Salvar usuário atualizado
        usuarioRepository.save(usuarioExistente);

        // Gerar novo token
        String novoToken = tokenService.gerarToken(usuarioExistente);

        // Retornar usuário atualizado com o novo token
        return new ResponseDTO(usuarioExistente.getId_usuario(), novoToken);
    }

    public void excluirUsuario(Integer id) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));
        usuarioRepository.delete(usuario);
    }
}
