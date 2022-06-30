package com.gestorprogramaciones.service.api.evaluaciones;

import java.util.List;

import com.gestorprogramaciones.models.cursos.Actividades;
import com.gestorprogramaciones.models.cursos.Ufs;
import com.gestorprogramaciones.models.evaluaciones.Apartados;
import com.gestorprogramaciones.models.evaluaciones.Evaluaciones;
import com.gestorprogramaciones.models.usuarios.Alumnos;
import com.gestorprogramaciones.service.api.GenericServiceAPI;

public interface EvaluacionesAPI extends GenericServiceAPI<Evaluaciones, Long>{
    
    public List<Evaluaciones> getListEvaluacionesActividad(Actividades actividad);
    public List<Evaluaciones> getListEvaluacionesActividadAlumno(Actividades actividad, Alumnos alumno);
    public Evaluaciones getEvaluacionFromApartadoAlumno(Apartados apartado, Alumnos alumno);
    public List<Evaluaciones> getAllEvalucionesFromUf(Ufs uf);

}
