package com.gestorprogramaciones.models.cursos;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "tiposActividades")
public class TiposActividades {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id_tipo_act;

    private String nombre;

    @OneToMany(mappedBy="tipoActividad")
    private Set<Actividades> actividades = new HashSet<>();

    public TiposActividades() {
    }

    public TiposActividades(String nombre) {
        this.nombre = nombre;
    }

    public Long getId_tipo_act() {
        return id_tipo_act;
    }

    public void setId_tipo_act(Long id_tipo_act) {
        this.id_tipo_act = id_tipo_act;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Set<Actividades> getActividades() {
        return actividades;
    }

    public void setActividades(Set<Actividades> actividades) {
        this.actividades = actividades;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        TiposActividades that = (TiposActividades) o;

        return id_tipo_act != null ? id_tipo_act.equals(that.id_tipo_act) : that.id_tipo_act == null;
    }

    @Override
    public int hashCode() {
        return id_tipo_act != null ? id_tipo_act.hashCode() : 0;
    }

    @Override
    public String toString() {
        return "TiposActividades{" +
                "id_tipo_act=" + id_tipo_act +
                ", tipoAct='" + nombre + '\'' +
                ", actividades=" + actividades +
                '}';
    }
}
