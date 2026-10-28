package com.softdevoluciones.backend.ordering.interfaces.rest.transform;

import com.softdevoluciones.backend.ordering.domain.model.aggregates.Pedido;
import com.softdevoluciones.backend.ordering.interfaces.rest.resources.*;

public class PedidoResourceFromEntityAssembler {

    public static PedidoResource toResourceFromEntity(Pedido pedido) {

        var detalles = pedido.getDetalles()
                .stream()
                .map(detalle -> new DetallePedidoResource(
                        detalle.getId(),
                        detalle.getProducto().getId(),
                        detalle.getProducto().getNombre(),
                        detalle.getCantidad(),
                        detalle.getPrecioUnitario(),
                        detalle.getSubtotal()
                ))
                .toList();

        return new PedidoResource(
                pedido.getId(),
                pedido.getUsuario().getId(),
                pedido.getClienteNombre(),
                pedido.getCelular(),
                pedido.getDireccion(),
                pedido.getFecha(),
                pedido.getEstado().name(),
                pedido.getTotal(),
                detalles
        );
    }
}