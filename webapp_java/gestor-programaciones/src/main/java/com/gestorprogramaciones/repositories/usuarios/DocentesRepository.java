package com.gestorprogramaciones.repositories.usuarios;

import com.gestorprogramaciones.models.usuarios.Docentes;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DocentesRepository extends JpaRepository<Docentes, Long> {
}
