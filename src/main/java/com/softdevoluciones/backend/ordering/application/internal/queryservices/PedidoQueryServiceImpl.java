
package com.softdevoluciones.backend.ordering.application.internal.queryservices;

import com.softdevoluciones.backend.ordering.domain.model.aggregates.Pedido;
import com.softdevoluciones.backend.ordering.domain.model.queries.*;
import com.softdevoluciones.backend.ordering.domain.services.PedidoQueryService;
import com.softdevoluciones.backend.ordering.infrastructure.persistence.jpa.repositories.PedidoRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PedidoQueryServiceImpl implements PedidoQueryService {

    private final PedidoRepository pedidoRepository;

    @Override
    public List<Pedido> handle(GetAllPedidosQuery query) {
        return pedidoRepository.findAll();
    }

    @Override
    public Optional<Pedido> handle(GetPedidoByIdQuery query) {
        return pedidoRepository.findById(query.id());
    }

    @Override
    public List<Pedido> handle(GetPedidosByUsuarioQuery query) {
        return pedidoRepository.findByUsuarioIdOrderByFechaDesc(
                query.usuarioId()
        );
    }
}
