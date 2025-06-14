// src/main/java/com/example/SegundoParcial/dto/ServicioResponseDTO.java
package com.example.SegundoParcial.dto;


public class ServicioResponseDTO {
    private Long id;
    private String nombre;
    private double precio;

    public ServicioResponseDTO() {
        // Constructor vacío explícito
    }

    public ServicioResponseDTO(Long id, String nombre, double precio) {
        this.id = id;
        this.nombre = nombre;
        this.precio = precio;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public double getPrecio() {
        return precio;
    }

    public void setPrecio(double precio) {
        this.precio = precio;
    }
}
