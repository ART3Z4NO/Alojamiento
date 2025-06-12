package com.example.ProyectoF.controller;

import com.example.ProyectoF.model.HabitacionNormal;
import com.example.ProyectoF.Repository.HabitacionNormalRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.JsonMappingException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@SecurityRequirement(name = "bearerAuth")
@RestController
@RequestMapping("/habitaciones/normales")
@Tag(name = "Habitacion Normal", description = "Operaciones para habitaciones normales")
public class HabitacionNormalController {

    private static final Logger logger = LoggerFactory.getLogger(HabitacionNormalController.class);
    private final HabitacionNormalRepository repo;
    private final ObjectMapper mapper;

    public HabitacionNormalController(HabitacionNormalRepository repo, ObjectMapper mapper) {
        this.repo = repo;
        this.mapper = mapper;
    }

    @GetMapping
    @Operation(summary = "Listar habitaciones normales", responses = {
            @ApiResponse(responseCode = "200", description = "Listado exitoso")
    })
    public ResponseEntity<List<HabitacionNormal>> getAll() {
        logger.info("Listando habitaciones normales");
        List<HabitacionNormal> lista = repo.findAll();
        logger.info("Habitaciones encontradas: {}", lista.size());
        return ResponseEntity.ok(lista);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar habitación normal por ID", responses = {
            @ApiResponse(responseCode = "200", description = "Habitación encontrada"),
            @ApiResponse(responseCode = "404", description = "No encontrada")
    })
    public ResponseEntity<HabitacionNormal> getById(@PathVariable Long id) {
        logger.info("Buscando habitación normal con ID: {}", id);
        return repo.findById(id)
                .map(h -> {
                    logger.info("Habitación encontrada: {}", h);
                    return ResponseEntity.ok(h);
                })
                .orElseGet(() -> {
                    logger.warn("No existe habitación con ID: {}", id);
                    return ResponseEntity.notFound().build();
                });
    }

    @PostMapping
    @Operation(summary = "Crear habitación normal", responses = {
            @ApiResponse(responseCode = "201", description = "Habitación creada")
    })
    public ResponseEntity<HabitacionNormal> create(@RequestBody HabitacionNormal h) {
        logger.info("Creando habitación normal: {}", h);
        h.setId(null);
        HabitacionNormal nueva = repo.save(h);
        logger.info("Habitación creada con ID: {}", nueva.getId());
        return ResponseEntity.status(HttpStatus.CREATED).body(nueva);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar habitación normal completamente", responses = {
            @ApiResponse(responseCode = "200", description = "Actualizada correctamente"),
            @ApiResponse(responseCode = "404", description = "No encontrada")
    })
    public ResponseEntity<HabitacionNormal> update(@PathVariable Long id, @RequestBody HabitacionNormal h) {
        logger.info("Actualizando habitación normal con ID: {}", id);
        if (!repo.existsById(id)) {
            logger.warn("No se encontró la habitación con ID: {}", id);
            return ResponseEntity.notFound().build();
        }
        h.setId(id);
        HabitacionNormal actualizada = repo.save(h);
        logger.info("Habitación actualizada: {}", actualizada);
        return ResponseEntity.ok(actualizada);
    }

    @PatchMapping("/{id}")
    @Operation(summary = "Actualizar parcialmente habitación normal", responses = {
            @ApiResponse(responseCode = "200", description = "Actualización parcial exitosa"),
            @ApiResponse(responseCode = "400", description = "Error de datos"),
            @ApiResponse(responseCode = "404", description = "No encontrada")
    })
    public ResponseEntity<HabitacionNormal> patch(@PathVariable Long id, @RequestBody Map<String, Object> cambios) {
        logger.info("Actualización parcial de habitación con ID: {}", id);
        HabitacionNormal h = repo.findById(id).orElse(null);
        if (h == null) {
            logger.warn("No existe habitación con ID: {}", id);
            return ResponseEntity.notFound().build();
        }

        try {
            mapper.updateValue(h, cambios);
        } catch (JsonMappingException e) {
            logger.error("Error al aplicar cambios: {}", e.getMessage());
            return ResponseEntity.badRequest().build();
        }

        HabitacionNormal actualizada = repo.save(h);
        logger.info("Habitación con ID {} actualizada parcialmente", id);
        return ResponseEntity.ok(actualizada);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar habitación normal", responses = {
            @ApiResponse(responseCode = "204", description = "Eliminada correctamente"),
            @ApiResponse(responseCode = "404", description = "No encontrada")
    })
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        logger.info("Eliminando habitación normal con ID: {}", id);
        if (!repo.existsById(id)) {
            logger.warn("Habitación con ID {} no encontrada", id);
            return ResponseEntity.notFound().build();
        }
        repo.deleteById(id);
        logger.info("Habitación eliminada");
        return ResponseEntity.noContent().build();
    }
}