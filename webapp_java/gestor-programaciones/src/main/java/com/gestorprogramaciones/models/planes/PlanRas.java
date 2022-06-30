package com.gestorprogramaciones.models.planes;

import com.gestorprogramaciones.models.cursos.Ras;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "planRas")
public class PlanRas {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id_planRa;

    private String cod_planRa;
    private String descripcion;
    private int porcent_uf;

    @ManyToOne
    @JoinColumn(name = "id_planUf")
    private PlanUfs planUf;

    @OneToMany(mappedBy = "planRa")
    private Set<Ras> ras = new HashSet<>();

    public PlanRas() {
    }

    public PlanRas(String cod_planRa, String descripcion, int porcent_uf, PlanUfs planUf) {
        this.cod_planRa = cod_planRa;
        this.descripcion = descripcion;
        this.porcent_uf = porcent_uf;
        this.planUf = planUf;
    }

    public Long getId_planRa() {
        return id_planRa;
    }

    public void setId_planRa(Long id_planRa) {
        this.id_planRa = id_planRa;
    }

    public String getCod_planRa() {
        return cod_planRa;
    }

    public void setCod_planRa(String cod_planRa) {
        this.cod_planRa = cod_planRa;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public int getPorcent_uf() {
        return porcent_uf;
    }

    public void setPorcent_uf(int porcent_uf) {
        this.porcent_uf = porcent_uf;
    }

    public PlanUfs getPlanUf() {
        return planUf;
    }

    public void setPlanUf(PlanUfs planUf) {
        this.planUf = planUf;
    }

    public Set<Ras> getRas() {
        return ras;
    }

    public void setRas(Set<Ras> ras) {
        this.ras = ras;
    }

    @Override
    public String toString() {
        return "PlanRas{" +
                "id_planRa=" + id_planRa +
                ", cod_planRa='" + cod_planRa + '\'' +
                ", descripcion_planRa='" + descripcion + '\'' +
                ", porcent_uf=" + porcent_uf +
                ", planUf=" + planUf +
                '}';
    }
}
