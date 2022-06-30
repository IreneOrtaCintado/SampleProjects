package com.gestorprogramaciones.repositories.evaluaciones;

import com.gestorprogramaciones.models.evaluaciones.Evaluaciones;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EvaluacionesRepository extends JpaRepository<Evaluaciones, Long> {
}
