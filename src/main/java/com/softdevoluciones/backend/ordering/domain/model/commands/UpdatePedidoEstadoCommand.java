
package com.softdevoluciones.backend.ordering.domain.model.commands;

import com.softdevoluciones.backend.ordering.domain.model.valueobjects.EstadoPedido;

public record UpdatePedidoEstadoCommand(
        Long pedidoId,
        EstadoPedido estado
) {}
