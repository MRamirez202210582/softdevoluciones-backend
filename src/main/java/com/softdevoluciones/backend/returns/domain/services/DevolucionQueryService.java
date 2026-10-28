
package com.softdevoluciones.backend.returns.domain.services;

import com.softdevoluciones.backend.returns.domain.model.aggregates.SolicitudDevolucion;
import com.softdevoluciones.backend.returns.domain.model.queries.GetAllDevolucionesQuery;
import com.softdevoluciones.backend.returns.domain.model.queries.GetDevolucionByIdQuery;
import com.softdevoluciones.backend.returns.domain.model.queries.GetDevolucionesByUsuarioQuery;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Optional;

public interface DevolucionQueryService {

    Optional<SolicitudDevolucion> handle(
            GetDevolucionByIdQuery query
    );

    List<SolicitudDevolucion> handle(
            GetDevolucionesByUsuarioQuery query
    );

    Page<SolicitudDevolucion> handle(
            GetAllDevolucionesQuery query
    );
}
