package com.gestorprogramaciones.models.grupos;


import java.util.Date;
import javax.persistence.*;

import org.springframework.format.annotation.DateTimeFormat;

@Entity
@Table(name = "eventos")
public class Eventos {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id_evento;

    @ManyToOne
    @JoinColumn(name="id_grupo")
    private Grupos grupo;

    @ManyToOne
    @JoinColumn(name="id_tipoEvento")
    private TiposEventos tipoEvento;

    @Temporal(TemporalType.DATE)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date fecha;
    private Float horasPrev;
    private Float horasReal;
    private String comenta;

    public Eventos() {
    }

    public Eventos(Grupos grupo, TiposEventos tipoEvento, Date fecha, Float horasPrev, Float horasReal, String comenta) {
        this.grupo = grupo;
        this.tipoEvento = tipoEvento;
        this.fecha = fecha;
        this.horasPrev = horasPrev;
        this.horasReal = horasReal;
        this.comenta = comenta;
    }

    public Long getId_evento() {
        return id_evento;
    }

    public void setId_evento(Long id_evento) {
        this.id_evento = id_evento;
    }

    public Grupos getGrupo() {
        return grupo;
    }

    public void setGrupo(Grupos grupo) {
        this.grupo = grupo;
    }

    public TiposEventos getTipoEvento() {
        return tipoEvento;
    }

    public void setTipoEvento(TiposEventos tipoEvento) {
        this.tipoEvento = tipoEvento;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public Float getHorasPrev() {
        return horasPrev;
    }

    public void setHorasPrev(Float horasPrev) {
        this.horasPrev = horasPrev;
    }

    public Float getHorasReal() {
        return horasReal;
    }

    public void setHorasReal(Float horasReal) {
        this.horasReal = horasReal;
    }

    public String getComenta() {
        return comenta;
    }

    public void setComenta(String comenta) {
        this.comenta = comenta;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Eventos eventos = (Eventos) o;

        return id_evento != null ? id_evento.equals(eventos.id_evento) : eventos.id_evento == null;
    }

    @Override
    public int hashCode() {
        return id_evento != null ? id_evento.hashCode() : 0;
    }

    @Override
    public String toString() {
        return
                "id_evento=" + id_evento +
            //    ", grupo=" + grupo +
            //    ", tipoEvento=" + tipoEvento +
            //    ", fecha=" + fecha +
                ", horas_prev=" + horasPrev +
                ", horas_real=" + horasReal +
                ", comenta='" + comenta ;
    }
}

