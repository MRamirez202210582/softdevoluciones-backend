
package com.softdevoluciones.backend.returns.application.internal.queryservices;

import com.softdevoluciones.backend.returns.domain.model.aggregates.SolicitudDevolucion;
import com.softdevoluciones.backend.returns.domain.model.queries.GetAllDevolucionesQuery;
import com.softdevoluciones.backend.returns.domain.model.queries.GetDevolucionByIdQuery;
import com.softdevoluciones.backend.returns.domain.model.queries.GetDevolucionesByUsuarioQuery;
import com.softdevoluciones.backend.returns.domain.services.DevolucionQueryService;
import com.softdevoluciones.backend.returns.infrastructure.persistence.jpa.repositories.SolicitudDevolucionRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class DevolucionQueryServiceImpl
        implements DevolucionQueryService {

    private final SolicitudDevolucionRepository repository;

    public DevolucionQueryServiceImpl(
            SolicitudDevolucionRepository repository
    ) {
        this.repository = repository;
    }

    @Override
    public Optional<SolicitudDevolucion> handle(
            GetDevolucionByIdQuery query
    ) {
        return repository.findById(query.devolucionId());
    }

    @Override
    public List<SolicitudDevolucion> handle(
            GetDevolucionesByUsuarioQuery query
    ) {
        return repository.findByUsuarioId(query.usuarioId());
    }

    @Override
    public Page<SolicitudDevolucion> handle(
            GetAllDevolucionesQuery query
    ) {
        if (query.pagina() < 0 || query.tamanio() <= 0
                || query.tamanio() > 100) {
            throw new IllegalArgumentException(
                    "Los parámetros de paginación no son válidos."
            );
        }

        if (query.fechaDesde() != null
                && query.fechaHasta() != null
                && query.fechaDesde().isAfter(query.fechaHasta())) {
            throw new IllegalArgumentException(
                    "La fecha inicial no puede superar la fecha final."
            );
        }

        Specification<SolicitudDevolucion> specification =
                (root, criteriaQuery, cb) -> cb.conjunction();

        if (query.estado() != null) {
            specification = specification.and(
                    (root, criteriaQuery, cb) ->
                            cb.equal(root.get("estado"), query.estado())
            );
        }

        if (query.motivo() != null) {
            specification = specification.and(
                    (root, criteriaQuery, cb) ->
                            cb.equal(root.get("motivo"), query.motivo())
            );
        }

        if (query.fechaDesde() != null) {
            specification = specification.and(
                    (root, criteriaQuery, cb) ->
                            cb.greaterThanOrEqualTo(
                                    root.get("fechaSolicitud"),
                                    query.fechaDesde().atStartOfDay()
                            )
            );
        }

        if (query.fechaHasta() != null) {
            specification = specification.and(
                    (root, criteriaQuery, cb) ->
                            cb.lessThan(
                                    root.get("fechaSolicitud"),
                                    query.fechaHasta().plusDays(1).atStartOfDay()
                            )
            );
        }

        return repository.findAll(
                specification,
                PageRequest.of(query.pagina(), query.tamanio())
        );
    }
}
