// src/main/java/com/example/ProyectoF/dto/ReservaResponseDTO.java
package com.example.ProyectoF.dto;


import java.time.LocalDate;
import java.util.List;


public class ReservaResponseDTO {
    private Long id;
    private LocalDate fechaIngreso;
    private LocalDate fechaSalida;
    private ClienteResponseDTO cliente;
    private HabitacionResponseDTO habitacion;
    private PisoResponseDTO piso;
    private List<ServicioResponseDTO> servicios;


    public ReservaResponseDTO() {
        // Constructor vacío explícito
    }


    public ReservaResponseDTO(Long id, LocalDate fechaIngreso, LocalDate fechaSalida, ClienteResponseDTO cliente, HabitacionResponseDTO habitacion, PisoResponseDTO piso, List<ServicioResponseDTO> servicios) {
        this.id = id;
        this.fechaIngreso = fechaIngreso;
        this.fechaSalida = fechaSalida;
        this.cliente = cliente;
        this.habitacion = habitacion;
        this.piso = piso;
        this.servicios = servicios;
    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getFechaIngreso() {
        return fechaIngreso;
    }

    public void setFechaIngreso(LocalDate fechaIngreso) {
        this.fechaIngreso = fechaIngreso;
    }

    public LocalDate getFechaSalida() {
        return fechaSalida;
    }

    public void setFechaSalida(LocalDate fechaSalida) {
        this.fechaSalida = fechaSalida;
    }

    public ClienteResponseDTO getCliente() {
        return cliente;
    }

    public void setCliente(ClienteResponseDTO cliente) {
        this.cliente = cliente;
    }

    public HabitacionResponseDTO getHabitacion() {
        return habitacion;
    }

    public void setHabitacion(HabitacionResponseDTO habitacion) {
        this.habitacion = habitacion;
    }

    public PisoResponseDTO getPiso() {
        return piso;
    }

    public void setPiso(PisoResponseDTO piso) {
        this.piso = piso;
    }

    public List<ServicioResponseDTO> getServicios() {
        return servicios;
    }

    public void setServicios(List<ServicioResponseDTO> servicios) {
        this.servicios = servicios;
    }
}
