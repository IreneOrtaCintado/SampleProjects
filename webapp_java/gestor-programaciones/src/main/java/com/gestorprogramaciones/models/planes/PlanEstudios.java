package com.gestorprogramaciones.models.planes;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "planEstudios")
public class PlanEstudios {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id_planEstudio;

    private String nombre;
    private String cod_planEstudio;
    private String url_decret;

    @OneToMany(mappedBy = "planEstudio")
    private Set<PlanAsignaturas> planAsignaturas = new HashSet<>();

    @ManyToOne
    @JoinColumn(name = "id_tipoEstudio")
    private TiposEstudios tipoEstudio;

    public PlanEstudios() {
    }

    public PlanEstudios(String nombre, String cod_planEstudio, String url_decret, TiposEstudios tipoEstudio) {
        this.nombre = nombre;
        this.cod_planEstudio = cod_planEstudio;
        this.url_decret = url_decret;
        this.tipoEstudio = tipoEstudio;
    }

    public Long getId_planEstudio() {
        return id_planEstudio;
    }

    public void setId_planEstudio(Long id_planEstudio) {
        this.id_planEstudio = id_planEstudio;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getCod_planEstudio() {
        return cod_planEstudio;
    }

    public void setCod_planEstudio(String cod_planEstudio) {
        this.cod_planEstudio = cod_planEstudio;
    }

    public String getUrl_decret() {
        return url_decret;
    }

    public void setUrl_decret(String url_decret) {
        this.url_decret = url_decret;
    }

    public Set<PlanAsignaturas> getPlanAsignaturas() {
        return planAsignaturas;
    }

    public void setPlanAsignaturas(Set<PlanAsignaturas> planAsignaturas) {
        this.planAsignaturas = planAsignaturas;
    }

    public TiposEstudios getTipoEstudio() {
        return tipoEstudio;
    }

    public void setTipoEstudio(TiposEstudios tipoEstudio) {
        this.tipoEstudio = tipoEstudio;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;

        PlanEstudios that = (PlanEstudios) o;

        return id_planEstudio != null ? id_planEstudio.equals(that.id_planEstudio) : that.id_planEstudio == null;
    }

    @Override
    public int hashCode() {
        return id_planEstudio != null ? id_planEstudio.hashCode() : 0;
    }

    @Override
    public String toString() {
        return "PlanEstudios{" +
                "id_planEstudio=" + id_planEstudio +
                ", estudio='" + nombre + '\'' +
                ", cod_planEstudio='" + cod_planEstudio + '\'' +
                ", url_decret='" + url_decret + '\'' +
                ", tipoEstudio=" + tipoEstudio +
                '}';
    }
}
