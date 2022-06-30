package com.gestorprogramaciones.controllers;

import java.util.ArrayList;
import java.util.List;

import com.gestorprogramaciones.models.cursos.Actividades;
import com.gestorprogramaciones.models.cursos.AlumnosGruposCurso;
import com.gestorprogramaciones.models.cursos.Cursos;
import com.gestorprogramaciones.models.cursos.Ufs;
import com.gestorprogramaciones.models.evaluaciones.Evaluaciones;
import com.gestorprogramaciones.models.evaluaciones.PesoActRa;
import com.gestorprogramaciones.models.grupos.Grupos;
import com.gestorprogramaciones.models.usuarios.Alumnos;
import com.gestorprogramaciones.models.usuarios.Docentes;
import com.gestorprogramaciones.service.api.cursos.AlumnosGruposCursoAPI;
import com.gestorprogramaciones.service.api.cursos.CursosAPI;
import com.gestorprogramaciones.service.api.cursos.UfsAPI;
import com.gestorprogramaciones.service.api.evaluaciones.EvaluacionesAPI;
import com.gestorprogramaciones.service.api.evaluaciones.PesoActRaAPI;
import com.gestorprogramaciones.service.api.grupos.GruposAPI;
import com.gestorprogramaciones.service.api.tablasaux.IdiomasAPI;
import com.gestorprogramaciones.service.api.usuarios.AlumnosAPI;
import com.gestorprogramaciones.service.api.usuarios.DocentesAPI;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class EvaluacionController {

    // TODO optimizar el acceso a los datos (menos consultas a la BBDD)
    // TODO mostrar mensaje cuando se intenta añadir a un grupo un alumno que ya está en el grupo 

    private Docentes usuarioDocente;
    private Cursos cursoActual;
    private Alumnos newAlumnoGrupo = new Alumnos();
    public static Grupos selectedGroup;
    public static String selected_group_saved;
    // private Long selectedGroupId;
    private List<Grupos> listaGrupos;
    public static Ufs selectedUf;
    public static String selected_uf_saved;
    private List<Ufs> listaUfs;

    @Autowired
    private CursosAPI cursosAPI;
    @Autowired
    private GruposAPI gruposAPI;
    @Autowired
    private AlumnosAPI alumnosAPI;
    @Autowired
    private UfsAPI ufsAPI;
    @Autowired
    private AlumnosGruposCursoAPI alumnosGruposCursoAPI;
    @Autowired
    private IdiomasAPI idiomas;
    @Autowired
    private PesoActRaAPI pesoActRaAPI;
    @Autowired
    private EvaluacionesAPI evaluacionesAPI;

    @GetMapping("/evaluacion/{id}")
    public String evaluacionLoad(@PathVariable Long id) {
        cursoActual = cursosAPI.findById(id);
        return "redirect:/evaluacion";
    }

    @GetMapping("/evaluacion")
    public String evaluacion(@RequestParam(defaultValue = "-1") String selected_group,
            @RequestParam(defaultValue = "-1") String selected_uf,
            @RequestParam(defaultValue = "false") String nuevoAlumnoForm,
            @RequestParam(defaultValue = "-1") Long editGroupId,
            Model model) {
        model.addAttribute("languages", idiomas.findAll());

        if (LoginController.getUsuarioDocente() != null) {
            model.addAttribute("user", LoginController.getUsuarioDocente().getNombre());
        }

        // if (usuarioDocente == null) { 
        //  usuarioDocente = LoginController.getUsuarioDocente();
        //  model.addAttribute("user", usuarioDocente.getNombre());
        // }

        // Curso
        model.addAttribute("currentCourse", cursoActual);

        // Lista grupos
        listaGrupos = gruposAPI.findGruposByCurso(cursoActual);
        model.addAttribute("grupos", listaGrupos);
        // Modal grupos
        if (editGroupId != null && editGroupId != -1) {
            model.addAttribute("newGrupo", gruposAPI.findById(editGroupId));
        } else {
            model.addAttribute("newGrupo", new Grupos());
        }
        // Panel lateral grupos
        if (selected_group_saved == null)
            selected_group_saved = -1 + "";
        else if (!selected_group.equals("-1"))
            selected_group_saved = selected_group;

        if (!listaGrupos.isEmpty()) {
            if (selected_group_saved.equals("-1") || selected_group_saved == null) {
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
        model.addAttribute("newAlumnoGrupoCurso", new AlumnosGruposCurso());

        // Lista Ufs
        listaUfs = ufsAPI.buscarUfsByCursos(cursoActual.getId_curso()); // curso puede ser nulo
        model.addAttribute("ufs", listaUfs);

        // Panel lateral ufs
        if (selected_uf_saved == null)
            selected_uf_saved = -1 + "";
        else if (!selected_uf.equals("-1"))
            selected_uf_saved = selected_uf;

        if (!listaUfs.isEmpty()) {
            if (selected_uf_saved.equals("-1")) {
                selectedUf = listaUfs.get(0);
            } else {
                selectedUf = ufsAPI.findById(Long.parseLong(selected_uf_saved));
            }
            model.addAttribute("currentUfId", selectedUf.getId_uf());
            model.addAttribute("currentUf", selectedUf);
            model.addAttribute("actividadesCurso", selectedUf.getActividades());
        } else {
            model.addAttribute("currentUfId", "");
            model.addAttribute("currentUf", new Ufs());
            model.addAttribute("actividadesCurso", new ArrayList<Actividades>());
        }

        // Alumnos
        List<Alumnos> alumnosGrupoCurso = alumnosGruposCursoAPI.getAlumnosGrupoCurso(cursoActual, selectedGroup);
        model.addAttribute("alumnosGrupoCurso", alumnosGrupoCurso);
        model.addAttribute("newAlumno", newAlumnoGrupo);
        // Display form new Alumno
        model.addAttribute("nuevoAlumnoForm", nuevoAlumnoForm);
        // Peso Act Ra From UF
        model.addAttribute("pesosActRa", pesoActRaAPI.getPesosActividadesRaFromUf(selectedUf));
        // Evalauciones UF
        model.addAttribute("evaluacionesUf", evaluacionesAPI.getAllEvalucionesFromUf(selectedUf));
        return "evaluacion";
    }

    // POST: SAVE GRUPO
    @PostMapping("/evaluacion/grupo/save")
    public String saveGrupo(AlumnosGruposCurso newAlumnoGrupoCurso, Model model) {
        // Crear un grupo con el nombre del form
        Grupos grupo = new Grupos(newAlumnoGrupoCurso.getGrupo().getNombre());
        // Guardar el grupo en la BBDD
        gruposAPI.save(grupo);
        // Buscar el curso en la BBDD
        Cursos curso = cursosAPI.findById(newAlumnoGrupoCurso.getCurso().getId_curso());
        // Buscar usar el alumno SYSTEM para establecer la realción entre cursos y
        // grupos aunque no haya alumnos todavía
        Alumnos alumnoSystem = alumnosAPI.findById((long) -1);
        // Crear relación entre curso y grupo
        alumnosGruposCursoAPI.save(new AlumnosGruposCurso(alumnoSystem, grupo, curso));
        return "redirect:/evaluacion";
    }

    // POST: EDIT GRUPO
    @PostMapping("/evaluacion/grupo/edit")
    public String editGrupo(Grupos grupo, Model model) {
        Grupos foundGroup = gruposAPI.findGrupo(grupo);
        if (foundGroup == null) {
            gruposAPI.save(grupo);
        } else {
            foundGroup.setNombre(grupo.getNombre());
            gruposAPI.save(foundGroup);
        }
        return "redirect:/evaluacion";
    }

    // POST: DELETE GRUPO
    @GetMapping("/evaluacion/grupo/delete/{id}")
    public String deleteGrupo(@PathVariable Long id) {
        //System.out.println(id);
        if(selectedGroup.getId_grupo()==id){
            selected_group_saved = -1 + "";
        }
        gruposAPI.deleteById(id);
        return "redirect:/evaluacion";
    }

    // POST: CHECK DNI
    @PostMapping("/evaluacion/alumno/dni")
    public String checkDniAlumno(Alumnos newAlumno, Model model) {
        // buscar alumno por dni
        Alumnos foundAlumno = alumnosAPI.findAlumnoByDni(newAlumno.getDni_alumno());
        // guardar en atributo nuevo alumno
        if (foundAlumno != null) {
            newAlumnoGrupo = foundAlumno;
        } else {
            newAlumnoGrupo = newAlumno;
        }
        return "redirect:/evaluacion/?nuevoAlumnoForm=true";
    }

    // POST: CANCEL ADD ALUMNO
    @GetMapping("/evaluacion/alumno/cancel_add_alumno")
    public String cancelAddAlumno(Alumnos newAlumno, Model model) {
        // inicializar formulario con objeto alumno vacío
        newAlumnoGrupo = new Alumnos();
        return "redirect:/evaluacion/?nuevoAlumnoForm=false";
    }

    // POST: ADD ALUMNO TO GROUP (AND CREATE IF NOT EXISTS)
    @PostMapping("/evaluacion/alumno/save")
    public String saveAlumno(Alumnos newAlumno, Model model) {
        Alumnos foundAlumno = null;
        if (newAlumno.getId_alumno() != null) {
            foundAlumno = alumnosAPI.findById(newAlumno.getId_alumno());
        }
        if (selectedGroup.getId_grupo() != null && cursoActual.getId_curso() != null) {
            if (foundAlumno == null) {
                // crear alumno
                alumnosAPI.save(newAlumno);
                // agregar al grupo y curso
                alumnosGruposCursoAPI.save(new AlumnosGruposCurso(newAlumno, selectedGroup, cursoActual));
            } else {
                foundAlumno.setNombre(newAlumno.getNombre());
                alumnosAPI.save(foundAlumno);
                // comprobar si el alumno ya está en el grupo/curso
                AlumnosGruposCurso registroAlumno = alumnosGruposCursoAPI.findAlumnoGrupoCurso(foundAlumno, selectedGroup, cursoActual);
                if (registroAlumno == null) {
                    // agregar al grupo y curso
                    alumnosGruposCursoAPI.save(new AlumnosGruposCurso(foundAlumno, selectedGroup, cursoActual));
                }
            }
            newAlumnoGrupo = new Alumnos();
        }
        return "redirect:/evaluacion/?nuevoAlumnoForm=true";
    }

    // POST: UPDATE ALUMNO
    @PostMapping("/evaluacion/alumno/update")
    public String updateAlumno(Alumnos newAlumno, Model model) {
        Alumnos foundAlumno = alumnosAPI.findById(newAlumno.getId_alumno());

        // mismo dni
        if (foundAlumno.getDni_alumno().equals(newAlumno.getDni_alumno())) {
            foundAlumno.setNombre(newAlumno.getNombre());
            alumnosAPI.save(foundAlumno);
        } else {
            // cambio de dni
            // comprobar si hay otro alumno con nuevo dni
            String nuevoDni = newAlumno.getDni_alumno();
            Alumnos alumnoConNuevoDni = alumnosAPI.findAlumnoByDni(nuevoDni);
            // no hay otro alumno con el nuevo dni
            if (alumnoConNuevoDni == null) {
                foundAlumno.setNombre(newAlumno.getNombre());
                foundAlumno.setDni_alumno(nuevoDni);
                alumnosAPI.save(foundAlumno);
            } else {
                // ya exixte un alumno con el nuevo dni
                // TODO: mensaje error
            }
        }
        newAlumnoGrupo = new Alumnos();
        return "redirect:/evaluacion";
    }

    // POST: DELETE ALUMNO
    @GetMapping("/evaluacion/alumno/delete/{id}")
    public String deleteAlumno(@PathVariable Long id) {
        System.out.println("Path encontrado");
        // eliminar alumno del grupo y curso
        Alumnos alumno = alumnosAPI.findById(id);
        System.out.println("Alumno encontrado");
        AlumnosGruposCurso agc = alumnosGruposCursoAPI.findAlumnoGrupoCurso(alumno, selectedGroup, cursoActual);
        System.out.println("registro encontrado");
        alumnosGruposCursoAPI.delete(agc);
        // si el alumno no está registrado en ningún curso/grupo, se elimina de la BBDD
        List<AlumnosGruposCurso> registrosAlumno = alumnosGruposCursoAPI.getGruposCursosFromAlumno(alumno);
        System.out.println("otros registros encontrados");
        if (registrosAlumno.size() == 0) {
            alumnosAPI.deleteById(id);
        }
        return "redirect:/evaluacion";
    }
}
