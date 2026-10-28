
package com.softdevoluciones.backend.returns.interfaces.rest;

import com.softdevoluciones.backend.identity.infrastructure.persistence.jpa.repositories.UsuarioRepository;
import com.softdevoluciones.backend.returns.domain.model.queries.GetDevolucionByIdQuery;
import com.softdevoluciones.backend.returns.domain.model.queries.GetDevolucionesByUsuarioQuery;
import com.softdevoluciones.backend.returns.domain.services.DevolucionCommandService;
import com.softdevoluciones.backend.returns.domain.services.DevolucionQueryService;
import com.softdevoluciones.backend.returns.interfaces.rest.resources.CreateDevolucionResource;
import com.softdevoluciones.backend.returns.interfaces.rest.resources.DevolucionResource;
import com.softdevoluciones.backend.returns.interfaces.rest.transform.CreateDevolucionCommandFromResourceAssembler;
import com.softdevoluciones.backend.returns.interfaces.rest.transform.DevolucionResourceFromEntityAssembler;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/api/devoluciones")
@RequiredArgsConstructor
public class DevolucionController {

    private final DevolucionCommandService commandService;
    private final DevolucionQueryService queryService;
    private final UsuarioRepository usuarioRepository;

    @PostMapping
    public ResponseEntity<DevolucionResource> crear(
            @Valid @RequestBody CreateDevolucionResource resource,
            Authentication authentication
    ) {
        Long usuarioId = obtenerUsuarioId(authentication);

        var command = CreateDevolucionCommandFromResourceAssembler
                .toCommand(resource);

        var solicitud = commandService.crearDevolucion(
                command, usuarioId
        );

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(DevolucionResourceFromEntityAssembler
                        .toResource(solicitud));
    }

    @GetMapping("/mis-devoluciones")
    public ResponseEntity<List<DevolucionResource>> listarMias(
            Authentication authentication
    ) {
        Long usuarioId = obtenerUsuarioId(authentication);

        var solicitudes = queryService.handle(
                new GetDevolucionesByUsuarioQuery(usuarioId)
        );

        return ResponseEntity.ok(
                solicitudes.stream()
                        .map(DevolucionResourceFromEntityAssembler::toResource)
                        .toList()
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<DevolucionResource> obtenerPorId(
            @PathVariable Long id,
            Authentication authentication
    ) {
        Long usuarioId = obtenerUsuarioId(authentication);

        var solicitud = queryService.handle(
                new GetDevolucionByIdQuery(id)
        ).orElseThrow(() -> new ResponseStatusException(
                HttpStatus.NOT_FOUND,
                "La devolución no existe."
        ));

        if (!solicitud.getUsuarioId().equals(usuarioId)) {
            throw new ResponseStatusException(
                    HttpStatus.FORBIDDEN,
                    "No tienes permiso para consultar esta devolución."
            );
        }

        return ResponseEntity.ok(
                DevolucionResourceFromEntityAssembler.toResource(solicitud)
        );
    }

    private Long obtenerUsuarioId(Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new ResponseStatusException(
                    HttpStatus.UNAUTHORIZED,
                    "Debes iniciar sesión."
            );
        }

        return usuarioRepository.findByEmail(authentication.getName())
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.UNAUTHORIZED,
                        "Usuario no encontrado."
                ))
                .getId();
    }
}

