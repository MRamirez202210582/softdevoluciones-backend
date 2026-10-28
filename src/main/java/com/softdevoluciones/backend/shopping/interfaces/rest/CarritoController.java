
package com.softdevoluciones.backend.shopping.interfaces.rest;

import com.softdevoluciones.backend.identity.infrastructure.persistence.jpa.repositories.UsuarioRepository;
import com.softdevoluciones.backend.shopping.domain.model.commands.*;
import com.softdevoluciones.backend.shopping.domain.model.queries.GetCarritoByUsuarioIdQuery;
import com.softdevoluciones.backend.shopping.domain.services.CarritoCommandService;
import com.softdevoluciones.backend.shopping.domain.services.CarritoQueryService;
import com.softdevoluciones.backend.shopping.interfaces.rest.resources.*;
import com.softdevoluciones.backend.shopping.interfaces.rest.transform.CarritoResourceFromEntityAssembler;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.util.List;
import java.util.NoSuchElementException;

@RestController
@RequestMapping("/api/carrito")
@RequiredArgsConstructor
public class CarritoController {

    private final CarritoCommandService commandService;
    private final CarritoQueryService queryService;
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

    private CarritoResource convertirCarrito(
            com.softdevoluciones.backend.shopping.domain.model.aggregates.Carrito carrito
    ) {
        return CarritoResourceFromEntityAssembler.toResourceFromEntity(carrito);
    }

    @GetMapping
    public ResponseEntity<CarritoResource> getCarrito(
            Authentication authentication
    ) {

        Long usuarioId = obtenerUsuarioId(authentication);

        var carrito = queryService.handle(
                new GetCarritoByUsuarioIdQuery(usuarioId)
        );

        if (carrito.isEmpty()) {
            return ResponseEntity.ok(
                    new CarritoResource(
                            null,
                            usuarioId,
                            List.of(),
                            BigDecimal.ZERO
                    )
            );
        }

        return ResponseEntity.ok(convertirCarrito(carrito.get()));
    }

    @PostMapping
    public ResponseEntity<CarritoResource> createCarrito(
            Authentication authentication,
            @Valid @RequestBody SaveCarritoResource resource
    ) {

        try {
            Long usuarioId = obtenerUsuarioId(authentication);

            var carrito = commandService.handle(
                    new CreateCarritoCommand(
                            usuarioId,
                            resource.productoId(),
                            resource.cantidad()
                    )
            );

            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(convertirCarrito(carrito));

        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, e.getMessage()
            );
        } catch (NoSuchElementException e) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, e.getMessage()
            );
        }
    }

    @PutMapping
    public ResponseEntity<CarritoResource> updateCarrito(
            Authentication authentication,
            @Valid @RequestBody SaveCarritoResource resource
    ) {

        try {
            Long usuarioId = obtenerUsuarioId(authentication);

            var carrito = commandService.handle(
                    new UpdateCarritoCommand(
                            usuarioId,
                            resource.productoId(),
                            resource.cantidad()
                    )
            );

            return ResponseEntity.ok(convertirCarrito(carrito));

        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, e.getMessage()
            );
        } catch (NoSuchElementException e) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, e.getMessage()
            );
        }
    }

    @DeleteMapping("/producto/{productoId}")
    public ResponseEntity<CarritoResource> deleteProductoCarrito(
            Authentication authentication,
            @PathVariable Long productoId
    ) {

        try {
            Long usuarioId = obtenerUsuarioId(authentication);

            var carrito = commandService.handle(
                    new DeleteCarritoCommand(usuarioId, productoId)
            );

            return ResponseEntity.ok(convertirCarrito(carrito));

        } catch (NoSuchElementException e) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, e.getMessage()
            );
        }
    }

    @DeleteMapping
    public ResponseEntity<CarritoResource> clearCarrito(
            Authentication authentication
    ) {

        try {
            Long usuarioId = obtenerUsuarioId(authentication);

            var carrito = commandService.handle(
                    new ClearCarritoCommand(usuarioId)
            );

            return ResponseEntity.ok(convertirCarrito(carrito));

        } catch (NoSuchElementException e) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, e.getMessage()
            );
        }
    }
}
