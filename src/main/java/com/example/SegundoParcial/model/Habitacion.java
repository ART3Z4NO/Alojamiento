package com.example.SegundoParcial.model;

import jakarta.persistence.*;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Transient;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties; // Importar esta anotación

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "tipo")
@JsonSubTypes({
        @JsonSubTypes.Type(value = Habitacion.class,  name = "Habitacion")

})
@Inheritance(strategy = InheritanceType.JOINED)
@Entity
@DiscriminatorColumn(name = "tipo")

@JsonIgnoreProperties(value = {"hibernateLazyInitializer", "handler"}, ignoreUnknown = true)
public abstract class Habitacion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String numero;
    private boolean disponible = true;
    private double precio;

    @ManyToOne
    @JoinColumn(name = "piso_id")
    private Piso piso;

    public Habitacion() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getNumero() { return numero; }
    public void setNumero(String numero) { this.numero = numero; }
    public boolean isDisponible() { return disponible; }
    public void setDisponible(boolean disponible) { this.disponible = disponible; }
    public double getPrecio() { return precio; }
    public void setPrecio(double precio) { this.precio = precio; }
    public Piso getPiso() { return piso; }
    public void setPiso(Piso piso) { this.piso = piso; }

    @Transient
    public String getTipo() {
        DiscriminatorValue value = this.getClass().getAnnotation(DiscriminatorValue.class);
        if (value != null) {
            return value.value();
        }
        String className = this.getClass().getSimpleName();
        if (className.startsWith("Habitacion")) {
            return className.substring("Habitacion".length()).toLowerCase();
        }
        return null;
    }
}
