// src/main/java/com/example/ProyectoF/dto/PisoResponseDTO.java
package com.example.ProyectoF.dto;


public class PisoResponseDTO {
    private Long id;
    private int numero;



    public PisoResponseDTO() {
        // Constructor vacío explícito
    }


    public PisoResponseDTO(Long id, int numero) {
        this.id = id;
        this.numero = numero;
    }


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
}
