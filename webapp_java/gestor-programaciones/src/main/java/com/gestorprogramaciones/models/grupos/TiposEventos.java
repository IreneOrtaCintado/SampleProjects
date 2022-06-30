package com.gestorprogramaciones.models.grupos;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "tiposEventos")
public class TiposEventos {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id_tipoEvento;

    private String nombre;

    @OneToMany(mappedBy="tipoEvento")
    private Set<Eventos> eventos = new HashSet<>();

    public TiposEventos() {
    }

    public TiposEventos(String nombre) {
        this.nombre = nombre;
    }

    public Long getId_tipoEvento() {
        return id_tipoEvento;
    }

    public void setId_tipoEvento(Long id_tipoEvento) {
        this.id_tipoEvento = id_tipoEvento;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Set<Eventos> getEventos() {
        return eventos;
    }

    public void setEventos(Set<Eventos> eventos) {
        this.eventos = eventos;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        TiposEventos that = (TiposEventos) o;

        return id_tipoEvento != null ? id_tipoEvento.equals(that.id_tipoEvento) : that.id_tipoEvento == null;
    }

    @Override
    public int hashCode() {
        return id_tipoEvento != null ? id_tipoEvento.hashCode() : 0;
    }

    @Override
    public String toString() {
        return "TiposEventos{" +
                "id_tipoEvento=" + id_tipoEvento +
                ", tipoEvento='" + nombre + '\'' +
                ", eventos=" + eventos +
                '}';
    }
}
