package com.softdevoluciones.backend.identity.application.internal.commandservices;

import com.softdevoluciones.backend.identity.domain.model.aggregate.Usuario;
import com.softdevoluciones.backend.identity.domain.model.commands.RegisterCommand;
import com.softdevoluciones.backend.identity.domain.model.valueobjects.Rol;
import com.softdevoluciones.backend.identity.domain.services.UsuarioCommandService;
import com.softdevoluciones.backend.identity.infrastructure.persistence.jpa.repositories.UsuarioRepository;

import lombok.RequiredArgsConstructor;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UsuarioCommandServiceImpl implements UsuarioCommandService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public Usuario handle(RegisterCommand command) {

        if (command.nombre() == null || command.nombre().isBlank()
                || command.email() == null || command.email().isBlank()
                || command.password() == null || command.password().isBlank()) {
            throw new IllegalArgumentException("Todos los campos son obligatorios");
        }

        String email = command.email().trim().toLowerCase();

        if (usuarioRepository.existsByEmail(email)) {
            throw new IllegalArgumentException("El correo ya está registrado");
        }

        Usuario usuario = Usuario.builder()
                .nombre(command.nombre().trim())
                .email(email)
                .password(passwordEncoder.encode(command.password()))
                .rol(Rol.CLIENTE)
                .build();

        return usuarioRepository.save(usuario);
    }
}