
package com.softdevoluciones.backend.returns.interfaces.rest.transform;

import com.softdevoluciones.backend.returns.domain.model.commands.UpdateDevolucionEstadoCommand;
import com.softdevoluciones.backend.returns.interfaces.rest.resources.UpdateEstadoDevolucionResource;

public class UpdateEstadoDevolucionCommandFromResourceAssembler {

    public static UpdateDevolucionEstadoCommand toCommand(
            Long devolucionId,
            UpdateEstadoDevolucionResource resource
    ) {
        return new UpdateDevolucionEstadoCommand(
                devolucionId,
                resource.nuevoEstado(),
                resource.observacionOperador()
        );
    }
}
