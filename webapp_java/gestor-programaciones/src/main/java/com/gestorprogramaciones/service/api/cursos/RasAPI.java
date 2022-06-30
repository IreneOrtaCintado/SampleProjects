package com.gestorprogramaciones.service.api.cursos;

import java.util.List;

import com.gestorprogramaciones.models.cursos.Actividades;
import com.gestorprogramaciones.models.cursos.Ras;
import com.gestorprogramaciones.service.api.GenericServiceAPI;

public interface RasAPI extends GenericServiceAPI<Ras, Long>{
    
    public List<Ras> buscarRasByCurso(Long idCurso);

    public List<Ras> getRasFromActividad(Actividades actividad);
}
