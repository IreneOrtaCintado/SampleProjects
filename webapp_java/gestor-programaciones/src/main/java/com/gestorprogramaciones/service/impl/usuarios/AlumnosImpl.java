package com.gestorprogramaciones.service.impl.usuarios;

import java.util.List;

import com.gestorprogramaciones.models.usuarios.Alumnos;
import com.gestorprogramaciones.repositories.usuarios.AlumnosRepository;
import com.gestorprogramaciones.service.api.usuarios.AlumnosAPI;
import com.gestorprogramaciones.service.impl.GenericServiceImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

@Service
public class AlumnosImpl extends GenericServiceImpl<Alumnos, Long> implements AlumnosAPI {

    @Autowired
    private AlumnosRepository AlumnosRepository;

    @Override
    public JpaRepository<Alumnos, Long> getRespository() {
        return AlumnosRepository;
    }

    /**
     * Devuelve un boleano indicando si el nombre de usuario pasado por parámetro ya
     * está siendo usado por otro docente.
     */
    @Override
    public boolean uniqueUserNameAlumno(String userName) {

        List<Alumnos> alumnos = this.findAll();

        for (Alumnos a : alumnos) {
            if (a.getUser_alumno() != null && a.getUser_alumno().equalsIgnoreCase(userName)) {
                return false;
            }
        }
        return true;
    }

    /**
     * Comprueba que el dni del alumno está registrado en el sistema y
     * que aún no tiene un usuario.
     * 
     * Devuelve un objeto con el alumno encontrado o null si no lo encuentra.
     * 
     * @param alumno
     * @return
     */
    @Override
    public Alumnos validateDniAlumno(Alumnos alumno) {

        for (Alumnos a : this.findAll()) {
            if (a.getDni_alumno() != null) {

                if (a.getDni_alumno().equals(alumno.getDni_alumno())) {
                    if (a.getUser_alumno() == null)
                        return a;
                    else
                        return null;
                }
            }
        }
        return null;
    }

    /**
     * Busca a un alumno por el campo dni y lo devuelve.
     */
    @Override
    public Alumnos findAlumnoByDni(String dni) {
        for (Alumnos a : this.findAll()) {
            if (a.getDni_alumno() != null) {
                if (a.getDni_alumno().equals(dni)) {
                    return a;
                }
            }
        }
        return null;
    }
}