package com.example.ProyectoF.Repository;


import org.springframework.data.jpa.repository.JpaRepository;
import com.example.ProyectoF.model.Cliente;

public interface ClienteRepository extends JpaRepository<Cliente, Long> {}
