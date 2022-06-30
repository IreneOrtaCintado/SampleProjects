package com.gestorprogramaciones.controllers;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import com.gestorprogramaciones.models.cursos.Actividades;
import com.gestorprogramaciones.models.cursos.Cursos;
import com.gestorprogramaciones.models.cursos.Ras;
import com.gestorprogramaciones.models.cursos.Ufs;
import com.gestorprogramaciones.models.evaluaciones.Apartados;
import com.gestorprogramaciones.models.evaluaciones.Evaluaciones;
import com.gestorprogramaciones.models.evaluaciones.PesoActRa;
import com.gestorprogramaciones.models.grupos.Grupos;
import com.gestorprogramaciones.models.usuarios.Alumnos;
import com.gestorprogramaciones.models.usuarios.Docentes;
import com.gestorprogramaciones.service.api.cursos.ActividadesAPI;
import com.gestorprogramaciones.service.api.cursos.AlumnosGruposCursoAPI;
import com.gestorprogramaciones.service.api.cursos.CursosAPI;
import com.gestorprogramaciones.service.api.cursos.RasAPI;
import com.gestorprogramaciones.service.api.evaluaciones.ApartadosAPI;
import com.gestorprogramaciones.service.api.evaluaciones.EvaluacionesAPI;
import com.gestorprogramaciones.service.api.evaluaciones.PesoActRaAPI;
import com.gestorprogramaciones.service.api.grupos.GruposAPI;
import com.gestorprogramaciones.service.api.tablasaux.IdiomasAPI;
import com.gestorprogramaciones.service.api.usuarios.AlumnosAPI;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class EvaluacionActividadesController {

    // TODO resolver actualizar con flechas campo: cambiar a 2 decimales en BBDD

    private static Cursos currentCurso;
    private static Docentes usuarioDocente;
    private Grupos selectedGroup;
    private String selected_group_saved;
    private List<Grupos> listaGrupos;
    private Actividades selectedActividad;
    private String selected_act_saved;

    @Autowired
    private GruposAPI gruposAPI;
    @Autowired
    private AlumnosAPI alumnosAPI;
    @Autowired
    private CursosAPI cursosAPI;
    @Autowired
    private IdiomasAPI idiomas;
    @Autowired
    private ActividadesAPI actividadesAPI;
    @Autowired
    private RasAPI rasAPI;
    @Autowired
    private ApartadosAPI apartadosAPI;
    @Autowired
    private AlumnosGruposCursoAPI alumnosGruposCursoAPI;
    @Autowired
    private PesoActRaAPI pesoActRaAPI;
    @Autowired
    private EvaluacionesAPI evaluacionesAPI;

    //  GET INIT
    @GetMapping("/evaluacion_actividades/{id}")
    public String diarioLoad(@PathVariable Long id, Model model) {
        selectedActividad = actividadesAPI.findById(id);
        currentCurso = cursosAPI.findById(selectedActividad.getUf().getCurso().getId_curso());
        usuarioDocente = LoginController.usuarioDocente;
        selectedGroup = EvaluacionController.selectedGroup;
        return "redirect:/evaluacion_actividades";
    }

    // GET
    @GetMapping("/evaluacion_actividades")
    public String diario(@RequestParam(defaultValue = "-1") String selectedAct,
            @RequestParam(defaultValue = "-1") String selected_group,
            Model model) {
        // language
        model.addAttribute("languages", idiomas.findAll());

        if (LoginController.getUsuarioDocente() != null) {
            model.addAttribute("user", LoginController.getUsuarioDocente().getNombre());
        }
        
        model.addAttribute("currentCurso", currentCurso);
        Ufs selectedUf = selectedActividad.getUf(); // can be null - fix
        model.addAttribute("selectedUf", selectedUf);
        List<Actividades> listaAct = actividadesAPI.getActividadesUf(selectedUf);
        model.addAttribute("actividadesUf", listaAct);

        // actualizar actividad seleccionado con el menu lateral
        if (!listaAct.isEmpty()) {
            if (!selectedAct.equals("-1")) {
                selectedActividad = actividadesAPI.findById(Long.parseLong(selectedAct));
            }
            // info act
            model.addAttribute("selectedAct", selectedActividad);
            model.addAttribute("currentActId", selectedActividad.getId_act());
            model.addAttribute("currentAct", selectedActividad);
            // System.out.println(selectedGroup.getId_grupo());
        } else {
            // info grupo
            model.addAttribute("currentActId", "");
            model.addAttribute("currentAct", new Actividades());
            // info eventos
        }

        // GRUPO
        // buscar grupos curso
        listaGrupos = gruposAPI.findGruposByCurso(currentCurso);
        model.addAttribute("grupos", listaGrupos);
        model.addAttribute("grupo", selectedGroup);

        // Panel lateral grupos
        if (selected_group_saved == null)
            selected_group_saved = -1 + "";
        else if (!selected_group.equals("-1"))
            selected_group_saved = selected_group;

        if (!listaGrupos.isEmpty()) {
            if (selected_group_saved.equals("-1")) {
                selectedGroup = listaGrupos.get(0);
            } else {
                selectedGroup = gruposAPI.findById(Long.parseLong(selected_group_saved));
            }
            model.addAttribute("currentGroupId", selectedGroup.getId_grupo());
            model.addAttribute("currentGroup", selectedGroup);
        } else {
            model.addAttribute("currentGroupId", "");
            model.addAttribute("currentGroup", new Grupos());
        }

        // ALUMNOS
        List<Alumnos> alumnosGrupoCurso = alumnosGruposCursoAPI.getAlumnosGrupoCurso(currentCurso, selectedGroup);
        model.addAttribute("alumnosGrupoCurso", alumnosGrupoCurso);
        // RA Actividad
        model.addAttribute("listaRAsUf", rasAPI.getRasFromActividad(selectedActividad));
        // Apartados Actividad
        model.addAttribute("listaApartadosAct", apartadosAPI.getApartadosFromActividad(selectedActividad));
        // peso act ra
        model.addAttribute("listaPesosRaAct", pesoActRaAPI.getListPesoActRaFromActividad(selectedActividad));
        // evaluación apartados
        model.addAttribute("listaEvalaucionesAct", evaluacionesAPI.getListEvaluacionesActividad(selectedActividad));
        // nueva evaluación
        model.addAttribute("newEvaluacion", new Evaluaciones());

        /*
         * if (usuarioDocente == null)
         * usuarioDocente = LoginController.getUsuarioDocente();
         * model.addAttribute("user", usuarioDocente.getNombre());
         */

        return "evaluacion_actividades";
    }

    // POST
    @PostMapping("/evaluacion_actividades/save_nota")
    public String saveEvaluacion(Evaluaciones newEvaluacion, Model model) {
        Apartados apartado = apartadosAPI.findById(newEvaluacion.getApartado().getId_apdo());
        Alumnos alumno = alumnosAPI.findById(newEvaluacion.getAlumno().getId_alumno());
        Evaluaciones evaluacion = evaluacionesAPI.getEvaluacionFromApartadoAlumno(apartado, alumno);
        //  nueva evaluación
        if(evaluacion==null){
            evaluacionesAPI.save(new Evaluaciones(alumno, apartado,
            newEvaluacion.getNota()));
        } else {    
            // actualizar evaluación
            if(newEvaluacion.getNota()!=-1){
                evaluacion.setNota(newEvaluacion.getNota());
                evaluacionesAPI.save(evaluacion);
            }
            else{
                // eliminar registro
                evaluacionesAPI.delete(evaluacion); 
            }
            
        }
        return "redirect:/evaluacion_actividades";
    }
}
