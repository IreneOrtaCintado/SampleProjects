package com.gestorprogramaciones.models.planes;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "tiposEstudios")
public class TiposEstudios {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id_tipoEstudio;
    private String nombre;

    @OneToMany(mappedBy="tipoEstudio")
    private Set<PlanEstudios> planEstudios = new HashSet<>();

    public TiposEstudios() {
    }

    public TiposEstudios(String nombre) {
        this.nombre = nombre;
    }

    public Long getId_tipoEstudio() {
        return id_tipoEstudio;
    }

    public void setId_tipoEstudio(Long id_tipoEstudio) {
        this.id_tipoEstudio = id_tipoEstudio;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Set<PlanEstudios> getPlanEstudios() {
        return planEstudios;
    }

    public void setPlanEstudios(Set<PlanEstudios> planEstudios) {
        this.planEstudios = planEstudios;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        TiposEstudios that = (TiposEstudios) o;

        return id_tipoEstudio != null ? id_tipoEstudio.equals(that.id_tipoEstudio) : that.id_tipoEstudio == null;
    }

    @Override
    public int hashCode() {
        return id_tipoEstudio != null ? id_tipoEstudio.hashCode() : 0;
    }

    @Override
    public String toString() {
        return "TiposEstudios{" +
                "id_tipoEstudio=" + id_tipoEstudio +
                ", tipoEstudio='" + nombre + '\'' +
                '}';
    }
}
