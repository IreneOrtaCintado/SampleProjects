package com.gestorprogramaciones.models.grupos;

import com.gestorprogramaciones.models.cursos.AlumnosGruposCurso;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "grupos")
public class Grupos {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id_grupo;
    private String nombre;

    @OneToMany(mappedBy = "grupo")
    private Set<Eventos> eventos = new HashSet<>();

    @OneToMany(mappedBy = "grupo")
    Set<AlumnosGruposCurso> alumnosGruposCursos = new HashSet<>();

    public Grupos() {
    }

    public Grupos(String nombre) {
        this.nombre = nombre;
    }

    public Long getId_grupo() {
        return id_grupo;
    }

    public void setId_grupo(Long id_grupo) {
        this.id_grupo = id_grupo;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Set<Eventos> getEventos() {
        return eventos;
    }

    public void setEventos(Set<Eventos> eventos) {
        this.eventos = eventos;
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

        Grupos grupos = (Grupos) o;

        return id_grupo != null ? id_grupo.equals(grupos.id_grupo) : grupos.id_grupo == null;
    }

    @Override
    public int hashCode() {
        return id_grupo != null ? id_grupo.hashCode() : 0;
    }

    @Override
    public String toString() {
        return "id_grupo=" + id_grupo +
                ", nombre='" + nombre
                + ", eventos=" + eventos +
                ", alumnosGruposCursos=" + alumnosGruposCursos;
    }
}
