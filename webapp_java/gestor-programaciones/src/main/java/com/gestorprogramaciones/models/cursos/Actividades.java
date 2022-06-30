package com.gestorprogramaciones.models.cursos;

import com.gestorprogramaciones.models.evaluaciones.Apartados;
import com.gestorprogramaciones.models.evaluaciones.PesoActRa;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;

import org.springframework.format.annotation.DateTimeFormat;

@Entity
@Table(name = "actividades")
public class Actividades {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id_act;

    private String nombre;
    private String cod_act;

    @Temporal(TemporalType.DATE)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date inicio_act;

    @Temporal(TemporalType.DATE)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date fin_act;
    
    private String descripcion_act;

    @OneToMany(mappedBy = "actividad", cascade = CascadeType.ALL,           
    orphanRemoval = true)
    private Set<Apartados> apartados  = new HashSet<>();

    @OneToMany(mappedBy = "actividad", cascade = CascadeType.ALL,           
    orphanRemoval = true)
    private Set<PesoActRa> pesoActRa  = new HashSet<>();

    @ManyToOne
    @JoinColumn(name="id_uf")
    private Ufs uf;

    @ManyToOne
    @JoinColumn(name="id_tipo_act")
    private TiposActividades tipoActividad;

    public Actividades() {
    }

    public Actividades(String nombre, String cod_act, Date inicio_act, Date fin_act, String descripcion_act, Ufs uf, TiposActividades tipoActividad) {
        this.nombre = nombre;
        this.cod_act = cod_act;
        this.inicio_act = inicio_act;
        this.fin_act = fin_act;
        this.descripcion_act = descripcion_act;
        this.uf = uf;
        this.tipoActividad = tipoActividad;
    }

    public Long getId_act() {
        return id_act;
    }

    public void setId_act(Long id_act) {
        this.id_act = id_act;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getCod_act() {
        return cod_act;
    }

    public void setCod_act(String cod_act) {
        this.cod_act = cod_act;
    }

    public Date getInicio_act() {
        return inicio_act;
    }

    public void setInicio_act(Date inicio_act) {
        this.inicio_act = inicio_act;
    }

    public Date getFin_act() {
        return fin_act;
    }

    public void setFin_act(Date fin_act) {
        this.fin_act = fin_act;
    }

    public String getDescripcion_act() {
        return descripcion_act;
    }

    public void setDescripcion_act(String descripcion_act) {
        this.descripcion_act = descripcion_act;
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

    public Ufs getUf() {
        return uf;
    }

    public void setUf(Ufs uf) {
        this.uf = uf;
    }

    public TiposActividades getTipoActividad() {
        return tipoActividad;
    }

    public void setTipoActividad(TiposActividades tipoActividad) {
        this.tipoActividad = tipoActividad;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Actividades that = (Actividades) o;

        return id_act != null ? id_act.equals(that.id_act) : that.id_act == null;
    }

    @Override
    public int hashCode() {
        return id_act != null ? id_act.hashCode() : 0;
    }

    @Override
    public String toString() {
        return "Actividades{" +
                "id_act=" + id_act +
                ", actividad='" + nombre + '\'' +
                ", cod_act='" + cod_act + '\'' +
                ", inicio_act=" + inicio_act +
                ", fin_act=" + fin_act +
                ", descripcion_act='" + descripcion_act + '\'' +
                ", apartados=" + apartados +
                ", pesoActRa=" + pesoActRa +
                ", uf=" + uf +
                ", tipoActividad=" + tipoActividad +
                '}';
    }
}
