
package com.example.ProyectoF.controller;

import com.example.ProyectoF.dto.ReservaRequestDTO;
import com.example.ProyectoF.dto.ReservaResponseDTO;
import com.example.ProyectoF.dto.ClienteResponseDTO;
import com.example.ProyectoF.dto.HabitacionResponseDTO;
import com.example.ProyectoF.dto.PisoResponseDTO;
import com.example.ProyectoF.dto.ServicioResponseDTO;

import com.example.ProyectoF.model.Cliente;
import com.example.ProyectoF.model.Habitacion;
import com.example.ProyectoF.model.Piso;
import com.example.ProyectoF.model.Reserva;
import com.example.ProyectoF.model.Servicio;

import com.example.ProyectoF.Repository.ClienteRepository;
import com.example.ProyectoF.Repository.HabitacionRepository;
import com.example.ProyectoF.Repository.PisoRepository;
import com.example.ProyectoF.Repository.ReservaRepository;
import com.example.ProyectoF.Repository.ServicioRepository;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@SecurityRequirement(name = "bearerAuth")
@RestController
@RequestMapping("/reservas")
@Tag(name = "Reservas", description = "API para gestión de reservas de hotel")
public class ReservaController {

    private final ReservaRepository repo;
    private final ClienteRepository clienteRepository;
    private final HabitacionRepository habitacionRepository;
    private final PisoRepository pisoRepository;
    private final ServicioRepository servicioRepository;

    private static final Logger logger = LoggerFactory.getLogger(ReservaController.class);

    @Autowired
    public ReservaController(ReservaRepository repo, ClienteRepository clienteRepository,
                             HabitacionRepository habitacionRepository, PisoRepository pisoRepository,
                             ServicioRepository servicioRepository) {
        this.repo = repo;
        this.clienteRepository = clienteRepository;
        this.habitacionRepository = habitacionRepository;
        this.pisoRepository = pisoRepository;
        this.servicioRepository = servicioRepository;
    }

