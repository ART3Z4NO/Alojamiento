package com.example.ProyectoF.model;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;

import java.util.List;

@Entity

@JsonIdentityInfo(
        generator = ObjectIdGenerators.PropertyGenerator.class,
        property = "id"
)
public class Piso {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private int numero;

    @OneToMany(mappedBy = "piso", cascade = CascadeType.ALL)
    private List<Habitacion> habitaciones;

    // CONSTRUCTOR VACÍO (¡NECESARIO PARA JPA Y SERIALIZACIÓN!)
    public Piso() {

    }

    public Piso(Long id, int numero, List<Habitacion> habitaciones) {
        this.id = id;
        this.numero = numero;
        this.habitaciones = habitaciones;
    }

    // Getters y Setters MANUALES (estos ya los tenías)
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getNumero() {
        return numero;
    }

    public void setNumero(int numero) {
        this.numero = numero;
    }

    public List<Habitacion> getHabitaciones() {
        return habitaciones;
    }

    public void setHabitaciones(List<Habitacion> habitaciones) {
        this.habitaciones = habitaciones;
    }
}
