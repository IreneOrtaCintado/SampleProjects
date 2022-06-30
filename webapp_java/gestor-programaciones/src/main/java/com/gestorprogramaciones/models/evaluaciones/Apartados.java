package com.gestorprogramaciones.models.evaluaciones;

import com.gestorprogramaciones.models.cursos.Actividades;
import com.gestorprogramaciones.models.cursos.Ras;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "apartados")
public class Apartados {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id_apdo;

    private String descripcion;
    private int peso_apdo_act_ra;

    @ManyToOne
    @JoinColumn(name="id_act")
    private Actividades actividad;

    @ManyToOne
    @JoinColumn(name="id_ra")
    private Ras ra;

    @OneToMany(mappedBy = "apartado")
    Set<Evaluaciones> evaluaciones = new HashSet<>();

    public Apartados() {
    }

    public Apartados(String descripcion, int peso_apdo_act_ra, Actividades actividad, Ras ra) {
        this.descripcion = descripcion;
        this.peso_apdo_act_ra = peso_apdo_act_ra;
        this.actividad = actividad;
        this.ra = ra;
    }

    public Long getId_apdo() {
        return id_apdo;
    }

    public void setId_apdo(Long id_apdo) {
        this.id_apdo = id_apdo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public int getPeso_apdo_act_ra() {
        return peso_apdo_act_ra;
    }

    public void setPeso_apdo_act_ra(int peso_apdo_act_ra) {
        this.peso_apdo_act_ra = peso_apdo_act_ra;
    }

    public Actividades getActividad() {
        return actividad;
    }

    public void setActividad(Actividades actividad) {
        this.actividad = actividad;
    }

    public Ras getRa() {
        return ra;
    }

    public void setRa(Ras ra) {
        this.ra = ra;
    }

    public Set<Evaluaciones> getEvaluaciones() {
        return evaluaciones;
    }

    public void setEvaluaciones(Set<Evaluaciones> evaluaciones) {
        this.evaluaciones = evaluaciones;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Apartados apartados = (Apartados) o;

        return id_apdo != null ? id_apdo.equals(apartados.id_apdo) : apartados.id_apdo == null;
    }

    @Override
    public int hashCode() {
        return id_apdo != null ? id_apdo.hashCode() : 0;
    }

    @Override
    public String toString() {
        return "Apartados{" +
                "id_apdos=" + id_apdo +
                ", descripcion_apdo='" + descripcion + '\'' +
                ", peso_apdoActRa=" + peso_apdo_act_ra +
                ", actividad=" + actividad +
                ", ra=" + ra +
                ", evaluaciones=" + evaluaciones +
                '}';
    }
}
