package com.gestorprogramaciones.service.impl.cursos;

import com.gestorprogramaciones.models.cursos.Ufs;
import com.gestorprogramaciones.repositories.cursos.UfsRepository;
import com.gestorprogramaciones.service.api.cursos.UfsAPI;
import com.gestorprogramaciones.service.impl.GenericServiceImpl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

@Service
public class UfsImpl extends GenericServiceImpl<Ufs, Long> implements UfsAPI {

    @Autowired
    private UfsRepository UfsRepository;

    @Override
    public JpaRepository<Ufs, Long> getRespository() {
        return UfsRepository;
    }

    /**
     * Devuelve una lista de las UFs del Curso pasado por parámetro.
     */
    public List<Ufs> buscarUfsByCursos(Long idCurso) {
        List<Ufs> UfsCurso = new ArrayList<Ufs>();
        for (Ufs uf : this.findAll()) {
            if (uf.getCurso().getId_curso() == idCurso)
                UfsCurso.add(uf);
        }
        return UfsCurso;
    }

}