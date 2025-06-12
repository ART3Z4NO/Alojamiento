
package com.example.ProyectoF.controller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.example.ProyectoF.Repository.UserRepository;
import com.example.ProyectoF.model.User;
import com.example.ProyectoF.util.JwtUtil;

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














































/*
import com.alojamiento.proyecto.alojamiento.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.HashSet;
import java.util.Set;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtUtil jwtUtil;

    // Lista de tokens invalidados (logout)
    private static final Set<String> blacklistedTokens = new HashSet<>();
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody AuthRequest request) {
        if (userRepository.findByUsername(request.getUsername()).isPresent()) {
            return ResponseEntity.badRequest().body("El nombre de usuario ya está en uso");
        }

        User newUser = new User();
        newUser.setUsername(request.getUsername());
// ⚠️ Importante: usa un encoder si tienes configurado uno. Aquí es sin encriptar.
        newUser.setPassword("{noop}" + request.getPassword());
        newUser.setRole("USER");

        userRepository.save(newUser);
        return ResponseEntity.ok("Usuario registrado exitosamente");
    }
    // LOGIN
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody AuthRequest request) {
        try {
            Authentication auth = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
            );

            UserDetails userDetails = (UserDetails) auth.getPrincipal();
            String token = jwtUtil.generateToken(userDetails.getUsername());
            return ResponseEntity.ok(new AuthResponse(token));

        } catch (AuthenticationException e) {
            return ResponseEntity.status(401).body("Credenciales inválidas");
        }
    }

    // LOGOUT con invalidación de token
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

    // Verifica si el token está en lista negra
    public static boolean isTokenBlacklisted(String token) {
        synchronized (blacklistedTokens) {
            return blacklistedTokens.contains(token);
        }
    }

    // Clases internas para solicitud y respuesta
    public static class AuthRequest {
        private String username;
        private String password;
        public String getUsername() { return username; }
        public void setUsername(String username) { this.username = username; }
        public String getPassword() { return password; }
        public void setPassword(String password) { this.password = password; }
    }

    public static class AuthResponse {
        private String token;
        public AuthResponse(String token) { this.token = token; }
        public String getToken() { return token; }
    }
}*/