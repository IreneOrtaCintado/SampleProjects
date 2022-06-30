package com.gestorprogramaciones.models.planes;

import com.gestorprogramaciones.models.cursos.Ufs;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "planUfs")
public class PlanUfs {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id_planUf;

    private String nombre;
    private String cod_planUf;
    private int horas_planUf;

    @ManyToOne
    @JoinColumn(name="id_planAsignatura")
    private PlanAsignaturas planAsignatura;

    @OneToMany(mappedBy="planUf")
    private Set<PlanRas> planRas = new HashSet<>();

    @OneToMany(mappedBy="planUf")
    private Set<Ufs> ufs = new HashSet<>();

    public PlanUfs() {
    }

    public PlanUfs(String nombre, String cod_planUf, int horas_planUf, PlanAsignaturas planAsignatura) {
        this.nombre = nombre;
        this.cod_planUf = cod_planUf;
        this.planAsignatura = planAsignatura;
        this.horas_planUf = horas_planUf;
    }

    public Long getId_planUf() {
        return id_planUf;
    }

    public void setId_planUf(Long id_planUf) {
        this.id_planUf = id_planUf;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getCod_planUf() {
        return cod_planUf;
    }

    public void setCod_planUf(String cod_planUf) {
        this.cod_planUf = cod_planUf;
    }

    public PlanAsignaturas getPlanAsignatura() {
        return planAsignatura;
    }

    public void setPlanAsignatura(PlanAsignaturas planAsignatura) {
        this.planAsignatura = planAsignatura;
    }

    
    public int getHoras_planUf() {
        return horas_planUf;
    }

    public void setHoras_planUf(int horas_planUf) {
        this.horas_planUf = horas_planUf;
    }

    public Set<PlanRas> getPlanRas() {
        return planRas;
    }

    public void setPlanRas(Set<PlanRas> planRas) {
        this.planRas = planRas;
    }

    public Set<Ufs> getUfs() {
        return ufs;
    }

    public void setUfs(Set<Ufs> ufs) {
        this.ufs = ufs;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        PlanUfs planUfs = (PlanUfs) o;

        return id_planUf != null ? id_planUf.equals(planUfs.id_planUf) : planUfs.id_planUf == null;
    }

    @Override
    public int hashCode() {
        return id_planUf != null ? id_planUf.hashCode() : 0;
    }

     @Override
     public String toString() {
         return "PlanUfs{" +
                 "id_planUf=" + id_planUf +
                 ", uf='" + nombre + '\'' +
                 ", cod_planUf='" + cod_planUf + '\'' +
                 ", planAsignatura=" + planAsignatura +
                 '}';
     }
}
