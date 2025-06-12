package com.example.ProyectoF.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;

import jakarta.persistence.DiscriminatorValue; // Asegúrate de tener esta importación

@Entity
@DiscriminatorValue("lujosa") // ¡Asegúrate de que esta línea esté presente y el valor sea correcto!

public class HabitacionLujosa extends Habitacion {

    @Column(nullable = false)
    private boolean cama = true;

    @Column(nullable = false)
    private boolean television = true;

    @Column(name = "bano_privado", nullable = false)
    private boolean bañoPrivado = true;

    @Column(nullable = false)
    private boolean wifi = true;


    public HabitacionLujosa() {
        // Constructor vacío explícito
    }


    public HabitacionLujosa(boolean cama, boolean television, boolean bañoPrivado, boolean wifi) {
        this.cama = cama;
        this.television = television;
        this.bañoPrivado = bañoPrivado;
        this.wifi = wifi;
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

    public boolean isBañoPrivado() {
        return bañoPrivado;
    }

    public void setBañoPrivado(boolean bañoPrivado) {
        this.bañoPrivado = bañoPrivado;
    }

    public boolean isWifi() {
        return wifi;
    }

    public void setWifi(boolean wifi) {
        this.wifi = wifi;
    }
}
