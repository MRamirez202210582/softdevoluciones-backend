
package com.softdevoluciones.backend.returns.interfaces.rest.transform;

import com.softdevoluciones.backend.returns.domain.model.commands.CreateDevolucionCommand;
import com.softdevoluciones.backend.returns.interfaces.rest.resources.CreateDevolucionResource;

public class CreateDevolucionCommandFromResourceAssembler {

    public static CreateDevolucionCommand toCommand(
            CreateDevolucionResource resource
    ) {
        var detalles = resource.detalles().stream()
                .map(item ->
                        new CreateDevolucionCommand.DetalleCommand(
                                item.detalleCompraId(),
                                item.cantidad()
                        )
                )
                .toList();

        return new CreateDevolucionCommand(
                resource.compraId(),
                resource.motivo(),
                resource.comentario(),
                detalles
        );
    }
}
