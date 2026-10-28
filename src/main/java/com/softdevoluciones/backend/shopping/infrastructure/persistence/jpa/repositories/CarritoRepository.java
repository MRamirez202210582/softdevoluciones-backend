
package com.softdevoluciones.backend.shopping.infrastructure.persistence.jpa.repositories;

import com.softdevoluciones.backend.shopping.domain.model.aggregates.Carrito;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CarritoRepository extends JpaRepository<Carrito, Long> {

    @EntityGraph(attributePaths = {
            "detalles",
            "detalles.producto"
    })
    Optional<Carrito> findByUsuarioId(Long usuarioId);
}
