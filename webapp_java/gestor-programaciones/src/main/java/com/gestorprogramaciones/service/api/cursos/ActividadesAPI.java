package com.gestorprogramaciones.service.api.cursos;

import java.util.List;

import com.gestorprogramaciones.models.cursos.Actividades;
import com.gestorprogramaciones.models.cursos.Ufs;
import com.gestorprogramaciones.service.api.GenericServiceAPI;

public interface ActividadesAPI extends GenericServiceAPI<Actividades, Long>{
    
    public List<Actividades> buscarActividadesByCurso(Long idCurso);

    public List<Actividades> getActividadesUf(Ufs uf);
}
