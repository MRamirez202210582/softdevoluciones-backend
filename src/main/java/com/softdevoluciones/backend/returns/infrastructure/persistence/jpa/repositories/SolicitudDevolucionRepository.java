
package com.softdevoluciones.backend.returns.infrastructure.persistence.jpa.repositories;

import com.softdevoluciones.backend.returns.domain.model.aggregates.SolicitudDevolucion;
import com.softdevoluciones.backend.returns.domain.model.valueobjects.EstadoDevolucion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;

public interface SolicitudDevolucionRepository
        extends JpaRepository<SolicitudDevolucion, Long>,
        JpaSpecificationExecutor<SolicitudDevolucion> {

    List<SolicitudDevolucion> findByUsuarioId(Long usuarioId);

    List<SolicitudDevolucion> findByEstado(
            EstadoDevolucion estado
    );

    List<SolicitudDevolucion> findByCompraId(Long compraId);


    @Query("""
    SELECT COALESCE(SUM(d.cantidad), 0)
    FROM DetalleDevolucion d
    JOIN d.solicitud s
    WHERE s.compraId = :compraId
      AND d.detalleCompraId = :detalleCompraId
      AND s.estado IN :estados
    """)
    Long sumarCantidadDevuelta(
            @Param("compraId") Long compraId,
            @Param("detalleCompraId") Long detalleCompraId,
            @Param("estados")
            java.util.Collection<EstadoDevolucion> estados
    );

}
