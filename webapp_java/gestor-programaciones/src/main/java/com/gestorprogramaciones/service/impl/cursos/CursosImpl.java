package com.gestorprogramaciones.service.impl.cursos;

import com.gestorprogramaciones.models.cursos.Cursos;
import com.gestorprogramaciones.models.planes.PlanAsignaturas;
import com.gestorprogramaciones.models.tablasaux.Centros;
import com.gestorprogramaciones.models.usuarios.Docentes;
import com.gestorprogramaciones.repositories.cursos.CursosRepository;
import com.gestorprogramaciones.service.api.cursos.CursosAPI;
import com.gestorprogramaciones.service.api.planes.PlanAsignaturasAPI;
import com.gestorprogramaciones.service.api.tablasaux.CentrosAPI;
import com.gestorprogramaciones.service.impl.GenericServiceImpl;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

@Service
public class CursosImpl extends GenericServiceImpl<Cursos, Long> implements CursosAPI {

    @Autowired
    private CursosRepository cursosRepository;
    @Autowired
    private PlanAsignaturasAPI planAsignaturasAPI;
    @Autowired
    private CentrosAPI centrosAPI;

    @Override
    public JpaRepository<Cursos, Long> getRespository() {
        return cursosRepository;
    }

    /**
     * Busca un curso en la base de datos y lo devuelve.
     * 
     * @param curso
     * @return
     */
    public Cursos findCurso(Cursos curso) {
        Cursos cursoFound = null;
        if (curso.getId_curso() != null && curso.getId_curso() != -1)
            cursoFound = this.findById(curso.getId_curso());
        return cursoFound;
    }

    /**
     * Devuelve una lista de los cursos del docente pasado por parámetro.
     */
    public List<Cursos> buscarCursosDocente(Docentes docente) {
        List<Cursos> cursosDocente = new ArrayList<Cursos>();
        for (Cursos c : this.findAll()) {
            if (c.getDocente().getId_docente() == docente.getId_docente())
                cursosDocente.add(c);
        }
        return cursosDocente;
    }

    /**
     * Devuelve los cursos de un docente para un año y centro concretos.
     * 
     * @param cursosDocente
     * @param cursoCentro
     * @param cursoAnyo
     * @return
     */
    public List<Cursos> buscarCursosCentroAnyo(List<Cursos> cursosDocente, Centros cursoCentro, String cursoAnyo) {
        List<Cursos> modulosFiltrados = new ArrayList<Cursos>();
        for (Cursos unCurso : cursosDocente) {
            if ((unCurso.getCentro().getId_centro() == cursoCentro.getId_centro())
                    && (unCurso.getAnyo().equals(cursoAnyo)))
                modulosFiltrados.add(unCurso);
        }
        return modulosFiltrados;
    }

    /**
     * Devuelve todos los cursos para un año y centro concretos.
     */
    public List<Cursos> buscarCursosCentroAnyo(Centros cursoCentro, String cursoAnyo) {
        List<Cursos> modulosFiltrados = new ArrayList<Cursos>();
        for (Cursos unCurso : this.findAll()) {
            if ((unCurso.getCentro().getId_centro() == cursoCentro.getId_centro())
                    && (unCurso.getAnyo().equals(cursoAnyo)))
                modulosFiltrados.add(unCurso);
        }
        return modulosFiltrados;
    }

    /**
     * Devuelve una lista de los cursos del docente pasado por parámetro, eliminando
     * aquellos con una combinación de año y centro que ya existe en la lista.
     * 
     * @param cursosDocente
     * @return
     */
    public List<Cursos> agruparCursosCentroAnyo(List<Cursos> cursosDocente) {
        List<Cursos> cursosAgrupados = cursosDocente.stream()
                .collect(Collectors.groupingBy(c -> c.getCentro() + c.getAnyo()))
                .values()
                .stream()
                .flatMap(group -> group.stream().limit(1))
                .collect(Collectors.toList());
        return cursosAgrupados;
    }

    /**
     * O.o
     * 
     * @return
     */
    public List<Cursos> agruparCursosCentroAnyo() {
        List<Cursos> cursosAgrupados = this.findAll().stream()
                .collect(Collectors.groupingBy(c -> c.getCentro() + c.getAnyo()))
                .values()
                .stream()
                .flatMap(group -> group.stream().limit(1))
                .collect(Collectors.toList());
        return cursosAgrupados;
    }

    /**
     * Añade el docente, el centro y el plan asignatura a un curso.
     * 
     * @param newCurso
     * @return
     */
    public Cursos addDataToNewCourse(Cursos newCurso, Docentes docente) {
        // docente
        newCurso.setDocente(docente);
        // centro
        Long idCentroCurso = newCurso.getCentro().getId_centro();
        Centros centroCurso = centrosAPI.addCentro(idCentroCurso, newCurso.getCentro().getNombre(),
                newCurso.getCentro().getMail());
        newCurso.setCentro(centroCurso);
        // plan asignatura
        PlanAsignaturas planAsignaturaCurso = planAsignaturasAPI
                .findById(newCurso.getPlanAsignatura().getId_planAsignatura());
        newCurso.setPlanAsignatura(planAsignaturaCurso);

        return newCurso;
    }

    /**
     * Actualiza todos los datos de un objeto curso, excepto su lista de Ufs.
     * 
     * @param curso
     * @param nuevosDatosCurso
     * @return
     */
    public Cursos updateDataFromCurso(Cursos curso, Cursos nuevosDatosCurso) {
        // año y horas
        curso.setAnyo(nuevosDatosCurso.getAnyo());
        curso.setHoras_curso(nuevosDatosCurso.getHoras_curso());
        // centro
        Centros centroCurso = centrosAPI.addCentro(
                nuevosDatosCurso.getCentro().getId_centro(),
                nuevosDatosCurso.getCentro().getNombre(),
                nuevosDatosCurso.getCentro().getMail());
        curso.setCentro(centroCurso);
        // plan asignatura
        PlanAsignaturas planAsignaturaCurso = planAsignaturasAPI
                .findById(nuevosDatosCurso.getPlanAsignatura().getId_planAsignatura());
        curso.setPlanAsignatura(planAsignaturaCurso);

        return curso;
    }
}