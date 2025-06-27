// src/main/java/com/example/SegundoParcial/dto/ReservaRequestDTO.java
package com.example.SegundoParcial.dto;

import java.time.LocalDate;
import java.util.List;

public class ReservaRequestDTO {
    private LocalDate fechaIngreso;
    private LocalDate fechaSalida;
    private Long clienteId;
    private Long habitacionId;
    private String habitacionTipo;
    private Long pisoId;
    private List<Long> servicioIds;


    public ReservaRequestDTO() {

    }

    public ReservaRequestDTO(LocalDate fechaIngreso, LocalDate fechaSalida, Long clienteId, Long habitacionId, String habitacionTipo, Long pisoId, List<Long> servicioIds) {
        this.fechaIngreso = fechaIngreso;
        this.fechaSalida = fechaSalida;
        this.clienteId = clienteId;
        this.habitacionId = habitacionId;
        this.habitacionTipo = habitacionTipo;
        this.pisoId = pisoId;
        this.servicioIds = servicioIds;
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

    public Long getClienteId() {
        return clienteId;
    }

    public void setClienteId(Long clienteId) {
        this.clienteId = clienteId;
    }

    public Long getHabitacionId() {
        return habitacionId;
    }

    public void setHabitacionId(Long habitacionId) {
        this.habitacionId = habitacionId;
    }

    public String getHabitacionTipo() {
        return habitacionTipo;
    }

    public void setHabitacionTipo(String habitacionTipo) {
        this.habitacionTipo = habitacionTipo;
    }

    public Long getPisoId() {
        return pisoId;
    }

    public void setPisoId(Long pisoId) {
        this.pisoId = pisoId;
    }

    public List<Long> getServicioIds() {
        return servicioIds;
    }

    public void setServicioIds(List<Long> servicioIds) {
        this.servicioIds = servicioIds;
    }
}
