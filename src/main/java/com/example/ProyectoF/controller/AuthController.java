package com.example.SegundoParcial.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.example.SegundoParcial.Repository.UserRepository;
import com.example.SegundoParcial.model.User;
import com.example.SegundoParcial.util.JwtUtil;

import java.util.*;
import java.util.stream.*;
import java.util.concurrent.CopyOnWriteArrayList;

import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private UserRepository userRepository;

    // Blacklist en memoria
    private final List<String> blacklistedTokens = new CopyOnWriteArrayList<>();

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody User request) {
        Optional<User> userOpt = userRepository.findByUsername(request.getUsername());

        if (userOpt.isEmpty()) {
            return ResponseEntity.status(401).body("Usuario no encontrado.");
        }
        User user = userOpt.get();

        if (!user.getPassword().equals(request.getPassword())) {
            return ResponseEntity.status(401).body("Contraseña incorrecta.");
        }
        String token = jwtUtil.generateToken(user.getUsername()); // Generar el JWT
        return ResponseEntity.ok().body(token);
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody User request) {
        Optional<User> existing = userRepository.findByUsername(request.getUsername());

        if (existing.isPresent()) {
            return ResponseEntity.badRequest().body("El usuario ya existe.");
        }
        User newUser = new User();
        newUser.setUsername(request.getUsername());
        newUser.setPassword(request.getPassword());

        userRepository.save(newUser);
        return ResponseEntity.ok("Usuario registrado con éxito.");
    }

    @PostMapping("/logout")
    public ResponseEntity<String> logout(HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);
            blacklistedTokens.add(token);
            return ResponseEntity.ok("Logout exitoso. Token invalidado.");
        } else {
            return ResponseEntity.badRequest().body("Token no proporcionado.");
        }
    }

    public boolean isTokenBlacklisted(String token) {
        return blacklistedTokens.contains(token);
    }
}





/*
package com.example.SegundoParcial.controller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.example.SegundoParcial.Repository.UserRepository;
import com.example.SegundoParcial.model.User;
import com.example.SegundoParcial.util.JwtUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private UserRepository userRepository;


    private final List<String> blacklistedTokens = new ArrayList<>();

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody User request) {
        Optional<User> userOpt = userRepository.findByUsername(request.getUsername());

        if (userOpt.isEmpty()) {
            return ResponseEntity.status(401).body("Usuario no encontrado");
        }

        User user = userOpt.get();

        if (!user.getPassword().equals(request.getPassword())) {
            return ResponseEntity.status(401).body("Contraseña incorrecta");
        }

        String token = jwtUtil.generateToken(user.getUsername());
        return ResponseEntity.ok().body(token);
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody User request) {
        Optional<User> existing = userRepository.findByUsername(request.getUsername());
        if (existing.isPresent()) {
            return ResponseEntity.badRequest().body("El usuario ya existe");
        }

        User newUser = new User();
        newUser.setUsername(request.getUsername());
        newUser.setPassword(request.getPassword());

        userRepository.save(newUser);
        return ResponseEntity.ok("Usuario registrado con éxito");
    }

    // Logout que guarda el token en una lista
    @GetMapping("/logout")
    public ResponseEntity<String> logout(@RequestHeader("Authorization") String authHeader) {
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);
            synchronized (blacklistedTokens) {
                if (!blacklistedTokens.contains(token)) {
                    blacklistedTokens.add(token);
                }
            }
            return ResponseEntity.ok("Logout exitoso. Token invalidado.");
        } else {
            return ResponseEntity.badRequest().body("Token no proporcionado");
        }
    }


    public boolean isTokenBlacklisted(String token) {
        synchronized (blacklistedTokens) {
            return blacklistedTokens.contains(token);
        }
    }
}

 */














































