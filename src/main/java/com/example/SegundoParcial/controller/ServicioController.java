

package com.example.SegundoParcial.controller;

import com.example.SegundoParcial.Repository.ServicioRepository;
import com.example.SegundoParcial.model.Servicio;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@SecurityRequirement(name = "bearerAuth")
@RestController
@RequestMapping("/servicios")
@Tag(name = "Servicio", description = "Permite crear, ver, actualizar y eliminar servicios")
public class ServicioController {

    private final ServicioRepository repo;
    private final ObjectMapper mapper;
    private static final Logger logger = LoggerFactory.getLogger(ServicioController.class);

    public ServicioController(ServicioRepository repo, ObjectMapper mapper) {
        this.repo = repo;
        this.mapper = mapper;
    }

    @Operation(summary = "Obtener todos los servicios")
    @ApiResponse(responseCode = "200", description = "Lista de servicios obtenida correctamente")
    @GetMapping
    public List<Servicio> getAll() {
        logger.info("Obteniendo todos los servicios");
        return repo.findAll();
    }

    @Operation(summary = "Obtener un servicio por ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Servicio encontrado"),
            @ApiResponse(responseCode = "404", description = "Servicio no encontrado")
    })
    @GetMapping("/{id}")
    public Servicio getById(@PathVariable Long id) {
        logger.info("Obteniendo servicio con ID: {}", id);
        return repo.findById(id)
                .orElseThrow(() -> {
                    logger.error("Servicio no encontrado con ID: {}", id);
                    return new RuntimeException("Servicio no encontrado con ID: " + id);
                });
    }

    @Operation(summary = "Crear un nuevo servicio")
    @ApiResponse(responseCode = "201", description = "Servicio creado correctamente")
    @PostMapping
    public ResponseEntity<Servicio> crear(@RequestBody Servicio servicio) {
        logger.info("Creando nuevo servicio con nombre: {}", servicio.getNombre());
        Servicio creado = repo.save(servicio);
        logger.info("Servicio creado con ID: {}", creado.getId());
        return new ResponseEntity<>(creado, HttpStatus.CREATED);
    }

    @Operation(summary = "Actualizar un servicio completo")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Servicio actualizado correctamente"),
            @ApiResponse(responseCode = "404", description = "Servicio no encontrado")
    })
    @PutMapping("/{id}")
    public Servicio actualizar(@PathVariable Long id, @RequestBody Servicio s) {
        logger.info("Actualizando servicio con ID: {}", id);
        Servicio actual = repo.findById(id).orElseThrow(() -> {
            logger.error("Servicio no encontrado para actualizar con ID: {}", id);
            return new RuntimeException("Servicio no encontrado con ID: " + id);
        });
        actual.setNombre(s.getNombre());
        actual.setPrecio(s.getPrecio());
        logger.info("Servicio con ID: {} actualizado", id);
        return repo.save(actual);
    }

    @Operation(summary = "Actualizar parcialmente un servicio")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Servicio actualizado parcialmente"),
            @ApiResponse(responseCode = "404", description = "Servicio no encontrado"),
            @ApiResponse(responseCode = "500", description = "Error en la actualización parcial")
    })
    @PatchMapping("/{id}")
    public Servicio patch(@PathVariable Long id, @RequestBody Map<String, Object> cambios) throws Exception {
        logger.info("Actualizando parcialmente servicio con ID: {}", id);
        Servicio s = repo.findById(id).orElseThrow(() -> {
            logger.error("Servicio no encontrado para patch con ID: {}", id);
            return new RuntimeException("Servicio no encontrado con ID: " + id);
        });
        mapper.updateValue(s, cambios);
        logger.info("Servicio con ID: {} actualizado parcialmente", id);
        return repo.save(s);
    }

    @Operation(summary = "Eliminar un servicio por ID")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Servicio eliminado correctamente"),
            @ApiResponse(responseCode = "404", description = "Servicio no encontrado")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<String> eliminar(@PathVariable Long id) {
        logger.info("Eliminando servicio con ID: {}", id);
        if (!repo.existsById(id)) {
            logger.error("Servicio no encontrado para eliminar con ID: {}", id);
            return new ResponseEntity<>("Servicio no encontrado", HttpStatus.NOT_FOUND);
        }
        repo.deleteById(id);
        logger.info("Servicio eliminado con ID: {}", id);
        return new ResponseEntity<>("", HttpStatus.NO_CONTENT);
    }
}
