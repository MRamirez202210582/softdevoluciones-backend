
package com.softdevoluciones.backend.returns.interfaces.rest.resources;

import com.softdevoluciones.backend.returns.domain.model.valueobjects.MotivoDevolucion;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import java.util.List;

public record CreateDevolucionResource(
        @NotNull Long compraId,
        @NotNull MotivoDevolucion motivo,
        @Size(max = 1000) String comentario,
        @NotEmpty List<@Valid DetalleResource> detalles
) {
    public record DetalleResource(
            @NotNull Long detalleCompraId,
            @NotNull @Positive Integer cantidad
    ) {
    }
}
