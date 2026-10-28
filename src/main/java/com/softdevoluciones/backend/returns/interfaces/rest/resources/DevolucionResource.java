
package com.softdevoluciones.backend.returns.interfaces.rest.resources;

import com.softdevoluciones.backend.returns.domain.model.valueobjects.EstadoDevolucion;
import com.softdevoluciones.backend.returns.domain.model.valueobjects.MotivoDevolucion;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public record DevolucionResource(
        Long id,
        Long compraId,
        Long usuarioId,
        LocalDateTime fechaSolicitud,
        EstadoDevolucion estado,
        MotivoDevolucion motivo,
        String comentario,
        String observacionOperador,
        BigDecimal importe,
        List<DetalleDevolucionResource> detalles
) {
}