    @PostMapping
    @Operation(summary = "Crear una nueva reserva", description = "Crea una nueva reserva con los datos proporcionados")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Reserva creada exitosamente"),
            @ApiResponse(responseCode = "400", description = "Datos de entrada inválidos"),
            @ApiResponse(responseCode = "404", description = "Recurso no encontrado (cliente, habitación, piso o servicio)")
    })
    public ResponseEntity<ReservaResponseDTO> crear(@RequestBody ReservaRequestDTO reservaDTO) {
        logger.info("Iniciando creación de nueva reserva - Cliente ID: {}, Habitación ID: {}",
                reservaDTO.getClienteId(), reservaDTO.getHabitacionId());
        logger.debug("Datos de reserva recibidos: {}", reservaDTO);

        Reserva reserva = new Reserva();
        reserva.setFechaIngreso(reservaDTO.getFechaIngreso());
        reserva.setFechaSalida(reservaDTO.getFechaSalida());

        try {
            Cliente cliente = clienteRepository.findById(reservaDTO.getClienteId())
                    .orElseThrow(() -> {
                        String errorMsg = String.format("Cliente no encontrado con ID: %d", reservaDTO.getClienteId());
                        logger.error(errorMsg);
                        return new NoSuchElementException(errorMsg);
                    });
            reserva.setCliente(cliente);
            logger.debug("Cliente asociado: {}", cliente.getNombre());

            Habitacion habitacion = habitacionRepository.findById(reservaDTO.getHabitacionId())
                    .orElseThrow(() -> {
                        String errorMsg = String.format("Habitación no encontrada con ID: %d", reservaDTO.getHabitacionId());
                        logger.error(errorMsg);
                        return new NoSuchElementException(errorMsg);
                    });
            reserva.setHabitacion(habitacion);
            logger.debug("Habitación asignada: {}", habitacion.getNumero());

            Piso piso = pisoRepository.findById(reservaDTO.getPisoId())
                    .orElseThrow(() -> {
                        String errorMsg = String.format("Piso no encontrado con ID: %d", reservaDTO.getPisoId());
                        logger.error(errorMsg);
                        return new NoSuchElementException(errorMsg);
                    });
            reserva.setPiso(piso);
            logger.debug("Piso asignado: {}", piso.getNumero());

            List<Servicio> servicios = servicioRepository.findAllById(reservaDTO.getServicioIds());
            if (servicios.size() != reservaDTO.getServicioIds().size()) {
                String errorMsg = "Algunos servicios no encontrados. IDs solicitados: " + reservaDTO.getServicioIds();
                logger.error(errorMsg);
                throw new NoSuchElementException(errorMsg);
            }
            reserva.setServicios(servicios);
            logger.debug("Servicios asociados: {}", servicios.stream().map(Servicio::getNombre).collect(Collectors.toList()));

            Reserva nuevaReserva = repo.save(reserva);
            logger.info("Reserva creada exitosamente con ID: {}", nuevaReserva.getId());

            return new ResponseEntity<>(convertToReservaResponseDTO(nuevaReserva), HttpStatus.CREATED);
        } catch (Exception e) {
            logger.error("Error al crear reserva: {}", e.getMessage(), e);
            throw e;
        }
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener reserva por ID", description = "Obtiene los detalles de una reserva específica")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Reserva encontrada"),
            @ApiResponse(responseCode = "404", description = "Reserva no encontrada")
    })
    public ResponseEntity<ReservaResponseDTO> getById(@PathVariable Long id) {
        logger.info("Solicitando reserva con ID: {}", id);

        try {
            Reserva reserva = repo.findById(id)
                    .orElseThrow(() -> {
                        String errorMsg = String.format("Reserva no encontrada con ID: %d", id);
                        logger.error(errorMsg);
                        return new NoSuchElementException(errorMsg);
                    });

            logger.debug("Reserva encontrada: {}", reserva);
            return ResponseEntity.ok(convertToReservaResponseDTO(reserva));
        } catch (Exception e) {
            logger.error("Error al obtener reserva con ID {}: {}", id, e.getMessage(), e);
            throw e;
        }
    }

    private ReservaResponseDTO convertToReservaResponseDTO(Reserva reserva) {
        logger.debug("Convirtiendo Reserva a DTO para ID: {}", reserva.getId());

        ReservaResponseDTO responseDTO = new ReservaResponseDTO();
        responseDTO.setId(reserva.getId());
        responseDTO.setFechaIngreso(reserva.getFechaIngreso());
        responseDTO.setFechaSalida(reserva.getFechaSalida());

        if (reserva.getCliente() != null) {
            ClienteResponseDTO clienteDTO = new ClienteResponseDTO();
            clienteDTO.setId(reserva.getCliente().getId());
            clienteDTO.setNombre(reserva.getCliente().getNombre());
            clienteDTO.setCi(reserva.getCliente().getCi());
            clienteDTO.setCorreo(reserva.getCliente().getCorreo());
            clienteDTO.setTelefono(reserva.getCliente().getTelefono());
            responseDTO.setCliente(clienteDTO);
            logger.trace("Cliente DTO creado para ID: {}", clienteDTO.getId());
        }

        if (reserva.getHabitacion() != null) {
            HabitacionResponseDTO habitacionDTO = new HabitacionResponseDTO();
            habitacionDTO.setId(reserva.getHabitacion().getId());
            habitacionDTO.setNumero(reserva.getHabitacion().getNumero());
            habitacionDTO.setPrecio(reserva.getHabitacion().getPrecio());
            habitacionDTO.setTipo(reserva.getHabitacion().getTipo());

            if (reserva.getHabitacion().getPiso() != null) {
                PisoResponseDTO pisoDTO = new PisoResponseDTO();
                pisoDTO.setId(reserva.getHabitacion().getPiso().getId());
                pisoDTO.setNumero(reserva.getHabitacion().getPiso().getNumero());
                habitacionDTO.setPiso(pisoDTO);
                logger.trace("Piso DTO creado para habitación ID: {}", habitacionDTO.getId());
            }

            responseDTO.setHabitacion(habitacionDTO);
            logger.trace("Habitación DTO creado para ID: {}", habitacionDTO.getId());
        }



        if (reserva.getPiso() != null) {
            PisoResponseDTO pisoDTO = new PisoResponseDTO();
            pisoDTO.setId(reserva.getPiso().getId());
            pisoDTO.setNumero(reserva.getPiso().getNumero());
            responseDTO.setPiso(pisoDTO);
            logger.trace("Piso DTO directo creado para ID: {}", pisoDTO.getId());
        }

        if (reserva.getServicios() != null) {
            responseDTO.setServicios(reserva.getServicios().stream()
                    .map(servicio -> {
                        ServicioResponseDTO servicioDTO = new ServicioResponseDTO();
                        servicioDTO.setId(servicio.getId());
                        servicioDTO.setNombre(servicio.getNombre());
                        servicioDTO.setPrecio(servicio.getPrecio());
                        return servicioDTO;
                    })
                    .collect(Collectors.toList()));
            logger.trace("Servicios DTO creados ({} elementos)", responseDTO.getServicios().size());
        }

        logger.debug("Conversión a DTO completada para reserva ID: {}", reserva.getId());
        return responseDTO;
    }

    @GetMapping
    @Operation(summary = "Listar todas las reservas", description = "Obtiene una lista de todas las reservas existentes")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de reservas obtenida exitosamente"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    public ResponseEntity<List<ReservaResponseDTO>> listarTodas() {
        logger.info("Solicitando listado de todas las reservas");

        try {
            List<Reserva> reservas = repo.findAll();
            List<ReservaResponseDTO> responseDTOs = reservas.stream()
                    .map(this::convertToReservaResponseDTO)
                    .collect(Collectors.toList());

            logger.info("Retornando {} reservas", responseDTOs.size());
            return ResponseEntity.ok(responseDTOs);
        } catch (Exception e) {
            logger.error("Error al listar reservas: {}", e.getMessage(), e);
            throw e;
        }
    }
}




