package com.example.ProyectoF.controller;

import com.example.ProyectoF.Repository.HabitacionSencillaRepository;
import com.example.ProyectoF.model.HabitacionSencilla;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.JsonMappingException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@SecurityRequirement(name = "bearerAuth")
@RestController
@RequestMapping("/habitaciones/sencillas")
@Tag(name = "Habitación Sencilla", description = "Gestión de habitaciones sencillas")
public class HabitacionSencillaController {

    private static final Logger logger = LoggerFactory.getLogger(HabitacionSencillaController.class);
    private final HabitacionSencillaRepository repo;
    private final ObjectMapper mapper;

    public HabitacionSencillaController(HabitacionSencillaRepository repo, ObjectMapper mapper) {
        this.repo = repo;
        this.mapper = mapper;
    }

    @GetMapping
    @Operation(summary = "Listar habitaciones sencillas", responses = {
            @ApiResponse(responseCode = "200", description = "Listado exitoso")
    })
    public ResponseEntity<List<HabitacionSencilla>> getAll() {
        logger.info("Listando habitaciones sencillas");
        List<HabitacionSencilla> lista = repo.findAll();
        logger.info("Habitaciones sencillas encontradas: {}", lista.size());
        return ResponseEntity.ok(lista);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener habitación sencilla por ID", responses = {
            @ApiResponse(responseCode = "200", description = "Habitación encontrada"),
            @ApiResponse(responseCode = "404", description = "Habitación no encontrada")
    })
    public ResponseEntity<HabitacionSencilla> getById(@PathVariable Long id) {
        logger.info("Buscando habitación sencilla con ID: {}", id);
        return repo.findById(id)
                .map(h -> {
                    logger.info("Habitación encontrada: {}", h);
                    return ResponseEntity.ok(h);
                })
                .orElseGet(() -> {
                    logger.warn("No se encontró habitación con ID: {}", id);
                    return ResponseEntity.notFound().build();
                });
    }

    @PostMapping
    @Operation(summary = "Crear habitación sencilla", responses = {
            @ApiResponse(responseCode = "201", description = "Habitación creada")
    })
    public ResponseEntity<HabitacionSencilla> create(@RequestBody HabitacionSencilla h) {
        logger.info("Creando habitación sencilla: {}", h);
        h.setId(null);
        HabitacionSencilla nueva = repo.save(h);
        logger.info("Habitación creada con ID: {}", nueva.getId());
        return ResponseEntity.status(HttpStatus.CREATED).body(nueva);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar habitación sencilla completamente", responses = {
            @ApiResponse(responseCode = "200", description = "Habitación actualizada"),
            @ApiResponse(responseCode = "404", description = "Habitación no encontrada")
    })
    public ResponseEntity<HabitacionSencilla> update(@PathVariable Long id, @RequestBody HabitacionSencilla h) {
        logger.info("Actualizando habitación sencilla con ID: {}", id);
        if (!repo.existsById(id)) {
            logger.warn("Habitación con ID {} no encontrada", id);
            return ResponseEntity.notFound().build();
        }
        h.setId(id);
        HabitacionSencilla actualizada = repo.save(h);
        logger.info("Habitación actualizada: {}", actualizada);
        return ResponseEntity.ok(actualizada);
    }

    @PatchMapping("/{id}")
    @Operation(summary = "Actualizar parcialmente habitación sencilla", responses = {
            @ApiResponse(responseCode = "200", description = "Actualización parcial exitosa"),
            @ApiResponse(responseCode = "400", description = "Error en los datos"),
            @ApiResponse(responseCode = "404", description = "Habitación no encontrada")
    })
    public ResponseEntity<HabitacionSencilla> patch(@PathVariable Long id, @RequestBody Map<String, Object> cambios) {
        logger.info("Actualización parcial de habitación sencilla con ID: {}", id);
        HabitacionSencilla h = repo.findById(id).orElse(null);
        if (h == null) {
            logger.warn("Habitación con ID {} no encontrada", id);
            return ResponseEntity.notFound().build();
        }
        try {
            mapper.updateValue(h, cambios);
        } catch (JsonMappingException e) {
            logger.error("Error al aplicar cambios: {}", e.getMessage());
            return ResponseEntity.badRequest().build();
        }
        HabitacionSencilla actualizada = repo.save(h);
        logger.info("Habitación con ID {} actualizada parcialmente", id);
        return ResponseEntity.ok(actualizada);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar habitación sencilla", responses = {
            @ApiResponse(responseCode = "204", description = "Habitación eliminada"),
            @ApiResponse(responseCode = "404", description = "Habitación no encontrada")
    })
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        logger.info("Eliminando habitación sencilla con ID: {}", id);
        if (!repo.existsById(id)) {
            logger.warn("Habitación con ID {} no encontrada", id);
            return ResponseEntity.notFound().build();
        }
        repo.deleteById(id);
        logger.info("Habitación eliminada");
        return ResponseEntity.noContent().build();
    }
}