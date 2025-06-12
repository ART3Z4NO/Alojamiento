package com.example.ProyectoF.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.ProyectoF.model.Reserva;

public interface ReservaRepository extends JpaRepository<Reserva, Long> {}

