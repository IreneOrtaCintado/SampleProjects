package com.gestorprogramaciones.models.cursos;

import com.gestorprogramaciones.models.grupos.Grupos;
import com.gestorprogramaciones.models.usuarios.Alumnos;

import javax.persistence.*;
import java.io.Serializable;

@Entity
public class AlumnosGruposCurso {

    @EmbeddedId
    private AlumnosGruposCursosKey alumnosGruposCursosKey = new AlumnosGruposCursosKey();

    @ManyToOne
    @MapsId("id_alumno")
    @JoinColumn(name = "id_alumno", insertable = false, updatable = false)
    private Alumnos alumno;
    @ManyToOne
    @MapsId("id_grupo")
    @JoinColumn(name = "id_grupo", insertable = false, updatable = false)
    private Grupos grupo;
    @ManyToOne
    @MapsId("id_curso")
    @JoinColumn(name = "id_curso", insertable = false, updatable = false)
    private Cursos curso;

    public AlumnosGruposCurso() {
    }

    public AlumnosGruposCurso(Alumnos alumno, Grupos grupo, Cursos curso) {
        this.alumno = alumno;
        this.grupo = grupo;
        this.curso = curso;
    }

    public AlumnosGruposCursosKey getAlumnosGruposCursosKey() {
        return alumnosGruposCursosKey;
    }

    public void setAlumnosGruposCursosKey(AlumnosGruposCursosKey alumnosGruposCursosKey) {
        this.alumnosGruposCursosKey = alumnosGruposCursosKey;
    }

    public Alumnos getAlumno() {
        return alumno;
    }

    public void setAlumno(Alumnos alumno) {
        this.alumno = alumno;
    }

    public Grupos getGrupo() {
        return grupo;
    }

    public void setGrupo(Grupos grupo) {
        this.grupo = grupo;
    }

    public Cursos getCurso() {
        return curso;
    }

    public void setCurso(Cursos curso) {
        this.curso = curso;
    }

    @Override
    public String toString() {
        return "AlumnosGruposCursos{" +
                "alumnosGruposCursosKey=" + alumnosGruposCursosKey +
                ", alumno=" + alumno +
                ", grupo=" + grupo +
                ", curso=" + curso +
                '}';
    }

    //  Clase interna Embebida que proporciona la PrimaryKey asociativa
    @Embeddable
    public
    static class AlumnosGruposCursosKey implements Serializable {
        
        //private static final long serialVersionUID = 7994974851694559677L;

        @Column(name="id_alumno")
        private Long id_alumno;
        @Column(name="id_grupo")
        private Long id_grupo;
        @Column(name="id_curso")
        private Long id_curso;

        // standard constructors, getters, and setters
        // hashcode and equals implementation

        public AlumnosGruposCursosKey() {
        }

        public AlumnosGruposCursosKey(Long id_alumno, Long id_grupo, Long id_curso) {
            this.id_alumno = id_alumno;
            this.id_grupo = id_grupo;
            this.id_curso = id_curso;
        }

        public Long getId_alumno() {
            return id_alumno;
        }

        public void setId_alumno(Long id_alumno) {
            this.id_alumno = id_alumno;
        }

        public Long getId_grupo() {
            return id_grupo;
        }

        public void setId_grupo(Long id_grupo) {
            this.id_grupo = id_grupo;
        }

        public Long getId_curso() {
            return id_curso;
        }

        public void setId_curso(Long id_curso) {
            this.id_curso = id_curso;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            AlumnosGruposCursosKey that = (AlumnosGruposCursosKey) o;

            if (id_alumno != null ? !id_alumno.equals(that.id_alumno) : that.id_alumno != null) return false;
            if (id_grupo != null ? !id_grupo.equals(that.id_grupo) : that.id_grupo != null) return false;
            return id_curso != null ? id_curso.equals(that.id_curso) : that.id_curso == null;
        }

        @Override
        public int hashCode() {
            int result = id_alumno != null ? id_alumno.hashCode() : 0;
            result = 31 * result + (id_grupo != null ? id_grupo.hashCode() : 0);
            result = 31 * result + (id_curso != null ? id_curso.hashCode() : 0);
            return result;
        }
    }
}
