package com.softdevoluciones.backend.ordering.interfaces.rest.resources;

import java.math.BigDecimal;

public record DetallePedidoResource(
        Long detalleCompraId,
        Long productoId,
        String nombreProducto,
        Integer cantidad,
        BigDecimal precioUnitario,
        BigDecimal subtotal
) {}