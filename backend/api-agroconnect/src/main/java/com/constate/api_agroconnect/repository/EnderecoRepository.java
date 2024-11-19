package com.constate.api_agroconnect.repository;

import com.constate.api_agroconnect.model.Endereco;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface EnderecoRepository extends JpaRepository<Endereco, Integer> {

    @Query("SELECT e FROM Endereco e WHERE e.usuario.id_usuario = :usuarioId")
    List<Endereco> findByUsuarioId(Integer usuarioId);
}
