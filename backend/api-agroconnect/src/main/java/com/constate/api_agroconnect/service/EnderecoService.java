package com.constate.api_agroconnect.service;

import com.constate.api_agroconnect.model.Endereco;
import com.constate.api_agroconnect.model.Usuario;
import com.constate.api_agroconnect.repository.EnderecoRepository;
import com.constate.api_agroconnect.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EnderecoService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private EnderecoRepository enderecoRepository;

    public Endereco cadastrarEndereco(Integer usuarioId, Endereco endereco){
        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        endereco.setUsuario(usuario);
        return enderecoRepository.save(endereco);
    }

    public List<Endereco> visualizarEndereco(Integer usuarioId){
        List<Endereco> enderecos = enderecoRepository.findByUsuarioId(usuarioId);

        if (enderecos == null) {
            throw new RuntimeException("Endereços não encontrados para o usuário");
        }

        return enderecos;
    }

    public void excluirEndereco(Integer usuarioId, Integer enderecoId){
        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        Endereco endereco = enderecoRepository.findById(enderecoId)
                .orElseThrow(() -> new RuntimeException("Endereço não encontrado"));

        endereco.setUsuario(null);
        enderecoRepository.delete(endereco);
    }
}
