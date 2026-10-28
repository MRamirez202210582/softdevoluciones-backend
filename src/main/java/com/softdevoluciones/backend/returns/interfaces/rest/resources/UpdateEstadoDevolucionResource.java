
package com.softdevoluciones.backend.returns.interfaces.rest.resources;

import com.softdevoluciones.backend.returns.domain.model.valueobjects.EstadoDevolucion;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record UpdateEstadoDevolucionResource(
        @NotNull EstadoDevolucion nuevoEstado,
        @Size(max = 1000) String observacionOperador
) {
}
