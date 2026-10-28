package com.softdevoluciones.backend.ordering.interfaces.rest;

import com.softdevoluciones.backend.identity.infrastructure.persistence.jpa.repositories.UsuarioRepository;
import com.softdevoluciones.backend.ordering.domain.model.commands.UpdatePedidoEstadoCommand;
import com.softdevoluciones.backend.ordering.domain.model.queries.GetAllPedidosQuery;
import com.softdevoluciones.backend.ordering.domain.model.queries.GetPedidoByIdQuery;
import com.softdevoluciones.backend.ordering.domain.model.queries.GetPedidosByUsuarioQuery;
import com.softdevoluciones.backend.ordering.domain.services.PedidoCommandService;
import com.softdevoluciones.backend.ordering.domain.services.PedidoQueryService;
import com.softdevoluciones.backend.ordering.interfaces.rest.resources.PedidoResource;
import com.softdevoluciones.backend.ordering.interfaces.rest.resources.UpdatePedidoEstadoResource;
import com.softdevoluciones.backend.ordering.interfaces.rest.transform.PedidoResourceFromEntityAssembler;
import com.softdevoluciones.backend.ordering.domain.model.commands.CreatePedidoCommand;
import com.softdevoluciones.backend.ordering.interfaces.rest.resources.CreatePedidoResource;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.NoSuchElementException;

@RestController
@RequestMapping("/api/pedidos")
@RequiredArgsConstructor
public class PedidoController {

    private final PedidoCommandService commandService;
    private final PedidoQueryService queryService;
    private final UsuarioRepository usuarioRepository;

    private Long obtenerUsuarioId(Authentication authentication) {

        if (authentication == null || !authentication.isAuthenticated()) {
            throw new ResponseStatusException(
                    HttpStatus.UNAUTHORIZED,
                    "Debes iniciar sesión"
            );
        }

        return usuarioRepository.findByEmail(authentication.getName())
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.UNAUTHORIZED,
                        "Usuario no encontrado"
                ))
                .getId();
    }

    // Consultar las compras del usuario autenticado
    @GetMapping("/mis-pedidos")
    public ResponseEntity<List<PedidoResource>> getMisPedidos(
            Authentication authentication
    ) {

        Long usuarioId = obtenerUsuarioId(authentication);

        var pedidos = queryService.handle(
                        new GetPedidosByUsuarioQuery(usuarioId)
                ).stream()
                .map(PedidoResourceFromEntityAssembler::toResourceFromEntity)
                .toList();

        return ResponseEntity.ok(pedidos);
    }

    // Consultar todas las compras
    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'OPERADOR')")
    public ResponseEntity<List<PedidoResource>> getAllPedidos() {

        var pedidos = queryService.handle(new GetAllPedidosQuery())
                .stream()
                .map(PedidoResourceFromEntityAssembler::toResourceFromEntity)
                .toList();

        return ResponseEntity.ok(pedidos);
    }

    // Consultar una compra específica
    @GetMapping("/{id}")
    public ResponseEntity<PedidoResource> getPedidoById(
            @PathVariable Long id,
            Authentication authentication
    ) {

        var pedido = queryService.handle(new GetPedidoByIdQuery(id))
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Pedido no encontrado"
                ));

        Long usuarioId = obtenerUsuarioId(authentication);

        boolean esPersonalAutorizado = authentication.getAuthorities()
                .stream()
                .anyMatch(authority ->
                        authority.getAuthority().equals("ROLE_ADMIN")
                                || authority.getAuthority().equals("ROLE_OPERADOR")
                );

        if (!esPersonalAutorizado
                && !pedido.getUsuario().getId().equals(usuarioId)) {

            throw new ResponseStatusException(
                    HttpStatus.FORBIDDEN,
                    "No tienes permiso para consultar este pedido"
            );
        }

        return ResponseEntity.ok(
                PedidoResourceFromEntityAssembler.toResourceFromEntity(pedido)
        );
    }

    // Actualizar el estado de una compra
    @PatchMapping("/{id}/estado")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<PedidoResource> updateEstado(
            @PathVariable Long id,
            @Valid @RequestBody UpdatePedidoEstadoResource resource
    ) {

        try {

            var pedido = commandService.handle(
                    new UpdatePedidoEstadoCommand(
                            id,
                            resource.estado()
                    )
            );

            return ResponseEntity.ok(
                    PedidoResourceFromEntityAssembler
                            .toResourceFromEntity(pedido)
            );

        } catch (NoSuchElementException e) {

            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    e.getMessage()
            );

        } catch (IllegalArgumentException e) {

            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    e.getMessage()
            );
        }
    }

    @PostMapping
    @PreAuthorize("hasRole('CLIENTE')")
    public ResponseEntity<PedidoResource> createPedido(
            Authentication authentication,
            @Valid @RequestBody CreatePedidoResource resource
    ) {
        try {
            Long usuarioId = obtenerUsuarioId(authentication);

            var pedido = commandService.handle(
                    new CreatePedidoCommand(
                            usuarioId,
                            resource.clienteNombre(),
                            resource.celular(),
                            resource.direccion()
                    )
            );

            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(
                            PedidoResourceFromEntityAssembler
                                    .toResourceFromEntity(pedido)
                    );

        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    e.getMessage()
            );

        } catch (NoSuchElementException e) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    e.getMessage()
            );
        }
    }
}