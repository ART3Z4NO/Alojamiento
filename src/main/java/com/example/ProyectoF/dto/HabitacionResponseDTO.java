// src/main/java/com/example/ProyectoF/dto/HabitacionResponseDTO.java
package com.example.ProyectoF.dto;


public class HabitacionResponseDTO {
    private Long id;
    private String numero;
    private double precio;
    private boolean disponible;
    private String tipo;        //
    private PisoResponseDTO piso;


    public HabitacionResponseDTO() {
        // Constructor vacío explícito
    }


    public HabitacionResponseDTO(Long id, String numero, double precio, boolean disponible, String tipo, PisoResponseDTO piso) {
        this.id = id;
        this.numero = numero;
        this.precio = precio;
        this.disponible = disponible;
        this.tipo = tipo;
        this.piso = piso;
    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public double getPrecio() {
        return precio;
    }

    public void setPrecio(double precio) {
        this.precio = precio;
    }

    public boolean isDisponible() {
        return disponible;
    }

    public void setDisponible(boolean disponible) {
        this.disponible = disponible;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public PisoResponseDTO getPiso() {
        return piso;
    }

    public void setPiso(PisoResponseDTO piso) {
        this.piso = piso;
    }
}
