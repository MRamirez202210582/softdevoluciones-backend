
package com.softdevoluciones.backend.ordering.application.internal.queryservices;

import com.softdevoluciones.backend.ordering.infrastructure.persistence.jpa.repositories.DetallePedidoRepository;
import com.softdevoluciones.backend.ordering.infrastructure.persistence.jpa.repositories.PedidoRepository;
import com.softdevoluciones.backend.returns.domain.services.CompraConsultaService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CompraConsultaServiceImpl implements CompraConsultaService {

    private final PedidoRepository pedidoRepository;
    private final DetallePedidoRepository detallePedidoRepository;

    @Override
    public boolean perteneceAlUsuario(
            Long compraId,
            Long usuarioId
    ) {
        if (compraId == null || usuarioId == null) {
            return false;
        }

        return pedidoRepository.findById(compraId)
                .filter(pedido ->
                        pedido.getUsuario() != null
                                && usuarioId.equals(pedido.getUsuario().getId())
                                && pedido.getEstado() ==
                                com.softdevoluciones.backend.ordering.domain.model.valueobjects.EstadoPedido.ENTREGADO
                )
                .isPresent();
    }

    @Override
    public DetalleCompraInfo obtenerDetalle(
            Long compraId,
            Long detalleCompraId
    ) {
        if (compraId == null || detalleCompraId == null) {
            return null;
        }

        return detallePedidoRepository.findById(detalleCompraId)
                .filter(detalle ->
                        detalle.getPedido() != null
                                && compraId.equals(detalle.getPedido().getId())
                )
                .map(detalle -> new DetalleCompraInfo(
                        detalle.getId(),
                        detalle.getCantidad(),
                        detalle.getPrecioUnitario()
                ))
                .orElse(null);
    }
}
