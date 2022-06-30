package com.gestorprogramaciones.models.evaluaciones;

import com.gestorprogramaciones.models.usuarios.Alumnos;

import javax.persistence.*;
import java.io.Serializable;

@Entity
public class Evaluaciones {

    @EmbeddedId
    private EvaluacionesKey evaluacionesKey = new EvaluacionesKey();

    @ManyToOne
    @MapsId("id_alumno")
    @JoinColumn(name = "id_alumno")
    private Alumnos alumno;

    @ManyToOne
    @MapsId("id_apdo")
    @JoinColumn(name = "id_apdo")
    private Apartados apartado;

    private float nota;

    public Evaluaciones() {
    }

    public Evaluaciones(Alumnos alumno, Apartados apartado, float nota) {
        this.alumno = alumno;
        this.apartado = apartado;
        this.nota = nota;
    }

    public EvaluacionesKey getEvaluacionesKey() {
        return evaluacionesKey;
    }

    public void setEvaluacionesKey(EvaluacionesKey evaluacionesKey) {
        this.evaluacionesKey = evaluacionesKey;
    }

    public Alumnos getAlumno() {
        return alumno;
    }

    public void setAlumno(Alumnos alumno) {
        this.alumno = alumno;
    }

    public Apartados getApartado() {
        return apartado;
    }

    public void setApartado(Apartados apartado) {
        this.apartado = apartado;
    }

    public float getNota() {
        return nota;
    }

    public void setNota(float nota) {
        this.nota = nota;
    }

    @Override
    public String toString() {
        return "Evaluaciones{" +
                "id=" + evaluacionesKey +
                ", alumno=" + alumno +
                ", apartado=" + apartado +
                ", nota=" + nota +
                '}';
    }

    // Clase interna Embebida que proporciona la PrimaryKey asociativa
    @Embeddable
    static public class EvaluacionesKey implements Serializable {

        @Column(name = "id_alumno")
        private Long id_alumno;
        @Column(name = "id_apdo")
        private Long id_apdo;

        public EvaluacionesKey() {
        }

        public EvaluacionesKey(Long id_alumno, Long id_apdo) {
            this.id_alumno = id_alumno;
            this.id_apdo = id_apdo;
        }

        public Long getId_alumno() {
            return id_alumno;
        }

        public void setId_alumno(Long id_alumno) {
            this.id_alumno = id_alumno;
        }

        public Long getId_apdo() {
            return id_apdo;
        }

        public void setId_apdo(Long id_apdo) {
            this.id_apdo = id_apdo;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o)
                return true;
            if (o == null || getClass() != o.getClass())
                return false;

            EvaluacionesKey that = (EvaluacionesKey) o;

            if (id_alumno != null ? !id_alumno.equals(that.id_alumno) : that.id_alumno != null)
                return false;
            return id_apdo != null ? id_apdo.equals(that.id_apdo) : that.id_apdo == null;
        }

        @Override
        public int hashCode() {
            int result = id_alumno != null ? id_alumno.hashCode() : 0;
            result = 31 * result + (id_apdo != null ? id_apdo.hashCode() : 0);
            return result;
        }
    }
}
