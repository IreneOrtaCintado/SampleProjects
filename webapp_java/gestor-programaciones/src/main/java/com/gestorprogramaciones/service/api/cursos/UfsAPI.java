package com.gestorprogramaciones.service.api.cursos;

import java.util.List;

import com.gestorprogramaciones.models.cursos.Ufs;
import com.gestorprogramaciones.service.api.GenericServiceAPI;

public interface UfsAPI extends GenericServiceAPI<Ufs, Long>{
    
    public List<Ufs> buscarUfsByCursos(Long idCurso);
}
