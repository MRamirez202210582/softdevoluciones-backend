
package com.softdevoluciones.backend.ordering.infrastructure.persistence.jpa.repositories;

import com.softdevoluciones.backend.ordering.domain.model.aggregates.DetallePedido;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DetallePedidoRepository
        extends JpaRepository<DetallePedido, Long> {
}
