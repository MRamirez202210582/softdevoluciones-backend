package com.softdevoluciones.backend.identity.interfaces.rest;

import com.softdevoluciones.backend.identity.domain.model.aggregate.Usuario;
import com.softdevoluciones.backend.identity.infrastructure.persistence.jpa.repositories.UsuarioRepository;
import com.softdevoluciones.backend.identity.interfaces.rest.resources.ActualizarRolResource;
import com.softdevoluciones.backend.identity.interfaces.rest.resources.UsuarioAdminResource;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/usuarios")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
public class AdminUsuarioController {

    private final UsuarioRepository usuarioRepository;

    @GetMapping
    public ResponseEntity<List<UsuarioAdminResource>> listarUsuarios() {

        List<UsuarioAdminResource> usuarios = usuarioRepository.findAll()
                .stream()
                .map(this::toResource)
                .toList();

        return ResponseEntity.ok(usuarios);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UsuarioAdminResource> obtenerUsuario(
            @PathVariable Long id
    ) {
        return usuarioRepository.findById(id)
                .map(this::toResource)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PatchMapping("/{id}/rol")
    public ResponseEntity<UsuarioAdminResource> actualizarRol(
            @PathVariable Long id,
            @RequestBody ActualizarRolResource resource
    ) {

        if (resource.rol() == null) {
            return ResponseEntity.badRequest().build();
        }

        return usuarioRepository.findById(id)
                .map(usuario -> {
                    usuario.setRol(resource.rol());

                    Usuario actualizado = usuarioRepository.save(usuario);

                    return ResponseEntity.ok(toResource(actualizado));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    private UsuarioAdminResource toResource(Usuario usuario) {
        return new UsuarioAdminResource(
                usuario.getId(),
                usuario.getNombre(),
                usuario.getEmail(),
                usuario.getRol()
        );
    }
}