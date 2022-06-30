package com.gestorprogramaciones.models.cursos;

import com.gestorprogramaciones.models.evaluaciones.Apartados;
import com.gestorprogramaciones.models.evaluaciones.PesoActRa;
import com.gestorprogramaciones.models.planes.PlanRas;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "ras")
public class Ras {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id_ra;

    @OneToMany(mappedBy = "ra")
    private Set<Apartados> apartados = new HashSet<>();

    @OneToMany(mappedBy = "ra")
    private Set<PesoActRa> pesoActRa  = new HashSet<>();

    @ManyToOne
    @JoinColumn(name="id_planRa")
    private PlanRas planRa;

    @ManyToOne
    @JoinColumn(name="id_uf")
    private Ufs uf;

    public Ras() {
    }

    public Ras(PlanRas planRa, Ufs uf) {
        this.planRa = planRa;
        this.uf = uf;
    }

    public Long getId_ra() {
        return id_ra;
    }

    public void setId_ra(Long id_ra) {
        this.id_ra = id_ra;
    }

    public Set<Apartados> getApartados() {
        return apartados;
    }

    public void setApartados(Set<Apartados> apartados) {
        this.apartados = apartados;
    }

    public Set<PesoActRa> getPesoActRa() {
        return pesoActRa;
    }

    public void setPesoActRa(Set<PesoActRa> pesoActRa) {
        this.pesoActRa = pesoActRa;
    }

    public PlanRas getPlanRa() {
        return planRa;
    }

    public void setPlanRa(PlanRas planRa) {
        this.planRa = planRa;
    }

    public Ufs getUf() {
        return uf;
    }

    public void setUf(Ufs uf) {
        this.uf = uf;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Ras ras = (Ras) o;

        return id_ra != null ? id_ra.equals(ras.id_ra) : ras.id_ra == null;
    }

    @Override
    public int hashCode() {
        return id_ra != null ? id_ra.hashCode() : 0;
    }

    @Override
    public String toString() {
        return "Ras{" +
                "id_ra=" + id_ra +
                ", apartados=" + apartados +
                ", pesoActRa=" + pesoActRa +
                ", planRa=" + planRa +
                ", uf=" + uf +
                '}';
    }
}
