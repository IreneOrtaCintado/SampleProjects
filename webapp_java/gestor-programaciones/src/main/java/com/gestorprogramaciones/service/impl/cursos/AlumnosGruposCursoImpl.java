package com.gestorprogramaciones.service.impl.cursos;

import com.gestorprogramaciones.models.cursos.AlumnosGruposCurso;
import com.gestorprogramaciones.models.cursos.Cursos;
import com.gestorprogramaciones.models.grupos.Grupos;
import com.gestorprogramaciones.models.usuarios.Alumnos;
import com.gestorprogramaciones.repositories.cursos.AlumnosGruposCursoRepository;
import com.gestorprogramaciones.service.api.cursos.AlumnosGruposCursoAPI;
import com.gestorprogramaciones.service.impl.GenericServiceImpl;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

@Service
public class AlumnosGruposCursoImpl extends GenericServiceImpl<AlumnosGruposCurso, Long>
        implements AlumnosGruposCursoAPI {

    @Autowired
    private AlumnosGruposCursoRepository AlumnosGruposCursoRepository;

    @Override
    public JpaRepository<AlumnosGruposCurso, Long> getRespository() {
        return AlumnosGruposCursoRepository;
    }

    /**
     * Devuelve una lista con los diferentes cursos de un grupo.
     * 
     * @param group
     * @return
     */
    public List<Cursos> findGroupCoursesByGroup(Grupos group) {
        Set<Cursos> groupCourses = new HashSet<Cursos>();
        List<AlumnosGruposCurso> allAGC = this.findAll();
        if (group != null) {
            for (AlumnosGruposCurso agc : allAGC) {
                if (group.getId_grupo() == agc.getGrupo().getId_grupo()) {
                    groupCourses.add(agc.getCurso());
                }
            }
        }
        return new ArrayList<Cursos>(groupCourses);
    }

    /**
     * Devuelve una lista con los alumnos pertenecientes a un grupo y a un curso.
     * 
     * @param curso
     * @param grupo
     * @return
     */
    public List<Alumnos> getAlumnosGrupoCurso(Cursos curso, Grupos grupo) {
        List<Alumnos> alumnos = new ArrayList<Alumnos>();
        if (curso != null && grupo != null) {
            for (AlumnosGruposCurso agc : this.findAll()) {
                if (agc.getCurso().getId_curso() == curso.getId_curso()
                        && agc.getGrupo().getId_grupo() == grupo.getId_grupo()) {
                    alumnos.add(agc.getAlumno());
                }
            }
        }
        return alumnos;
    }

    /**
     * Devuelve el registro de un alumno en un grupo y un curso.
     * 
     * @param alumno
     * @param grupo
     * @param curso
     * @return
     */
    public AlumnosGruposCurso findAlumnoGrupoCurso(Alumnos alumno, Grupos grupo, Cursos curso) {
        if (alumno.getId_alumno() != null && grupo.getId_grupo() != null && curso.getId_curso() != null) {
            for (AlumnosGruposCurso agc : this.findAll()) {
                if (alumno.getId_alumno() == agc.getAlumno().getId_alumno()
                        && grupo.getId_grupo() == agc.getGrupo().getId_grupo()
                        && curso.getId_curso() == agc.getCurso().getId_curso()) {
                    return agc;
                }
            }
        }
        return null;
    }

    /**
     * Devuelve una lista contodos los registros en cursos y grupos del alumno
     * pasado por parámetro.
     * 
     * @param alumnos
     * @return
     */
    public List<AlumnosGruposCurso> getGruposCursosFromAlumno(Alumnos alumnos) {
        List<AlumnosGruposCurso> registros = new ArrayList<AlumnosGruposCurso>();
        if (alumnos.getId_alumno() != null) {
            for (AlumnosGruposCurso agc : this.findAll()) {
                if (agc.getAlumno().getId_alumno() == alumnos.getId_alumno()) {
                    registros.add(agc);
                }
            }
        }
        return registros;
    }
}