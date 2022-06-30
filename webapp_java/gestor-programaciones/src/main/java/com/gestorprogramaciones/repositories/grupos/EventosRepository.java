package com.gestorprogramaciones.repositories.grupos;

import com.gestorprogramaciones.models.grupos.Eventos;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EventosRepository extends JpaRepository<Eventos, Long> {
}
