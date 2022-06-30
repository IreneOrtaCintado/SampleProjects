package com.gestorprogramaciones.service.impl.evaluaciones;

import com.gestorprogramaciones.models.cursos.Actividades;
import com.gestorprogramaciones.models.cursos.Ras;
import com.gestorprogramaciones.models.cursos.Ufs;
import com.gestorprogramaciones.models.evaluaciones.PesoActRa;
import com.gestorprogramaciones.repositories.evaluaciones.PesoActRaRepository;
import com.gestorprogramaciones.service.api.evaluaciones.PesoActRaAPI;
import com.gestorprogramaciones.service.impl.GenericServiceImpl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

@Service
public class PesoActRaImpl extends GenericServiceImpl<PesoActRa, Long> implements PesoActRaAPI{

    @Autowired
    private PesoActRaRepository PesoActRaRepository;
    @Override
    public JpaRepository<PesoActRa, Long> getRespository() {
        return PesoActRaRepository;
    }

    public List<PesoActRa> buscarPesoActRasByCurso(Long idCurso) {
        List<PesoActRa> pesoActRaCurso = new ArrayList<PesoActRa>();
        for (PesoActRa item : this.findAll()) {
            if (item.getActividad().getUf().getCurso().getId_curso() == idCurso)
                pesoActRaCurso.add(item);
        }
        return pesoActRaCurso;
    }

    public List<PesoActRa> getListPesoActRaFromActividad(Actividades actividad) {
        List<PesoActRa> listPesoActRa = new ArrayList<PesoActRa>();
        if (actividad != null) {
            for (PesoActRa par : this.findAll()) {
                if (par.getActividad().getId_act() == actividad.getId_act()) {
                    listPesoActRa.add(par);
                }
            }
        }
        return listPesoActRa;
    }

    public PesoActRa getPesoFromActRa(Actividades actividad, Ras ra) {
        if (actividad != null && ra != null) {
            for (PesoActRa e : this.findAll()) {
                if (e.getActividad().getId_act() == actividad.getId_act()
                        && e.getRa().getId_ra() == ra.getId_ra()) {
                    return e;
                }
            }
        }
        return null;
    }
    
    public List<PesoActRa> getPesosActividadesRaFromUf(Ufs uf) {
        List<PesoActRa> pesos = new ArrayList<PesoActRa>();
        if (uf != null) {
            for (PesoActRa par : this.findAll()) {
                if (par.getRa().getUf().getId_uf() == uf.getId_uf()) {
                    pesos.add(par);
                }
            }
        }
        return pesos;
    }
}
