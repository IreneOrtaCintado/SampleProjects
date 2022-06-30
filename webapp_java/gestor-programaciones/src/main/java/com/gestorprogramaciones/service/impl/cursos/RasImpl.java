package com.gestorprogramaciones.service.impl.cursos;

import com.gestorprogramaciones.models.cursos.Actividades;
import com.gestorprogramaciones.models.cursos.Ras;
import com.gestorprogramaciones.repositories.cursos.RasRepository;
import com.gestorprogramaciones.service.api.cursos.RasAPI;
import com.gestorprogramaciones.service.impl.GenericServiceImpl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

@Service
public class RasImpl extends GenericServiceImpl<Ras, Long> implements RasAPI{

    @Autowired
    private RasRepository RasRepository;
    @Override
    public JpaRepository<Ras, Long> getRespository() {
        return RasRepository;
    }
    

    public List<Ras> buscarRasByCurso(Long idCurso) {
        List<Ras> RasCurso = new ArrayList<Ras>();
        for (Ras ra : this.findAll()) {
            if (ra.getUf().getCurso().getId_curso() == idCurso)
                RasCurso.add(ra);
        }
        return RasCurso;
    }

    public List<Ras> getRasFromActividad(Actividades actividad) {
        List<Ras> rasAct = new ArrayList<Ras>();
        if (actividad != null) {
            for (Ras r : this.findAll()) {
                if (r.getUf().getId_uf() == actividad.getUf().getId_uf()) {
                    rasAct.add(r);
                }
            }
        }
        return rasAct;
    }
}