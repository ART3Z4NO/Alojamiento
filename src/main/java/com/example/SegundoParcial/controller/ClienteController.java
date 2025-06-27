package com.example.SegundoParcial.controller;

import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.example.SegundoParcial.model.Cliente;
import com.example.SegundoParcial.Repository.ClienteRepository;

import java.util.List;
import java.util.Map;

@SecurityRequirement(name = "bearerAuth")
@RestController
@RequestMapping("/clientes")
@Tag(name = "clientes", description = "Operaciones para gestionar clientes")
public class ClienteController {

    private static final Logger logger = LoggerFactory.getLogger(ClienteController.class);

    private final ClienteRepository repo;
    private final ObjectMapper mapper;

    public ClienteController(ClienteRepository repo, ObjectMapper mapper) {
        this.repo = repo;
        this.mapper = mapper;
    }

    @GetMapping
    @Operation(
            summary = "Listar todos los clientes",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Lista de clientes obtenida exitosamente")
            }
    )
    public ResponseEntity<List<Cliente>> getAll() {
        logger.info("Solicitud para listar todos los clientes");
        List<Cliente> clientes = repo.findAll();
        logger.info("Cantidad de clientes encontrados: {}", clientes.size());
        return ResponseEntity.ok(clientes);
    }

    @GetMapping("/{id}")
    @Operation(
            summary = "Obtener cliente por ID",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Cliente encontrado"),
                    @ApiResponse(responseCode = "404", description = "Cliente no encontrado")
            }
    )
    public ResponseEntity<Cliente> getById(@PathVariable Long id) {
        logger.info("Solicitud para obtener cliente con ID: {}", id);
        return repo.findById(id)
                .map(cliente -> {
                    logger.info("Cliente encontrado: {}", cliente);
                    return ResponseEntity.ok(cliente);
                })
                .orElseGet(() -> {
                    logger.warn("Cliente con ID {} no encontrado", id);
                    return ResponseEntity.notFound().build();
                });
    }

    @PostMapping
    @Operation(
            summary = "Crear un nuevo cliente",
            responses = {
                    @ApiResponse(responseCode = "201", description = "Cliente creado exitosamente")
            }
    )
    public ResponseEntity<Cliente> create(@RequestBody Cliente cliente) {
        logger.info("Solicitud para crear un nuevo cliente: {}", cliente);
        cliente.setId(null);
        Cliente saved = repo.save(cliente);
        logger.info("Cliente creado con ID: {}", saved.getId());
        return new ResponseEntity<>(saved, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    @Operation(
            summary = "Actualizar cliente completo",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Cliente actualizado exitosamente"),
                    @ApiResponse(responseCode = "404", description = "Cliente no encontrado")
            }
    )
    public ResponseEntity<Cliente> update(@PathVariable Long id, @RequestBody Cliente cliente) {
        logger.info("Solicitud para actualizar cliente con ID: {}", id);
        if (!repo.existsById(id)) {
            logger.warn("No se encontró cliente con ID: {} para actualizar", id);
            return ResponseEntity.notFound().build();
        }
        cliente.setId(id);
        Cliente updated = repo.save(cliente);
        logger.info("Cliente con ID {} actualizado", id);
        return ResponseEntity.ok(updated);
    }

    @PatchMapping("/{id}")
    @Operation(
            summary = "Actualizar parcialmente cliente",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Cliente actualizado parcialmente"),
                    @ApiResponse(responseCode = "400", description = "Error al procesar la solicitud"),
                    @ApiResponse(responseCode = "404", description = "Cliente no encontrado")
            }
    )
    public ResponseEntity<Cliente> patch(@PathVariable Long id, @RequestBody Map<String, Object> cambios) {
        logger.info("Solicitud para actualizar parcialmente cliente con ID: {}", id);
        Cliente cliente = repo.findById(id).orElse(null);
        if (cliente == null) {
            logger.warn("Cliente con ID {} no encontrado para patch", id);
            return ResponseEntity.notFound().build();
        }

        try {
            mapper.updateValue(cliente, cambios);
        } catch (JsonMappingException e) {
            logger.error("Error al mapear JSON para cliente ID {}: {}", id, e.getMessage());
            return ResponseEntity.badRequest().build();
        }

        Cliente updated = repo.save(cliente);
        logger.info("Cliente con ID {} actualizado parcialmente", id);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    @Operation(
            summary = "Eliminar un cliente",
            responses = {
                    @ApiResponse(responseCode = "204", description = "Cliente eliminado exitosamente"),
                    @ApiResponse(responseCode = "404", description = "Cliente no encontrado")
            }
    )
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        logger.info("Solicitud para eliminar cliente con ID: {}", id);
        if (!repo.existsById(id)) {
            logger.warn("Cliente con ID {} no encontrado para eliminar", id);
            return ResponseEntity.notFound().build();
        }
        repo.deleteById(id);
        logger.info("Cliente con ID {} eliminado", id);
        return ResponseEntity.noContent().build();
    }
}