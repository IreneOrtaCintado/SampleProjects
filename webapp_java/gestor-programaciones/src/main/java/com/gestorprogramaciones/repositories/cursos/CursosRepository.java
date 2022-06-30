package com.gestorprogramaciones.repositories.cursos;

import com.gestorprogramaciones.models.cursos.Cursos;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CursosRepository extends JpaRepository<Cursos, Long> {
}
