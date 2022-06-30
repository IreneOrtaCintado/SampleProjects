package com.gestorprogramaciones.models.planes;

import com.gestorprogramaciones.models.cursos.Cursos;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "planAsignaturas")
public class PlanAsignaturas {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id_planAsignatura;

    private String nombre;
    private String cod_planAsignatura;
    private int horas_planAsignatura;

    @ManyToOne
    @JoinColumn(name = "id_planEstudio")
    private PlanEstudios planEstudio;

    @OneToMany(mappedBy = "planAsignatura")
    private Set<PlanUfs> planUfs = new HashSet<>();

    @OneToMany(mappedBy = "planAsignatura")
    private Set<Cursos> cursos = new HashSet<>();

    public PlanAsignaturas() {
    }

    public PlanAsignaturas(String nombre, String cod_planAsignatura, int horas_planAsignatura,
            PlanEstudios planEstudio) {
        this.nombre = nombre;
        this.cod_planAsignatura = cod_planAsignatura;
        this.horas_planAsignatura = horas_planAsignatura;
        this.planEstudio = planEstudio;
    }

    public Long getId_planAsignatura() {
        return id_planAsignatura;
    }

    public void setId_planAsignatura(Long id_planAsignatura) {
        this.id_planAsignatura = id_planAsignatura;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getCod_planAsignatura() {
        return cod_planAsignatura;
    }

    public void setCod_planAsignatura(String cod_planAsignatura) {
        this.cod_planAsignatura = cod_planAsignatura;
    }

    public int getHoras_planAsignatura() {
        return horas_planAsignatura;
    }

    public void setHoras_planAsignatura(int horas_planAsignatura) {
        this.horas_planAsignatura = horas_planAsignatura;
    }

    public PlanEstudios getPlanEstudio() {
        return planEstudio;
    }

    public void setPlanEstudio(PlanEstudios planEstudio) {
        this.planEstudio = planEstudio;
    }

    public Set<PlanUfs> getPlanUfs() {
        return planUfs;
    }

    public void setPlanUfs(Set<PlanUfs> planUfs) {
        this.planUfs = planUfs;
    }

    public Set<Cursos> getCursos() {
        return cursos;
    }

    public void setCursos(Set<Cursos> cursos) {
        this.cursos = cursos;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;

        PlanAsignaturas that = (PlanAsignaturas) o;

        return id_planAsignatura != null ? id_planAsignatura.equals(that.id_planAsignatura)
                : that.id_planAsignatura == null;
    }

    @Override
    public int hashCode() {
        return id_planAsignatura != null ? id_planAsignatura.hashCode() : 0;
    }

    @Override
    public String toString() {
        return "PlanAsignaturas{" +
                "id_planAsignatura=" + id_planAsignatura +
                ", asignatura='" + nombre + '\'' +
                ", cod_planAsignatura='" + cod_planAsignatura + '\'' +
                ", horas_planAsignatura=" + horas_planAsignatura +
                ", planEstudios=" + planEstudio +
                '}';
    }
}
