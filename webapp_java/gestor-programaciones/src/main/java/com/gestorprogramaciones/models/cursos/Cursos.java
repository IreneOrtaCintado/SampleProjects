package com.gestorprogramaciones.models.cursos;

import com.gestorprogramaciones.models.planes.PlanAsignaturas;
import com.gestorprogramaciones.models.tablasaux.Centros;
import com.gestorprogramaciones.models.usuarios.Docentes;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "cursos")
public class Cursos {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id_curso;

    private String anyo;
    private int horas_curso;

    @ManyToOne
    @JoinColumn(name = "id_planAsignatura")
    private PlanAsignaturas planAsignatura;

    @ManyToOne
    @JoinColumn(name = "id_centro")
    private Centros centro;

    @ManyToOne
    @JoinColumn(name = "id_docente")
    private Docentes docente;

    @OneToMany(mappedBy = "curso")
    private Set<Ufs> ufs = new HashSet<>();

    @OneToMany(mappedBy = "curso")
    Set<AlumnosGruposCurso> alumnosGruposCursos = new HashSet<>();

    public Cursos() {
    }

    public Cursos(String anyo, int horas_curso, PlanAsignaturas planAsignatura, Centros centro, Docentes docente) {
        this.anyo = anyo;
        this.horas_curso = horas_curso;
        this.planAsignatura = planAsignatura;
        this.centro = centro;
        this.docente = docente;
    }

    public Long getId_curso() {
        return id_curso;
    }

    public void setId_curso(Long id_curso) {
        this.id_curso = id_curso;
    }

    public String getAnyo() {
        return anyo;
    }

    public void setAnyo(String anyo) {
        this.anyo = anyo;
    }

    public int getHoras_curso() {
        return horas_curso;
    }

    public void setHoras_curso(int horas_curso) {
        this.horas_curso = horas_curso;
    }

    public PlanAsignaturas getPlanAsignatura() {
        return planAsignatura;
    }

    public void setPlanAsignatura(PlanAsignaturas planAsignatura) {
        this.planAsignatura = planAsignatura;
    }

    public Centros getCentro() {
        return centro;
    }

    public void setCentro(Centros centro) {
        this.centro = centro;
    }

    public Docentes getDocente() {
        return docente;
    }

    public void setDocente(Docentes docente) {
        this.docente = docente;
    }

    public Set<Ufs> getUfs() {
        return ufs;
    }

    public void setUfs(Set<Ufs> ufs) {
        this.ufs = ufs;
    }

    public Set<AlumnosGruposCurso> getAlumnosGruposCursos() {
        return alumnosGruposCursos;
    }

    public void setAlumnosGruposCursos(Set<AlumnosGruposCurso> alumnosGruposCursos) {
        this.alumnosGruposCursos = alumnosGruposCursos;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;

        Cursos cursos = (Cursos) o;

        return id_curso != null ? id_curso.equals(cursos.id_curso) : cursos.id_curso == null;
    }

    @Override
    public int hashCode() {
        return id_curso != null ? id_curso.hashCode() : 0;
    }

    @Override
    public String toString() {
        return "Cursos{" +
                "id_curso=" + id_curso +
                ", anyo='" + anyo + '\'' +
                ", horas_curso=" + horas_curso +
                ", planAsignaturas=" + planAsignatura +
                ", centro=" + centro +
                ", docente=" + docente +
                ", ufs=" + ufs +
                ", alumnosGruposCursos=" + alumnosGruposCursos +
                '}';
    }
}
