package com.gestorprogramaciones.models.tablasaux;

import com.gestorprogramaciones.models.usuarios.Alumnos;
import com.gestorprogramaciones.models.usuarios.Docentes;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "idiomas")
public class Idiomas {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id_idioma;
    private String nombre;

    @OneToMany(mappedBy="idioma")
    private Set<Docentes> docentes = new HashSet<>();

    @OneToMany(mappedBy="idioma")
    private Set<Alumnos> alumnos = new HashSet<>();

    public Idiomas() {
    }

    public Idiomas(String nombre) {
        this.nombre = nombre;
    }

    public Long getId_idioma() {
        return id_idioma;
    }

    public void setId_idioma(Long id_idioma) {
        this.id_idioma = id_idioma;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Set<Docentes> getDocentes() {
        return docentes;
    }

    public void setDocentes(Set<Docentes> docentes) {
        this.docentes = docentes;
    }

    public Set<Alumnos> getAlumnos() {
        return alumnos;
    }

    public void setAlumnos(Set<Alumnos> alumnos) {
        this.alumnos = alumnos;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Idiomas idiomas = (Idiomas) o;

        return id_idioma != null ? id_idioma.equals(idiomas.id_idioma) : idiomas.id_idioma == null;
    }

    @Override
    public int hashCode() {
        return id_idioma != null ? id_idioma.hashCode() : 0;
    }

    @Override
    public String toString() {
        return "Idiomas{" +
                "id_idioma=" + id_idioma +
                ", idioma='" + nombre + '\'' +
                ", docentes=" + docentes +
                ", alumnos=" + alumnos +
                '}';
    }
}
