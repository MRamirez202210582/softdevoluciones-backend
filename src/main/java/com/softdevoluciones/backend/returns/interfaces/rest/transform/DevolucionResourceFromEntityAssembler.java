
package com.softdevoluciones.backend.returns.interfaces.rest.transform;

import com.softdevoluciones.backend.returns.domain.model.aggregates.SolicitudDevolucion;
import com.softdevoluciones.backend.returns.interfaces.rest.resources.DetalleDevolucionResource;
import com.softdevoluciones.backend.returns.interfaces.rest.resources.DevolucionResource;

public class DevolucionResourceFromEntityAssembler {

    public static DevolucionResource toResource(
            SolicitudDevolucion entity
    ) {
        var detalles = entity.getDetalles().stream()
                .map(detalle -> new DetalleDevolucionResource(
                        detalle.getId(),
                        detalle.getDetalleCompraId(),
                        detalle.getCantidad(),
                        detalle.getPrecioUnitario(),
                        detalle.getImporte()
                ))
                .toList();

        return new DevolucionResource(
                entity.getId(),
                entity.getCompraId(),
                entity.getUsuarioId(),
                entity.getFechaSolicitud(),
                entity.getEstado(),
                entity.getMotivo(),
                entity.getComentario(),
                entity.getObservacionOperador(),
                entity.getImporte(),
                detalles
        );
    }
}
