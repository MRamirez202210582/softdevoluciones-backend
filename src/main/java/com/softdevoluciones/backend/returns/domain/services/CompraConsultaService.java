
package com.softdevoluciones.backend.returns.domain.services;

import java.math.BigDecimal;

public interface CompraConsultaService {

    boolean perteneceAlUsuario(
            Long compraId,
            Long usuarioId
    );

    DetalleCompraInfo obtenerDetalle(
            Long compraId,
            Long detalleCompraId
    );

    record DetalleCompraInfo(
            Long detalleCompraId,
            Integer cantidadComprada,
            BigDecimal precioUnitario
    ) {
    }
}
