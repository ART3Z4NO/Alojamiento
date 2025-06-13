// src/main/java/com/example/SegundoParcial/dto/ClienteResponseDTO.java
package com.example.SegundoParcial.dto;

public class ClienteResponseDTO {
    private Long id;
    private String nombre;
    private String ci;
    private String correo;
    private String telefono;


    public ClienteResponseDTO() {

    }


    public ClienteResponseDTO(Long id, String nombre, String ci, String correo, String telefono) {
        this.id = id;
        this.nombre = nombre;
        this.ci = ci;
        this.correo = correo;
        this.telefono = telefono;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getCi() {
        return ci;
    }

    public void setCi(String ci) {
        this.ci = ci;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
