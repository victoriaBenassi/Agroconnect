package com.constate.api_agroconnect.repository;

import com.constate.api_agroconnect.model.Pedido;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PedidoRepository extends JpaRepository<Pedido, Integer> {

    @Query("SELECT p FROM Pedido p WHERE p.usuario.id_usuario = :usuarioId")
    List<Pedido> findByUsuarioId(Integer usuarioId);
}
