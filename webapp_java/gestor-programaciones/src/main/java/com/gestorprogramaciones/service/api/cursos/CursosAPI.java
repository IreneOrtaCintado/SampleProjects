package com.gestorprogramaciones.service.api.cursos;

import java.util.List;

import com.gestorprogramaciones.models.cursos.Cursos;
import com.gestorprogramaciones.models.tablasaux.Centros;
import com.gestorprogramaciones.models.usuarios.Docentes;
import com.gestorprogramaciones.service.api.GenericServiceAPI;

public interface CursosAPI extends GenericServiceAPI<Cursos, Long>{
    
    public Cursos findCurso(Cursos curso);

    public List<Cursos> buscarCursosDocente(Docentes docente);

    public List<Cursos> buscarCursosCentroAnyo(List<Cursos> cursosDocente, Centros cursoCentro, String cursoAnyo);
    public List<Cursos> buscarCursosCentroAnyo(Centros cursoCentro, String cursoAnyo);

    public List<Cursos> agruparCursosCentroAnyo(List<Cursos> cursosDocente);
    public List<Cursos> agruparCursosCentroAnyo();

    public Cursos addDataToNewCourse(Cursos newCurso, Docentes docente);
    public Cursos updateDataFromCurso(Cursos curso, Cursos nuevosDatosCurso);
}
