
package com.softdevoluciones.backend.shopping.infrastructure.persistence.jpa.repositories;

import com.softdevoluciones.backend.shopping.domain.model.aggregates.DetalleCarrito;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface DetalleCarritoRepository
        extends JpaRepository<DetalleCarrito, Long> {

    Optional<DetalleCarrito> findByCarritoIdAndProductoId(
            Long carritoId,
            Long productoId
    );
}
