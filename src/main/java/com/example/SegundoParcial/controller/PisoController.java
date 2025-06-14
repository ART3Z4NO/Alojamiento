

package com.example.SegundoParcial.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.JsonMappingException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.example.SegundoParcial.model.Piso;
import com.example.SegundoParcial.Repository.PisoRepository;

import java.util.List;
import java.util.Map;

@SecurityRequirement(name = "bearerAuth")
@RestController
@RequestMapping("/pisos")
@Tag(name = "Piso", description = "Operaciones para gestionar pisos")
public class PisoController {

    private final PisoRepository repo;
    private final ObjectMapper mapper;
    private static final Logger logger = LoggerFactory.getLogger(PisoController.class);

    public PisoController(PisoRepository repo, ObjectMapper mapper) {
        this.repo = repo;
        this.mapper = mapper;
    }

    @GetMapping
    @Operation(summary = "Listar todos los pisos", description = "Obtiene una lista completa de todos los pisos registrados")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de pisos obtenida exitosamente"),
            @ApiResponse(responseCode = "204", description = "No hay pisos registrados")
    })
    public ResponseEntity<List<Piso>> getAll() {
        logger.info("Solicitando lista completa de pisos");
        List<Piso> pisos = repo.findAll();

        if (pisos.isEmpty()) {
            logger.warn("No se encontraron pisos registrados");
            return ResponseEntity.noContent().build();
        }

        logger.debug("Se encontraron {} pisos", pisos.size());
        return ResponseEntity.ok(pisos);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener piso por ID", description = "Recupera los detalles de un piso específico por su ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Piso encontrado"),
            @ApiResponse(responseCode = "404", description = "Piso no encontrado")
    })
    public ResponseEntity<Piso> getById(@PathVariable Long id) {
        logger.info("Buscando piso con ID: {}", id);
        return repo.findById(id)
                .map(piso -> {
                    logger.debug("Piso encontrado: {}", piso);
                    return ResponseEntity.ok(piso);
                })
                .orElseGet(() -> {
                    logger.warn("Piso con ID {} no encontrado", id);
                    return ResponseEntity.notFound().build();
                });
    }

    @PostMapping
    @Operation(summary = "Crear un nuevo piso", description = "Registra un nuevo piso en el sistema")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Piso creado exitosamente"),
            @ApiResponse(responseCode = "400", description = "Datos del piso inválidos")
    })
    public ResponseEntity<Piso> create(@RequestBody Piso piso) {
        logger.info("Intentando crear nuevo piso");
        logger.debug("Datos del piso recibidos: {}", piso);

        piso.setId(null);
        Piso saved = repo.save(piso);

        logger.info("Piso creado exitosamente con ID: {}", saved.getId());
        return new ResponseEntity<>(saved, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar piso completamente", description = "Reemplaza todos los datos de un piso existente")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Piso actualizado exitosamente"),
            @ApiResponse(responseCode = "400", description = "Datos del piso inválidos"),
            @ApiResponse(responseCode = "404", description = "Piso no encontrado")
    })
    public ResponseEntity<Piso> update(@PathVariable Long id, @RequestBody Piso piso) {
        logger.info("Intentando actualizar piso con ID: {}", id);

        if (!repo.existsById(id)) {
            logger.warn("No se puede actualizar: Piso con ID {} no encontrado", id);
            return ResponseEntity.notFound().build();
        }

        piso.setId(id);
        Piso updated = repo.save(piso);

        logger.info("Piso con ID {} actualizado exitosamente", id);
        return ResponseEntity.ok(updated);
    }

    @PatchMapping("/{id}")
    @Operation(summary = "Actualizar piso parcialmente", description = "Actualiza solo los campos especificados de un piso existente")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Piso actualizado exitosamente"),
            @ApiResponse(responseCode = "400", description = "Datos de actualización inválidos"),
            @ApiResponse(responseCode = "404", description = "Piso no encontrado")
    })
    public ResponseEntity<Piso> patch(@PathVariable Long id, @RequestBody Map<String, Object> cambios) {
        logger.info("Intentando actualización parcial del piso con ID: {}", id);
        logger.debug("Cambios recibidos: {}", cambios);

        Piso piso = repo.findById(id).orElse(null);
        if (piso == null) {
            logger.warn("No se puede actualizar: Piso con ID {} no encontrado", id);
            return ResponseEntity.notFound().build();
        }

        try {
            mapper.updateValue(piso, cambios);
            logger.debug("Datos del piso después de aplicar cambios: {}", piso);
        } catch (JsonMappingException e) {
            logger.error("Error al mapear los cambios al piso: {}", e.getMessage());
            return ResponseEntity.badRequest().build();
        }

        Piso updated = repo.save(piso);
        logger.info("Piso con ID {} actualizado parcialmente", id);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar un piso", description = "Elimina permanentemente un piso del sistema")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Piso eliminado exitosamente"),
            @ApiResponse(responseCode = "404", description = "Piso no encontrado")
    })
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        logger.info("Intentando eliminar piso con ID: {}", id);

        if (!repo.existsById(id)) {
            logger.warn("No se puede eliminar: Piso con ID {} no encontrado", id);
            return ResponseEntity.notFound().build();
        }

        repo.deleteById(id);
        logger.info("Piso con ID {} eliminado exitosamente", id);
        return ResponseEntity.noContent().build();
    }
}

