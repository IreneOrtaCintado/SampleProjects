package com.gestorprogramaciones.service;

import java.util.ArrayList;
import java.util.List;

import com.gestorprogramaciones.models.evaluaciones.Apartados;

/**
 * Sirve para recibir una lista de Apartados del modal de rubircas en la
 * ventana de programación.
 */
public class ApartadosCollection {
    private List<Apartados> apartados;

    public ApartadosCollection() {
        apartados = new ArrayList<Apartados>();
    }

    public ApartadosCollection(List<Apartados> apartados) {
        this.apartados = apartados;
    }

    public void addApartado(Apartados apartado) {
        this.apartados.add(apartado);
    }

    public List<Apartados> getApartados() {
        return apartados;
    }

    public void setApartados(List<Apartados> apartados) {
        this.apartados = apartados;
    }

}
