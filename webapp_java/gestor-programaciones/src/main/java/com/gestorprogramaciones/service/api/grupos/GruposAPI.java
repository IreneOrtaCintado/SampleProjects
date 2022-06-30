package com.gestorprogramaciones.service.api.grupos;

import java.util.List;

import com.gestorprogramaciones.models.cursos.Cursos;
import com.gestorprogramaciones.models.grupos.Grupos;
import com.gestorprogramaciones.models.usuarios.Docentes;
import com.gestorprogramaciones.service.api.GenericServiceAPI;

public interface GruposAPI extends GenericServiceAPI<Grupos, Long>{
    
    public Grupos findGrupo(Grupos grupo);
    
    public List<Grupos> findGruposByDocente(Docentes docente);
    public List<Grupos> findGruposByCurso(Cursos curso);

    public void deleteGruposSinCurso(List<Grupos> listaGrupos);
}
