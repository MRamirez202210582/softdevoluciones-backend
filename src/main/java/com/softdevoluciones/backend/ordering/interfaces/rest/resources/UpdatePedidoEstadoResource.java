
package com.softdevoluciones.backend.ordering.interfaces.rest.resources;

import com.softdevoluciones.backend.ordering.domain.model.valueobjects.EstadoPedido;
import jakarta.validation.constraints.NotNull;

public record UpdatePedidoEstadoResource(

        @NotNull(message = "El estado es obligatorio")
        EstadoPedido estado

) {}
