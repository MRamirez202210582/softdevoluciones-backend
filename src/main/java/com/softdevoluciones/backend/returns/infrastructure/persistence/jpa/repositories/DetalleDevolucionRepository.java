
package com.softdevoluciones.backend.returns.infrastructure.persistence.jpa.repositories;

import com.softdevoluciones.backend.returns.domain.model.aggregates.DetalleDevolucion;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DetalleDevolucionRepository
        extends JpaRepository<DetalleDevolucion, Long> {
}
