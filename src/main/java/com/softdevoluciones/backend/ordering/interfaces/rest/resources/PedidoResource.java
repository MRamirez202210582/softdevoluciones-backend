
package com.softdevoluciones.backend.ordering.interfaces.rest.resources;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public record PedidoResource(
        Long id,
        Long usuarioId,
        String clienteNombre,
        String celular,
        String direccion,
        LocalDateTime fecha,
        String estado,
        BigDecimal total,
        List<DetallePedidoResource> detalles
) {}
