
package com.softdevoluciones.backend.returns.interfaces.rest.resources;

import java.math.BigDecimal;

public record DetalleDevolucionResource(
        Long id,
        Long detalleCompraId,
        Integer cantidad,
        BigDecimal precioUnitario,
        BigDecimal importe
) {
}
