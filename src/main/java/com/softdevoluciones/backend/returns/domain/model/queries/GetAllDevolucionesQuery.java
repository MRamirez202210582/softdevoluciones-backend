
package com.softdevoluciones.backend.returns.domain.model.queries;

import com.softdevoluciones.backend.returns.domain.model.valueobjects.EstadoDevolucion;
import com.softdevoluciones.backend.returns.domain.model.valueobjects.MotivoDevolucion;

import java.time.LocalDate;

public record GetAllDevolucionesQuery(
        EstadoDevolucion estado,
        MotivoDevolucion motivo,
        LocalDate fechaDesde,
        LocalDate fechaHasta,
        int pagina,
        int tamanio
) {
}
