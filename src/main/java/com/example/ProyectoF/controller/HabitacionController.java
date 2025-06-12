package com.example.ProyectoF.controller;

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

import com.example.ProyectoF.model.Habitacion;
import com.example.ProyectoF.Repository.HabitacionRepository;

import java.util.List;
import java.util.Map;

@SecurityRequirement(name = "bearerAuth")
@RestController
@RequestMapping("/habitaciones")
@Tag(name = "Habitacion", description = "Operaciones para gestionar habitaciones")
public class HabitacionController {

    private static final Logger logger = LoggerFactory.getLogger(HabitacionController.class);

    private final HabitacionRepository repo;
    private final ObjectMapper mapper;

    public HabitacionController(HabitacionRepository repo, ObjectMapper mapper) {
        this.repo = repo;
        this.mapper = mapper;
    }

    @GetMapping
    @Operation(
            summary = "Listar todas las habitaciones",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Habitaciones listadas exitosamente")
            }
    )
    public List<Habitacion> getAll() {
        logger.info("Solicitud para listar todas las habitaciones");
        List<Habitacion> habitaciones = repo.findAll();
        logger.info("Total habitaciones encontradas: {}", habitaciones.size());
        return habitaciones;
    }

    @GetMapping("/{id}")
    @Operation(
            summary = "Obtener una habitación por ID",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Habitación encontrada"),
                    @ApiResponse(responseCode = "404", description = "Habitación no encontrada")
            }
    )
    public ResponseEntity<Habitacion> getById(@PathVariable Long id) {
        logger.info("Solicitud para obtener habitación con ID: {}", id);
        return repo.findById(id)
                .map(h -> {
                    logger.info("Habitación encontrada: {}", h);
                    return ResponseEntity.ok(h);
                })
                .orElseGet(() -> {
                    logger.warn("Habitación con ID {} no encontrada", id);
                    return ResponseEntity.notFound().build();
                });
    }

    @PostMapping
    @Operation(
            summary = "Crear una nueva habitación",
            responses = {
                    @ApiResponse(responseCode = "201", description = "Habitación creada exitosamente")
            }
    )
    public ResponseEntity<Habitacion> create(@RequestBody Habitacion h) {
        logger.info("Solicitud para crear nueva habitación: {}", h);
        h.setId(null);
        Habitacion saved = repo.save(h);
        logger.info("Habitación creada con ID: {}", saved.getId());
        return new ResponseEntity<>(saved, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    @Operation(
            summary = "Actualizar habitación",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Habitación actualizada exitosamente"),
                    @ApiResponse(responseCode = "404", description = "Habitación no encontrada")
            }
    )
    public ResponseEntity<Habitacion> update(@PathVariable Long id, @RequestBody Habitacion h) {
        logger.info("Solicitud para actualizar habitación con ID: {}", id);
        if (!repo.existsById(id)) {
            logger.warn("Habitación con ID {} no encontrada para actualizar", id);
            return ResponseEntity.notFound().build();
        }
        h.setId(id);
        Habitacion updated = repo.save(h);
        logger.info("Habitación con ID {} actualizada", id);
        return ResponseEntity.ok(updated);
    }

    @PatchMapping("/{id}")
    @Operation(
            summary = "Actualizar parcialmente una habitación",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Habitación actualizada parcialmente"),
                    @ApiResponse(responseCode = "400", description = "Error en el cuerpo de la solicitud"),
                    @ApiResponse(responseCode = "404", description = "Habitación no encontrada")
            }
    )
    public ResponseEntity<Habitacion> patch(@PathVariable Long id, @RequestBody Map<String, Object> cambios) {
        logger.info("Solicitud para actualizar parcialmente habitación con ID: {}", id);
        Habitacion h = repo.findById(id).orElse(null);
        if (h == null) {
            logger.warn("Habitación con ID {} no encontrada para patch", id);
            return ResponseEntity.notFound().build();
        }

        try {
            mapper.updateValue(h, cambios);
        } catch (JsonMappingException e) {
            logger.error("Error al mapear JSON para habitación ID {}: {}", id, e.getMessage());
            return ResponseEntity.badRequest().build();
        }

        Habitacion updated = repo.save(h);
        logger.info("Habitación con ID {} actualizada parcialmente", id);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    @Operation(
            summary = "Eliminar una habitación",
            responses = {
                    @ApiResponse(responseCode = "204", description = "Habitación eliminada exitosamente"),
                    @ApiResponse(responseCode = "404", description = "Habitación no encontrada")
            }
    )
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        logger.info("Solicitud para eliminar habitación con ID: {}", id);
        if (!repo.existsById(id)) {
            logger.warn("Habitación con ID {} no encontrada para eliminar", id);
            return ResponseEntity.notFound().build();
        }
        repo.deleteById(id);
        logger.info("Habitación con ID {} eliminada", id);
        return ResponseEntity.noContent().build();
    }
}