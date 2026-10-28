
package com.softdevoluciones.backend.shopping.application.internal.queryservices;

import com.softdevoluciones.backend.shopping.domain.model.aggregates.Carrito;
import com.softdevoluciones.backend.shopping.domain.model.queries.GetCarritoByUsuarioIdQuery;
import com.softdevoluciones.backend.shopping.domain.services.CarritoQueryService;
import com.softdevoluciones.backend.shopping.infrastructure.persistence.jpa.repositories.CarritoRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CarritoQueryServiceImpl implements CarritoQueryService {

    private final CarritoRepository carritoRepository;

    @Override
    public Optional<Carrito> handle(GetCarritoByUsuarioIdQuery query) {

        return carritoRepository.findByUsuarioId(query.usuarioId());
    }
}
