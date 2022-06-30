package com.gestorprogramaciones.service.impl.evaluaciones;

import com.gestorprogramaciones.models.cursos.Actividades;
import com.gestorprogramaciones.models.cursos.Ufs;
import com.gestorprogramaciones.models.evaluaciones.Apartados;
import com.gestorprogramaciones.models.evaluaciones.Evaluaciones;
import com.gestorprogramaciones.models.usuarios.Alumnos;
import com.gestorprogramaciones.repositories.evaluaciones.EvaluacionesRepository;
import com.gestorprogramaciones.service.api.evaluaciones.ApartadosAPI;
import com.gestorprogramaciones.service.api.evaluaciones.EvaluacionesAPI;
import com.gestorprogramaciones.service.impl.GenericServiceImpl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

@Service
public class EvaluacionesImpl extends GenericServiceImpl<Evaluaciones, Long> implements EvaluacionesAPI{

    @Autowired
    private EvaluacionesRepository EvaluacionesRepository;
    @Autowired
    private ApartadosAPI apartadosAPI;
    @Override
    public JpaRepository<Evaluaciones, Long> getRespository() {
        return EvaluacionesRepository;
    }
    
    public List<Evaluaciones> getListEvaluacionesActividad(Actividades actividad) {
        List<Evaluaciones> listEval = new ArrayList<Evaluaciones>();
        if (actividad != null) {
            for (Evaluaciones e : this.findAll()) {
                for (Apartados a : apartadosAPI.getApartadosFromActividad(actividad)) {
                    if (e.getApartado().getId_apdo() == a.getId_apdo()) {
                        listEval.add(e);
                    }
                }
            }
        }
        return listEval;
    }

    public List<Evaluaciones> getListEvaluacionesActividadAlumno(Actividades actividad, Alumnos alumno) {
        List<Evaluaciones> listEval = new ArrayList<Evaluaciones>();
        if (actividad != null && alumno != null) {
            for (Evaluaciones e : this.findAll()) {
                for (Apartados a : apartadosAPI.getApartadosFromActividad(actividad)) {
                    if (e.getApartado().getId_apdo() == a.getId_apdo()
                            && e.getAlumno().getId_alumno() == alumno.getId_alumno()) {
                        listEval.add(e);
                    }
                }
            }
        }
        return listEval;
    }

    public Evaluaciones getEvaluacionFromApartadoAlumno(Apartados apartado, Alumnos alumno) {
        if (apartado != null && alumno != null) {
            for (Evaluaciones e : this.findAll()) {
                if (e.getAlumno().getId_alumno() == alumno.getId_alumno()
                        && e.getApartado().getId_apdo() == apartado.getId_apdo()) {
                    return e;
                }
            }
        }
        return null;
    }

    public List<Evaluaciones> getAllEvalucionesFromUf(Ufs uf) {
        List<Evaluaciones> evaluaciones = new ArrayList<Evaluaciones>();
        if (uf != null) {
            for (Evaluaciones e : this.findAll()) {
                if (e.getApartado().getRa().getUf().getId_uf() == uf.getId_uf()) {
                    evaluaciones.add(e);
                }
            }
        }
        return evaluaciones;
    }

}
