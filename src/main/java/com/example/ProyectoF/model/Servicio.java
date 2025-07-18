package com.example.ProyectoF.model;

import jakarta.persistence.*;
// ELIMINAR: import lombok.Getter;
// ELIMINAR: import lombok.Setter;

import java.util.List;

@Entity

public class Servicio {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nombre;
    private double precio;


    public Servicio() {
        // Constructor vacío explícito
    }


    public Servicio(Long id, String nombre, double precio) {
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
