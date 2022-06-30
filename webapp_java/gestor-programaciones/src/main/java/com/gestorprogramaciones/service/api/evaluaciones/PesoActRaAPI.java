package com.gestorprogramaciones.service.api.evaluaciones;

import java.util.List;

import com.gestorprogramaciones.models.cursos.Actividades;
import com.gestorprogramaciones.models.cursos.Ras;
import com.gestorprogramaciones.models.cursos.Ufs;
import com.gestorprogramaciones.models.evaluaciones.PesoActRa;
import com.gestorprogramaciones.service.api.GenericServiceAPI;

public interface PesoActRaAPI extends GenericServiceAPI<PesoActRa, Long>{
    
    public List<PesoActRa> buscarPesoActRasByCurso(Long idCurso);
    public List<PesoActRa> getListPesoActRaFromActividad(Actividades actividad);
    public PesoActRa getPesoFromActRa(Actividades actividad, Ras ra);
    public List<PesoActRa> getPesosActividadesRaFromUf(Ufs uf);
}
