package com.softdevoluciones.backend.identity.infrastructure.persistence.jpa.repositories;

import com.softdevoluciones.backend.identity.domain.model.aggregate.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    Optional<Usuario> findByEmail(String email);

    boolean existsByEmail(String email);
}