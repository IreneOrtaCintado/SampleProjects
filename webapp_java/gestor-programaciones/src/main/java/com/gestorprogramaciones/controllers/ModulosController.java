package com.gestorprogramaciones.controllers;

import com.gestorprogramaciones.models.tablasaux.Centros;
import com.gestorprogramaciones.service.api.cursos.AlumnosGruposCursoAPI;
import com.gestorprogramaciones.service.api.cursos.CursosAPI;
import com.gestorprogramaciones.service.api.grupos.GruposAPI;
import com.gestorprogramaciones.service.api.planes.PlanAsignaturasAPI;
import com.gestorprogramaciones.service.api.planes.PlanEstudiosAPI;
import com.gestorprogramaciones.service.api.tablasaux.CentrosAPI;
import com.gestorprogramaciones.service.api.tablasaux.IdiomasAPI;
import com.gestorprogramaciones.service.api.usuarios.DocentesAPI;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.gestorprogramaciones.models.cursos.AlumnosGruposCurso;
import com.gestorprogramaciones.models.cursos.Cursos;
import com.gestorprogramaciones.models.grupos.Grupos;
import com.gestorprogramaciones.models.planes.PlanAsignaturas;
import com.gestorprogramaciones.models.planes.PlanEstudios;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class ModulosController {

    // TODO optimizar el acceso a los datos (menos consultas a la BBDD)
    // TODO si se modifica el módulo del curso, se eliminará toda la programación y
    // todas las evaluaciones?
    // TODO permitir borrar cursos con relaciones en la BBDD
    // TODO mover funciones a APIs

    private Long selectedFilterId;
    private Centros selectedFilterCenter;
    private String selectedFilterYear;

    @Autowired
    private CentrosAPI centrosAPI;
    @Autowired
    private CursosAPI cursosAPI;
    @Autowired
    private PlanAsignaturasAPI planAsignaturasAPI;
    @Autowired
    private PlanEstudiosAPI planEstudiosAPI;
    @Autowired
    private IdiomasAPI idiomas;
    @Autowired
    private DocentesAPI docentesAPI;
    @Autowired
    private GruposAPI gruposAPI;
    @Autowired
    private AlumnosGruposCursoAPI alumnosGruposCursoAPI;

    // GET
    @GetMapping("/modulos")
    public String modulos(@RequestParam(defaultValue = "-1") String selectedFilter, Model model) {
        // TEMP USER
        tempFixErrorUser(model);
        resetControllerData();

        // language
        model.addAttribute("languages", idiomas.findAll());

        if (LoginController.getUsuarioDocente() != null) {
            model.addAttribute("user", LoginController.getUsuarioDocente().getNombre());
        }

        // data modulo
        model.addAttribute("newCurso", new Cursos());
        model.addAttribute("newCentro", new Centros());
        model.addAttribute("selectedPlanEtudios", new PlanEstudios());
        model.addAttribute("selectedAsignatura", new PlanAsignaturas());

        // modal course - dropdown data
        model.addAttribute("centros", centrosAPI.findAll());
        model.addAttribute("planEstudios", planEstudiosAPI.findAll());
        model.addAttribute("planAsignaturas", planAsignaturasAPI.findAll());

        // selected filter (a course represents a filter option)
        model.addAttribute("currentFilter", selectedFilter);
        selectedFilterId = Long.parseLong(selectedFilter);

        // Filtrar cursos:
        // get cursos user
        List<Cursos> cursosDocente = cursosAPI.buscarCursosDocente(LoginController.getUsuarioDocente());
        // find years and centers from user's courses
        if (selectedFilterId != null && selectedFilterId != -1) {
            selectedFilterCenter = cursosAPI.findById(selectedFilterId).getCentro();
            selectedFilterYear = cursosAPI.findById(selectedFilterId).getAnyo();
            // filtra los módulos del docente de cada año/centro en la ventana principal
            model.addAttribute("modulos",
                    cursosAPI.buscarCursosCentroAnyo(cursosDocente, selectedFilterCenter, selectedFilterYear));
        } else {
            model.addAttribute("modulos", cursosDocente);
        }
        // crea un elemento en el menu por cada centro/año de los cursos registrados
        model.addAttribute("listaCentroAnyo", cursosAPI.agruparCursosCentroAnyo(cursosDocente));
        return "modulos";
    }

    // POST - CREAR/MODIFICAR CURSO
    @PostMapping("/curso/save")
    public String saveCurso(Cursos newCurso, Model model) {
        // TEMP USER
        tempFixErrorUser(model);
        // new course
        Cursos foundCurso = cursosAPI.findCurso(newCurso);
        if (foundCurso == null) {
            foundCurso = cursosAPI.addDataToNewCourse(newCurso, LoginController.getUsuarioDocente());
        }
        // update course
        else {
            foundCurso = cursosAPI.updateDataFromCurso(foundCurso, newCurso);
        }
        // save to db
        cursosAPI.save(foundCurso);
        return "redirect:/modulos";
    }

    // POST: DELETE COURSE
    @GetMapping("/modulos/curso/delete/{id}")
    public String deleteCurso(@PathVariable Long id) {
        // Encontrar curso
        Cursos curso = cursosAPI.findById(id);
        // obtener lista de los grupos del docente
        List<Grupos> listaGruposDocente = gruposAPI.findGruposByDocente(LoginController.getUsuarioDocente());
        // obtener centro
        Centros centro = curso.getCentro();
        // delete relaciones Alumnos Grupos Curso
        List<AlumnosGruposCurso> relacionesCurso = findAlumnosGruposCursoFromCurso(curso);
        for (AlumnosGruposCurso agc : relacionesCurso) {
            alumnosGruposCursoAPI.delete(agc);
        }
        // delete curso + (registros alumnosGruposCurso, ufs)
        cursosAPI.deleteById(id);
        // comprobar que los grupos no pertenecen a otro curso y borrarlos
        gruposAPI.deleteGruposSinCurso(listaGruposDocente);
        // eliminar centro si no tiene más cursos
        deleteCentroSinCurso(centro);
        return "redirect:/modulos";
    }

    public List<AlumnosGruposCurso> findAlumnosGruposCursoFromCurso(Cursos curso) {
        List<AlumnosGruposCurso> relacionesCurso = new ArrayList<AlumnosGruposCurso>();
        if (curso != null) {
            for (AlumnosGruposCurso agc : alumnosGruposCursoAPI.findAll()) {
                if (agc.getCurso().getId_curso() == curso.getId_curso()) {
                    relacionesCurso.add(agc);
                }
            }
        }
        return relacionesCurso;
    }

    public void deleteCentroSinCurso(Centros centro) {
        if (centro != null) {
            for (Cursos c : cursosAPI.findAll()) {
                if (c.getCentro().getId_centro() == centro.getId_centro()) {
                    return;
                }
            }
            centrosAPI.delete(centro);
        }
    }

    public void tempFixErrorUser(Model model) {
        if (LoginController.getUsuarioDocente() != null) {
            model.addAttribute("usuario", LoginController.getUsuarioDocente().getNombre());
        } else {
            // se cambiará - temporal para controlar errores usuario NULL (testDocenteA por
            // defecto)
            model.addAttribute("usuario", "testDocenteA");
            LoginController.usuarioDocente = docentesAPI.findByUsername("testDocenteA");
        }
    }

    public void resetControllerData() {
        EvaluacionController.selectedGroup = null;
        EvaluacionController.selectedUf = null;
        EvaluacionController.selected_group_saved = "-1";
        EvaluacionController.selected_uf_saved = "-1";
    }
}
