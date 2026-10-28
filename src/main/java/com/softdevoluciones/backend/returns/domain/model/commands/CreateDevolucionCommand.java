
package com.softdevoluciones.backend.returns.domain.model.commands;

import com.softdevoluciones.backend.returns.domain.model.valueobjects.MotivoDevolucion;

import java.util.List;

public record CreateDevolucionCommand(
        Long compraId,
        MotivoDevolucion motivo,
        String comentario,
        List<DetalleCommand> detalles
) {
    public record DetalleCommand(
            Long detalleCompraId,
            Integer cantidad
    ) {
    }
}
