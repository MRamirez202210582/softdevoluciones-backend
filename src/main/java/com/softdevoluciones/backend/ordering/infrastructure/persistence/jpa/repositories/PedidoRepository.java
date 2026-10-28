
package com.softdevoluciones.backend.ordering.infrastructure.persistence.jpa.repositories;

import com.softdevoluciones.backend.ordering.domain.model.aggregates.Pedido;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PedidoRepository extends JpaRepository<Pedido, Long> {

    @Override
    @EntityGraph(attributePaths = {"detalles", "detalles.producto"})
    List<Pedido> findAll();

    @Override
    @EntityGraph(attributePaths = {"detalles", "detalles.producto"})
    Optional<Pedido> findById(Long id);

    @EntityGraph(attributePaths = {"detalles", "detalles.producto"})
    List<Pedido> findByUsuarioIdOrderByFechaDesc(Long usuarioId);
}
