
package com.softdevoluciones.backend.returns.domain.model.commands;

import com.softdevoluciones.backend.returns.domain.model.valueobjects.EstadoDevolucion;

public record UpdateDevolucionEstadoCommand(
        Long devolucionId,
        EstadoDevolucion nuevoEstado,
        String observacionOperador
) {
}
