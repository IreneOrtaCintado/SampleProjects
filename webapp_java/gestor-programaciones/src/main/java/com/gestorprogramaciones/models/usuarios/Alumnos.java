package com.gestorprogramaciones.models.usuarios;

import com.gestorprogramaciones.models.cursos.AlumnosGruposCurso;
import com.gestorprogramaciones.models.evaluaciones.Evaluaciones;
import com.gestorprogramaciones.models.tablasaux.Idiomas;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "alumnos")
public class Alumnos {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id_alumno;

    private String nombre;
    private String dni_alumno;
    private String user_alumno;
    private String pass_alumno;

    @ManyToOne
    @JoinColumn(name="id_idioma")
    private Idiomas idioma;

    @OneToMany(mappedBy = "alumno")
    Set<AlumnosGruposCurso> alumnosGruposCursos = new HashSet<>();

    @OneToMany(mappedBy = "alumno")
    Set<Evaluaciones> evaluaciones = new HashSet<>();

    public Alumnos() {
    }

    public Alumnos(String nombre, String dni_alumno, String user_alumno, String pass_alumno) {
        this.nombre = nombre;
        this.dni_alumno = dni_alumno;
        this.user_alumno = user_alumno;
        this.pass_alumno = pass_alumno;
    }

    public Long getId_alumno() {
        return id_alumno;
    }

    public void setId_alumno(Long id_alumno) {
        this.id_alumno = id_alumno;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDni_alumno() {
        return dni_alumno;
    }

    public void setDni_alumno(String dni_alumno) {
        this.dni_alumno = dni_alumno;
    }

    public String getUser_alumno() {
        return user_alumno;
    }

    public void setUser_alumno(String user_alumno) {
        this.user_alumno = user_alumno;
    }

    public String getPass_alumno() {
        return pass_alumno;
    }

    public void setPass_alumno(String pass_alumno) {
        this.pass_alumno = pass_alumno;
    }

    public Idiomas getIdioma() {
        return idioma;
    }

    public void setIdioma(Idiomas idioma) {
        this.idioma = idioma;
    }

    public Set<AlumnosGruposCurso> getAlumnosGruposCursos() {
        return alumnosGruposCursos;
    }

    public void setAlumnosGruposCursos(Set<AlumnosGruposCurso> alumnosGruposCursos) {
        this.alumnosGruposCursos = alumnosGruposCursos;
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

        Alumnos alumnos = (Alumnos) o;

        return id_alumno != null ? id_alumno.equals(alumnos.id_alumno) : alumnos.id_alumno == null;
    }

    @Override
    public int hashCode() {
        return id_alumno != null ? id_alumno.hashCode() : 0;
    }

    @Override
    public String toString() {
        return "Alumnos{" +
                "id_alumno=" + id_alumno +
                ", alumno='" + nombre + '\'' +
                ", dni_alumno='" + dni_alumno + '\'' +
                ", user_alumno='" + user_alumno + '\'' +
                ", idioma=" + idioma +
                ", alumnosGruposCursos=" + alumnosGruposCursos +
                ", evaluaciones=" + evaluaciones +
                '}';
    }
}
