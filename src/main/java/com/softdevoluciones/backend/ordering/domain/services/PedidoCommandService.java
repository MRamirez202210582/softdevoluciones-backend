
package com.softdevoluciones.backend.ordering.domain.services;

import com.softdevoluciones.backend.ordering.domain.model.aggregates.Pedido;
import com.softdevoluciones.backend.ordering.domain.model.commands.CreatePedidoCommand;
import com.softdevoluciones.backend.ordering.domain.model.commands.UpdatePedidoEstadoCommand;

public interface PedidoCommandService {

    Pedido handle(CreatePedidoCommand command);

    Pedido handle(UpdatePedidoEstadoCommand command);
}
