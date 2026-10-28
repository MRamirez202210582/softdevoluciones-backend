
package com.softdevoluciones.backend.catalog.interfaces.rest;

import com.softdevoluciones.backend.catalog.domain.model.commands.*;
import com.softdevoluciones.backend.catalog.domain.model.queries.*;
import com.softdevoluciones.backend.catalog.domain.services.ProductoCommandService;
import com.softdevoluciones.backend.catalog.domain.services.ProductoQueryService;
import com.softdevoluciones.backend.catalog.interfaces.rest.resources.*;
import com.softdevoluciones.backend.catalog.interfaces.rest.transform.ProductoResourceFromEntityAssembler;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.NoSuchElementException;

@RestController
@RequestMapping("/api/productos")
@RequiredArgsConstructor
public class ProductoController {

    private final ProductoCommandService commandService;
    private final ProductoQueryService queryService;

    @GetMapping
    public ResponseEntity<List<ProductoResource>> getAllProductos() {

        var productos = queryService.handle(new GetAllProductosQuery())
                .stream()
                .map(ProductoResourceFromEntityAssembler::toResourceFromEntity)
                .toList();

        return ResponseEntity.ok(productos);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductoResource> getProductoById(
            @PathVariable Long id
    ) {

        var producto = queryService.handle(new GetProductoByIdQuery(id))
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Producto no encontrado"
                ));

        return ResponseEntity.ok(
                ProductoResourceFromEntityAssembler.toResourceFromEntity(producto)
        );
    }

    @PostMapping
    public ResponseEntity<ProductoResource> createProducto(
            @Valid @RequestBody SaveProductoResource resource
    ) {

        try {
            var command = new CreateProductoCommand(
                    resource.nombre(),
                    resource.descripcion(),
                    resource.precio(),
                    resource.stock(),
                    resource.imagenUrl()
            );

            var producto = commandService.handle(command);

            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(ProductoResourceFromEntityAssembler
                            .toResourceFromEntity(producto));

        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    e.getMessage()
            );
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProductoResource> updateProducto(
            @PathVariable Long id,
            @Valid @RequestBody SaveProductoResource resource
    ) {

        try {
            var command = new UpdateProductoCommand(
                    id,
                    resource.nombre(),
                    resource.descripcion(),
                    resource.precio(),
                    resource.stock(),
                    resource.imagenUrl()
            );

            var producto = commandService.handle(command);

            return ResponseEntity.ok(
                    ProductoResourceFromEntityAssembler.toResourceFromEntity(producto)
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

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProducto(
            @PathVariable Long id
    ) {

        try {
            commandService.handle(new DeleteProductoCommand(id));

            return ResponseEntity.noContent().build();

        } catch (NoSuchElementException e) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    e.getMessage()
            );
        }
    }
}
