package com.softdevoluciones.backend.identity.application.internal.queryservices;

import com.softdevoluciones.backend.identity.domain.model.aggregate.Usuario;
import com.softdevoluciones.backend.identity.domain.model.queries.GetUsuarioByEmailQuery;
import com.softdevoluciones.backend.identity.domain.model.queries.GetUsuarioByIdQuery;
import com.softdevoluciones.backend.identity.domain.services.UsuarioQueryService;
import com.softdevoluciones.backend.identity.infrastructure.persistence.jpa.repositories.UsuarioRepository;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UsuarioQueryServiceImpl implements UsuarioQueryService {

    private final UsuarioRepository usuarioRepository;

    @Override
    public Optional<Usuario> handle(GetUsuarioByEmailQuery query) {
        return usuarioRepository.findByEmail(query.email());
    }

    @Override
    public Optional<Usuario> handle(GetUsuarioByIdQuery query) {
        return usuarioRepository.findById(query.id());
    }
}