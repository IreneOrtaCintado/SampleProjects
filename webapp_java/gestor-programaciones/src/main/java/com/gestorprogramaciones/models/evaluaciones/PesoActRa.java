package com.gestorprogramaciones.models.evaluaciones;

import com.gestorprogramaciones.models.cursos.Actividades;
import com.gestorprogramaciones.models.cursos.Ras;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "pesoActRa")
public class PesoActRa {

    @EmbeddedId()
    private PesoActRaKey pesoActRaKey = new PesoActRaKey();

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("id_act")   //  id compuesto
    @JoinColumn(name = "id_act")
    private Actividades actividad;

    @ManyToOne
    @MapsId("id_ra")    //  id compuesto
    @JoinColumn(name = "id_ra")
    private Ras ra;

    private int porcent_pesoActRa;

    public PesoActRa() {
    }

    public PesoActRa(Actividades actividad, Ras ra, int porcent_pesoActRa) {
        this.actividad = actividad;
        this.ra = ra;
        this.porcent_pesoActRa = porcent_pesoActRa;
    }

    public PesoActRaKey getPesoActRaKey() {
        return pesoActRaKey;
    }

    public void setPesoActRaKey(PesoActRaKey pesoActRaKey) {
        this.pesoActRaKey = pesoActRaKey;
    }

    public Actividades getActividad() {
        return actividad;
    }

    public void setActividad(Actividades actividad) {
        this.actividad = actividad;
    }

    public Ras getRa() {
        return ra;
    }

    public void setRa(Ras ra) {
        this.ra = ra;
    }

    public int getPorcent_pesoActRa() {
        return porcent_pesoActRa;
    }

    public void setPorcent_pesoActRa(int porcent_pesoActRa) {
        this.porcent_pesoActRa = porcent_pesoActRa;
    }

    @Override
    public String toString() {
        return "PesoActRa{" +
                "id=" + pesoActRaKey +
                ", actvidad=" + actividad +
                ", ra=" + ra +
                ", porcent_pesoActRa=" + porcent_pesoActRa +
                '}';
    }

    //  Clase interna Embebida que proporciona la PrimaryKey asociativa
    @Embeddable
    static public class PesoActRaKey implements Serializable {
        @Column(name="id_act")
        private Long  id_act;
        @Column(name="id_ra")
        private Long  id_ra;

        public PesoActRaKey() {
        }

        public PesoActRaKey(Long id_act, Long id_ra) {
            this.id_act = id_act;
            this.id_ra = id_ra;
        }

        public Long getId_act() {
            return id_act;
        }

        public void setId_act(Long id_act) {
            this.id_act = id_act;
        }

        public Long getId_ra() {
            return id_ra;
        }

        public void setId_ra(Long id_ra) {
            this.id_ra = id_ra;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            PesoActRaKey that = (PesoActRaKey) o;

            if (id_act != null ? !id_act.equals(that.id_act) : that.id_act != null) return false;
            return id_ra != null ? id_ra.equals(that.id_ra) : that.id_ra == null;
        }

        @Override
        public int hashCode() {
            int result = id_act != null ? id_act.hashCode() : 0;
            result = 31 * result + (id_ra != null ? id_ra.hashCode() : 0);
            return result;
        }
    }
}
