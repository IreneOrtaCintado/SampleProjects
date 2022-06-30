package com.gestorprogramaciones.service.api.usuarios;

import com.gestorprogramaciones.models.usuarios.Alumnos;
import com.gestorprogramaciones.service.api.GenericServiceAPI;

public interface AlumnosAPI extends GenericServiceAPI<Alumnos, Long>{
    
    boolean uniqueUserNameAlumno(String userName);

    Alumnos validateDniAlumno(Alumnos alumno);

    public Alumnos findAlumnoByDni(String dni);
}