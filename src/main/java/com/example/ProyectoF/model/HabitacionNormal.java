package com.example.SegundoParcial.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
// ELIMINAR: import lombok.Getter;
// ELIMINAR: import lombok.Setter;
import jakarta.persistence.DiscriminatorValue; // Asegúrate de tener esta importación

@Entity
@DiscriminatorValue("normal") // Asegúrate de que esta línea esté presente

public class HabitacionNormal extends Habitacion {

    @Column(nullable = false)
    private boolean television = true;

    @Column(name = "bano_privado", nullable = false)
    private boolean bañoPrivado = true;

    public HabitacionNormal() {
        // Constructor vacío explícito
    }


    public HabitacionNormal(boolean television, boolean bañoPrivado) {
        this.television = television;
        this.bañoPrivado = bañoPrivado;
    }


    public boolean isTelevision() {
        return television;
    }

    public void setTelevision(boolean television) {
        this.television = television;
    }

    public boolean isBañoPrivado() {
        return bañoPrivado;
    }

    public void setBañoPrivado(boolean bañoPrivado) {
        this.bañoPrivado = bañoPrivado;
    }
}
