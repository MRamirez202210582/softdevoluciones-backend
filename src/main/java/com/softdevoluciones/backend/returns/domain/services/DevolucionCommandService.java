
package com.softdevoluciones.backend.returns.domain.services;

import com.softdevoluciones.backend.returns.domain.model.aggregates.SolicitudDevolucion;
import com.softdevoluciones.backend.returns.domain.model.commands.CreateDevolucionCommand;
import com.softdevoluciones.backend.returns.domain.model.commands.UpdateDevolucionEstadoCommand;

public interface DevolucionCommandService {

    SolicitudDevolucion crearDevolucion(
            CreateDevolucionCommand command,
            Long usuarioId
    );

    SolicitudDevolucion actualizarEstado(
            UpdateDevolucionEstadoCommand command
    );
}
