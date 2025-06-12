// src/main/java/com/example/ProyectoF/exception/ResourceNotFoundException.java
package com.example.ProyectoF.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND) // Esto automáticamente dará un 404
public class ResourceNotFoundException extends RuntimeException {
    public ResourceNotFoundException(String message) {
        super(message);
    }
}