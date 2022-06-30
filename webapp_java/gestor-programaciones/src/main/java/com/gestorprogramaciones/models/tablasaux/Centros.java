package com.gestorprogramaciones.models.tablasaux;

import com.gestorprogramaciones.models.cursos.Cursos;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "centros")
public class Centros {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id_centro;

    private String nombre;
    private String mail;

    @OneToMany(mappedBy="centro")
    private Set<Cursos> cursos = new HashSet<>();

    public Centros() {
    }

    public Centros(String nombre, String mail) {
        this.nombre = nombre;
        this.mail = mail;
    }

    public Long getId_centro() {
        return id_centro;
    }

    public void setId_centro(Long id_centro) {
        this.id_centro = id_centro;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public Set<Cursos> getCursos() {
        return cursos;
    }

    public void setCursos(Set<Cursos> cursos) {
        this.cursos = cursos;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Centros centros = (Centros) o;

        return id_centro != null ? id_centro.equals(centros.id_centro) : centros.id_centro == null;
    }

    @Override
    public int hashCode() {
        return id_centro != null ? id_centro.hashCode() : 0;
    }

    @Override
    public String toString() {
        return "Centros{" +
                "id_centro=" + id_centro +
                ", centro='" + nombre + '\'' +
                ", mail='" + mail + '\'' +
                '}';
    }
}
