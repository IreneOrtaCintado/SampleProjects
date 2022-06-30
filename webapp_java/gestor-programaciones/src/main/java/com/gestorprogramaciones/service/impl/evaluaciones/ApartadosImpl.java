package com.gestorprogramaciones.service.impl.evaluaciones;

import com.gestorprogramaciones.models.cursos.Actividades;
import com.gestorprogramaciones.models.cursos.Ras;
import com.gestorprogramaciones.models.evaluaciones.Apartados;
import com.gestorprogramaciones.repositories.evaluaciones.ApartadosRepository;
import com.gestorprogramaciones.service.api.cursos.RasAPI;
import com.gestorprogramaciones.service.api.evaluaciones.ApartadosAPI;
import com.gestorprogramaciones.service.impl.GenericServiceImpl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

@Service
public class ApartadosImpl extends GenericServiceImpl<Apartados, Long> implements ApartadosAPI {

    @Autowired
    private ApartadosRepository ApartadosRepository;
    @Autowired
    private RasAPI rasAPI;

    @Override
    public JpaRepository<Apartados, Long> getRespository() {
        return ApartadosRepository;
    }

    public List<Apartados> buscarApartadosByCurso(Long idCurso) {
        List<Apartados> apartadosCurso = new ArrayList<Apartados>();
        for (Apartados item : this.findAll()) {
            if (item.getActividad().getUf().getCurso().getId_curso() == idCurso)
                apartadosCurso.add(item);
        }
        return apartadosCurso;
    }

    public List<Apartados> getApartadosFromActividad(Actividades actividad) {
        List<Apartados> apartadosAct = new ArrayList<Apartados>();
        if (actividad != null) {
            for (Apartados a : this.findAll()) {
                if (a.getActividad().getId_act() == actividad.getId_act()) {

                    apartadosAct.add(a);
                }
            }
        }
        return apartadosAct;
    }

    /**
     * Guarda y actualiza los apartados de una actividad.
     * 
     * @param apartados
     * @param actividad
     */
    public void updateApartadosActividad(List<Apartados> apartados, Actividades actividad) {
        List<Apartados> apartadosActividad = this.getApartadosFromActividad(actividad);
        List<Apartados> apartadosNuevos = new ArrayList<Apartados>();
        List<Apartados> apartadosActualizar = new ArrayList<Apartados>();

        // sort update and new
        for (Apartados apartado : apartados) {
            if (apartado.getId_apdo() == null) {
                apartadosNuevos.add(apartado);
            } else {
                apartadosActualizar.add(apartado);
            }
        }

        // update apartados
        for (Apartados apartadoAct : apartadosActividad) {
            System.out.println("act: " + apartadoAct.getId_apdo() + ", " + apartadoAct.getDescripcion());
            boolean borrarApartado = true;
            for (Apartados apartado : apartadosActualizar) {
                System.out.println("update: " + apartado.getId_apdo() + ", " + apartado.getDescripcion());
                if (apartado.getId_apdo() == apartadoAct.getId_apdo()) {
                    apartadoAct.setDescripcion(apartado.getDescripcion());
                    apartadoAct.setPeso_apdo_act_ra(apartado.getPeso_apdo_act_ra());
                    Ras ra = rasAPI.findById(apartado.getRa().getId_ra());
                    apartadoAct.setRa(ra);
                    this.save(apartadoAct);
                    borrarApartado = false;
                    break;
                }
            }
            // delete apartado
            if (borrarApartado) {
                this.delete(apartadoAct);
            }
        }

        // añadir apartados nuevos
        for (Apartados apartado : apartadosNuevos) {
            System.out.println("nuevo: " + apartado.getId_apdo() + ", " + apartado.getDescripcion());
            Ras ra = rasAPI.findById(apartado.getRa().getId_ra());
            Apartados nuevoApartado = new Apartados(
                    apartado.getDescripcion(),
                    apartado.getPeso_apdo_act_ra(),
                    actividad,
                    ra);
            this.save(nuevoApartado);
        }
    }
}
