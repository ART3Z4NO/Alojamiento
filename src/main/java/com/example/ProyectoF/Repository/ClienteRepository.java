package com.example.SegundoParcial.Repository;


import org.springframework.data.jpa.repository.JpaRepository;
import com.example.SegundoParcial.model.Cliente;

public interface ClienteRepository extends JpaRepository<Cliente, Long> {}
