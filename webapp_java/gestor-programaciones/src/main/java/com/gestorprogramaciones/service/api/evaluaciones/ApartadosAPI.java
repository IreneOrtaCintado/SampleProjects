package com.gestorprogramaciones.service.api.evaluaciones;

import java.util.List;

import com.gestorprogramaciones.models.cursos.Actividades;
import com.gestorprogramaciones.models.evaluaciones.Apartados;
import com.gestorprogramaciones.service.api.GenericServiceAPI;

public interface ApartadosAPI extends GenericServiceAPI<Apartados, Long>{

    public List<Apartados> buscarApartadosByCurso(Long idCurso);
    public List<Apartados> getApartadosFromActividad(Actividades actividad);
    public void updateApartadosActividad(List<Apartados> apartados, Actividades actividad);
}
