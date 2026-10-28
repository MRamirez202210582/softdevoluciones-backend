
package com.softdevoluciones.backend.ordering.domain.services;

import com.softdevoluciones.backend.ordering.domain.model.aggregates.Pedido;
import com.softdevoluciones.backend.ordering.domain.model.queries.*;

import java.util.List;
import java.util.Optional;

public interface PedidoQueryService {

    List<Pedido> handle(GetAllPedidosQuery query);

    Optional<Pedido> handle(GetPedidoByIdQuery query);

    List<Pedido> handle(GetPedidosByUsuarioQuery query);
}
