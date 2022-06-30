package com.gestorprogramaciones.models.usuarios;

import com.gestorprogramaciones.models.cursos.Cursos;
import com.gestorprogramaciones.models.tablasaux.Idiomas;

import javax.persistence.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "docentes")
public class Docentes {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id_docente;
    private String nombre;
    private String dni_docente;
    private String user_docente;
    private String pass_docente;

    @ManyToOne
    @JoinColumn(name="id_idioma")
    private Idiomas idioma;

    @OneToMany(mappedBy="docente")
    private Set<Cursos> cursos = new HashSet<>();

    public Docentes() {
    }

    public Docentes(String user_docente, String pass_docente) {
        this.user_docente = user_docente;
        this.pass_docente = pass_docente;
    }

    public Docentes(String nombre, String dni_docente, String user_docente, String pass_docente, Idiomas idioma) {
        this.nombre = nombre;
        this.dni_docente = dni_docente;
        this.user_docente = user_docente;
        this.pass_docente = pass_docente;
        this.idioma = idioma;
    }

    public Long getId_docente() {
        return id_docente;
    }

    public void setId_docente(Long id_docente) {
        this.id_docente = id_docente;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDni_docente() {
        return dni_docente;
    }

    public void setDni_docente(String dni_docente) {
        this.dni_docente = dni_docente;
    }

    public String getUser_docente() {
        return user_docente;
    }

    public void setUser_docente(String user_docente) {
        this.user_docente = user_docente;
    }

    public String getPass_docente() {
        return pass_docente;
    }

    public void setPass_docente(String pass_docente) {
        this.pass_docente = pass_docente;
    }

    public Idiomas getIdioma() {
        return idioma;
    }

    public void setIdioma(Idiomas idioma) {
        this.idioma = idioma;
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

        Docentes docentes = (Docentes) o;

        return id_docente != null ? id_docente.equals(docentes.id_docente) : docentes.id_docente == null;
    }

    @Override
    public int hashCode() {
        return id_docente != null ? id_docente.hashCode() : 0;
    }

    @Override
    public String toString() {
        return "Docentes{" +
                "id_docente=" + id_docente +
                ", docente='" + nombre + '\'' +
                ", dni_docente='" + dni_docente + '\'' +
                ", user_docente='" + user_docente + '\'' +
                ", idioma=" + idioma +
                ", cursos=" + cursos +
                '}';
    }  
	
}
