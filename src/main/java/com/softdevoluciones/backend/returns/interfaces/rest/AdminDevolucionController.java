
package com.softdevoluciones.backend.returns.interfaces.rest;

import com.softdevoluciones.backend.returns.domain.model.queries.GetAllDevolucionesQuery;
import com.softdevoluciones.backend.returns.domain.model.valueobjects.EstadoDevolucion;
import com.softdevoluciones.backend.returns.domain.model.valueobjects.MotivoDevolucion;
import com.softdevoluciones.backend.returns.domain.services.DevolucionCommandService;
import com.softdevoluciones.backend.returns.domain.services.DevolucionQueryService;
import com.softdevoluciones.backend.returns.interfaces.rest.resources.DevolucionResource;
import com.softdevoluciones.backend.returns.interfaces.rest.resources.UpdateEstadoDevolucionResource;
import com.softdevoluciones.backend.returns.interfaces.rest.transform.DevolucionResourceFromEntityAssembler;
import com.softdevoluciones.backend.returns.interfaces.rest.transform.UpdateEstadoDevolucionCommandFromResourceAssembler;

import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequestMapping("/api/admin/devoluciones")
public class AdminDevolucionController {

    private final DevolucionCommandService commandService;
    private final DevolucionQueryService queryService;

    public AdminDevolucionController(
            DevolucionCommandService commandService,
            DevolucionQueryService queryService
    ) {
        this.commandService = commandService;
        this.queryService = queryService;
    }

    @GetMapping
    public ResponseEntity<Page<DevolucionResource>> listar(
            @RequestParam(required = false) EstadoDevolucion estado,
            @RequestParam(required = false) MotivoDevolucion motivo,
            @RequestParam(required = false) LocalDate fechaDesde,
            @RequestParam(required = false) LocalDate fechaHasta,
            @RequestParam(defaultValue = "0") int pagina,
            @RequestParam(defaultValue = "10") int tamanio
    ) {
        var query = new GetAllDevolucionesQuery(
                estado,
                motivo,
                fechaDesde,
                fechaHasta,
                pagina,
                tamanio
        );

        var resultado = queryService.handle(query)
                .map(DevolucionResourceFromEntityAssembler::toResource);

        return ResponseEntity.ok(resultado);
    }

    @PatchMapping("/{id}/estado")
    public ResponseEntity<DevolucionResource> actualizarEstado(
            @PathVariable Long id,
            @Valid @RequestBody UpdateEstadoDevolucionResource resource
    ) {
        var command =
                UpdateEstadoDevolucionCommandFromResourceAssembler
                        .toCommand(id, resource);

        var solicitud = commandService.actualizarEstado(command);

        return ResponseEntity.ok(
                DevolucionResourceFromEntityAssembler
                        .toResource(solicitud)
        );
    }
}
