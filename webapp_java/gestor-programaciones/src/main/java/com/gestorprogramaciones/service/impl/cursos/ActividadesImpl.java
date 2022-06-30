package com.gestorprogramaciones.service.impl.cursos;

import com.gestorprogramaciones.models.cursos.Actividades;
import com.gestorprogramaciones.models.cursos.Ufs;
import com.gestorprogramaciones.repositories.cursos.ActividadesRepository;
import com.gestorprogramaciones.service.api.cursos.ActividadesAPI;
import com.gestorprogramaciones.service.impl.GenericServiceImpl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

@Service
public class ActividadesImpl extends GenericServiceImpl<Actividades, Long> implements ActividadesAPI {

    @Autowired
    private ActividadesRepository ActividadesRepository;

    @Override
    public JpaRepository<Actividades, Long> getRespository() {
        return ActividadesRepository;
    }

    public List<Actividades> buscarActividadesByCurso(Long idCurso) {
        List<Actividades> ActsCurso = new ArrayList<Actividades>();
        for (Actividades act : this.findAll()) {
            if (act.getUf().getCurso().getId_curso() == idCurso)
                ActsCurso.add(act);
        }
        return ActsCurso;
    }

    public List<Actividades> getActividadesUf(Ufs uf) {
        List<Actividades> actividadesUf = new ArrayList<Actividades>();
        if (uf != null) {
            for (Actividades a : this.findAll()) {
                if (a.getUf().getId_uf() == uf.getId_uf()) {
                    actividadesUf.add(a);
                }
            }
        }
        return actividadesUf;
    }

}