package com.gestorprogramaciones.models.cursos;

import com.gestorprogramaciones.models.planes.PlanUfs;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;

import org.springframework.format.annotation.DateTimeFormat;

@Entity
@Table(name = "ufs")
public class Ufs {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id_uf;

    private int horas_uf;

    @Temporal(TemporalType.DATE)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date inicio_uf;

    @Temporal(TemporalType.DATE)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date fin_uf;

    @ManyToOne
    @JoinColumn(name = "id_planUf")
    private PlanUfs planUf;

    @ManyToOne
    @JoinColumn(name = "id_curso")
    private Cursos curso;

    @OneToMany(mappedBy = "uf", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Ras> ras = new HashSet<>();

    @OneToMany(mappedBy = "uf", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Actividades> actividades = new HashSet<>();

    public Ufs() {
    }

    public Ufs(int horas_uf, Date inicio_uf, Date fin_uf, PlanUfs planUf, Cursos curso) {
        this.horas_uf = horas_uf;
        this.inicio_uf = inicio_uf;
        this.fin_uf = fin_uf;
        this.planUf = planUf;
        this.curso = curso;
    }

    public Long getId_uf() {
        return id_uf;
    }

    public void setId_uf(Long id_uf) {
        this.id_uf = id_uf;
    }

    public int getHoras_uf() {
        return horas_uf;
    }

    public void setHoras_uf(int horas_uf) {
        this.horas_uf = horas_uf;
    }

    public Date getInicio_uf() {
        return inicio_uf;
    }

    public void setInicio_uf(Date inicio_uf) {
        this.inicio_uf = inicio_uf;
    }

    public Date getFin_uf() {
        return fin_uf;
    }

    public void setFin_uf(Date fin_uf) {
        this.fin_uf = fin_uf;
    }

    public PlanUfs getPlanUf() {
        return planUf;
    }

    public void setPlanUf(PlanUfs planUf) {
        this.planUf = planUf;
    }

    public Cursos getCurso() {
        return curso;
    }

    public void setCurso(Cursos curso) {
        this.curso = curso;
    }

    public Set<Ras> getRas() {
        return ras;
    }

    public void setRas(Set<Ras> ras) {
        this.ras = ras;
    }

    public Set<Actividades> getActividades() {
        return actividades;
    }

    public void setActividades(Set<Actividades> actividades) {
        this.actividades = actividades;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;

        Ufs ufs = (Ufs) o;

        return id_uf != null ? id_uf.equals(ufs.id_uf) : ufs.id_uf == null;
    }

    @Override
    public int hashCode() {
        return id_uf != null ? id_uf.hashCode() : 0;
    }

    @Override
    public String toString() {
        return "Ufs{" +
                "id_uf=" + id_uf +
                ", horas_uf=" + horas_uf +
                ", inicio_uf=" + inicio_uf +
                ", fin_uf=" + fin_uf +
                ", planUf=" + planUf +
                ", curso=" + curso +
                ", ras=" + ras +
                ", actividades=" + actividades +
                '}';
    }
}
