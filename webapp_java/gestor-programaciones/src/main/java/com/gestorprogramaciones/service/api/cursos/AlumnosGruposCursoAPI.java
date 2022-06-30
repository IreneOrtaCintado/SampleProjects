package com.gestorprogramaciones.service.api.cursos;

import java.util.List;

import com.gestorprogramaciones.models.cursos.AlumnosGruposCurso;
import com.gestorprogramaciones.models.cursos.Cursos;
import com.gestorprogramaciones.models.grupos.Grupos;
import com.gestorprogramaciones.models.usuarios.Alumnos;
import com.gestorprogramaciones.service.api.GenericServiceAPI;

public interface AlumnosGruposCursoAPI extends GenericServiceAPI<AlumnosGruposCurso, Long>{
    
    public List<Cursos> findGroupCoursesByGroup(Grupos group);

    public List<Alumnos> getAlumnosGrupoCurso(Cursos curso, Grupos grupo);

    public AlumnosGruposCurso findAlumnoGrupoCurso(Alumnos alumno, Grupos grupo, Cursos curso);

    public List<AlumnosGruposCurso> getGruposCursosFromAlumno(Alumnos alumnos);
}
