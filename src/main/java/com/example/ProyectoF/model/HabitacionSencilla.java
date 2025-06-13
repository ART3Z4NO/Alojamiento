package com.example.SegundoParcial.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
// ELIMINAR: import lombok.Getter;
// ELIMINAR: import lombok.Setter;
import jakarta.persistence.DiscriminatorValue; // Asegúrate de tener esta importación

@Entity
@DiscriminatorValue("sencilla") // ¡Asegúrate de que esta línea esté presente y el valor sea correcto!

public class HabitacionSencilla extends Habitacion {

    @Column(nullable = false)
    private boolean cama = true;

    @Column(nullable = false)
    private boolean television = true;


    public HabitacionSencilla() {
        // Constructor vacío explícito
    }


    public HabitacionSencilla(boolean cama, boolean television) {
        this.cama = cama;
        this.television = television;
    }

    public boolean isCama() {
        return cama;
    }

    public void setCama(boolean cama) {
        this.cama = cama;
    }

    public boolean isTelevision() {
        return television;
    }

    public void setTelevision(boolean television) {
        this.television = television;
    }
}
