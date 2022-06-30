package com.gestorprogramaciones.controllers;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import com.gestorprogramaciones.models.planes.PlanRas;
import com.gestorprogramaciones.models.planes.PlanUfs;
import com.gestorprogramaciones.models.tablasaux.Centros;
import com.gestorprogramaciones.service.ApartadosCollection;
import com.gestorprogramaciones.service.api.cursos.ActividadesAPI;
import com.gestorprogramaciones.service.api.cursos.CursosAPI;
import com.gestorprogramaciones.service.api.cursos.RasAPI;
import com.gestorprogramaciones.service.api.cursos.TiposActividadesAPI;
import com.gestorprogramaciones.service.api.cursos.UfsAPI;
import com.gestorprogramaciones.service.api.evaluaciones.ApartadosAPI;
import com.gestorprogramaciones.service.api.evaluaciones.PesoActRaAPI;
import com.gestorprogramaciones.service.api.planes.PlanRasAPI;
import com.gestorprogramaciones.service.api.planes.PlanUfsAPI;
import com.gestorprogramaciones.service.api.tablasaux.IdiomasAPI;
import com.gestorprogramaciones.service.api.usuarios.DocentesAPI;
import com.gestorprogramaciones.models.cursos.Actividades;
import com.gestorprogramaciones.models.cursos.Cursos;
import com.gestorprogramaciones.models.cursos.Ras;
import com.gestorprogramaciones.models.cursos.TiposActividades;
import com.gestorprogramaciones.models.cursos.Ufs;
import com.gestorprogramaciones.models.evaluaciones.Apartados;
import com.gestorprogramaciones.models.evaluaciones.PesoActRa;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class ProgramacionController {

    private static Cursos filterCurso;
    private static Centros filterCentro;

    private String filterAnyo;

    private Long selectedCourseId;
    private Long selectedUfId = -1L;

    @Autowired
    private ApartadosAPI apartadosAPI;
    @Autowired
    private PesoActRaAPI pesoActRaAPI;
    @Autowired
    private UfsAPI ufsAPI;
    @Autowired
    private CursosAPI cursosAPI;
    @Autowired
    private PlanUfsAPI planUfsAPI;
    @Autowired
    private PlanRasAPI planRasAPI;
    @Autowired
    private ActividadesAPI actividadesAPI;
    @Autowired
    private RasAPI rasAPI;
    @Autowired
    private TiposActividadesAPI tiposActividadesAPI;
    @Autowired
    private IdiomasAPI idiomas;
    @Autowired
    private DocentesAPI docentesAPI;

    // TODO optimizar el acceso a los datos (menos consultas a la BBDD)
    // TODO link real decreto
    // TODO controlar no crear 2 veces la misma UF

    @GetMapping("/programacion/{id}")
    public String programacionLoad(@PathVariable Long id) {
        System.out.println("ID pasado: " + id);
        filterCurso = cursosAPI.findById(id);
        filterCentro = filterCurso.getCentro();
        filterAnyo = filterCurso.getAnyo();
        // System.out.println("Centro:" + filterCentro.getNombre() + "Anyo: " +
        // filterAnyo);

        return "redirect:/programacion";
    }

    // GET
    @GetMapping("/programacion")
    public String programacion(@RequestParam(defaultValue = "-1") String selectedCourse,
            // @RequestParam(defaultValue = "-1") String selectedUf,
            Model model) {
        // DATA GENERAL
        model.addAttribute("languages", idiomas.findAll());

        if (LoginController.getUsuarioDocente() != null) {
            model.addAttribute("user", LoginController.getUsuarioDocente().getNombre());
        }

        // DATA PROGRAMACION
        model.addAttribute("planUfs", planUfsAPI.findAll());
        model.addAttribute("planRas", planRasAPI.findAll());
        model.addAttribute("tiposActividades", tiposActividadesAPI.findAll());

        model.addAttribute("newUf", new Ufs());
        model.addAttribute("newAct", new Actividades());
        model.addAttribute("newRa", new Ras());
        model.addAttribute("newPesoActRa", new PesoActRa());

        // RUBRICA: CREAR APARTADOS VACIOS
        ApartadosCollection newApartados = new ApartadosCollection();
        int maxApartados = 16;
        for (int i = 0; i < maxApartados; i++) {
            newApartados.addApartado(new Apartados());
        }
        model.addAttribute("newApartadosAct", newApartados);

        // FILTRAR CURSOS

        model.addAttribute("usuario", LoginController.getUsuarioDocente().getNombre());

        if (selectedCourse.equals("-1")) {
            selectedCourseId = filterCurso.getId_curso();
            model.addAttribute("currentCourse", cursosAPI.findById(filterCurso.getId_curso()));
        } else {
            selectedCourseId = Long.parseLong(selectedCourse);
            model.addAttribute("currentCourse", cursosAPI.findById(selectedCourseId));// filterCurso
        }

        // selectedUfId = Long.parseLong(selectedUf);
        model.addAttribute("currentUf", selectedUfId);
        // System.out.println(selectedUf);
        System.out.println(selectedUfId);

        // filtra los módulos del docente, centro y anyo
        List<Cursos> cursosDocente = cursosAPI.buscarCursosDocente(LoginController.getUsuarioDocente());
        model.addAttribute("cursos", cursosAPI.buscarCursosCentroAnyo(cursosDocente, filterCentro, filterAnyo));

        // filtra las ufs, actividades, ras y rubrica del Modulo seleccionado
        model.addAttribute("ufs", ufsAPI.buscarUfsByCursos(selectedCourseId));
        model.addAttribute("rasCurso", rasAPI.buscarRasByCurso(selectedCourseId));
        model.addAttribute("actividadesCurso", actividadesAPI.buscarActividadesByCurso(selectedCourseId));
        model.addAttribute("pesoActRaCurso", pesoActRaAPI.buscarPesoActRasByCurso(selectedCourseId));
        model.addAttribute("apartadosCurso", apartadosAPI.buscarApartadosByCurso(selectedCourseId));

        return "programacion";
    }

    // ********** Ufs *****************

    // POST - CREAR/MODIFICAR UFS
    @PostMapping("programacion/uf/save")
    public String saveUf(Ufs newUf, Model model) {
        // comprobar que se reciben todos los datos
        // System.out.println();
        if (newUf.getId_uf() == null) { // add new uf
            // set curso
            Cursos cursoUf = cursosAPI.findById(newUf.getCurso().getId_curso());
            newUf.setCurso(cursoUf);
            // set plan uf
            PlanUfs planUf = planUfsAPI.findById(newUf.getPlanUf().getId_planUf());
            newUf.setPlanUf(planUf);
            // add to BD
            ufsAPI.save(newUf);
            // geneate RAs
            createRasForUf(newUf);
        } else { // update uf
            Ufs foundUF = ufsAPI.findById(newUf.getId_uf());
            // update data
            foundUF.setHoras_uf(newUf.getHoras_uf());
            foundUF.setInicio_uf(newUf.getInicio_uf());
            foundUF.setFin_uf(newUf.getFin_uf());
            // set plan uf
            PlanUfs newPlanUf = planUfsAPI.findById(newUf.getPlanUf().getId_planUf());
            if (newPlanUf.getId_planUf() != foundUF.getPlanUf().getId_planUf()) {
                foundUF.setPlanUf(newPlanUf);
                // actualizar RAs
                deleteRasFromUf(foundUF);
                createRasForUf(foundUF);
            }
            // update to BD
            ufsAPI.save(foundUF);
        }
        return "redirect:/programacion";
    }

    public void createRasForUf(Ufs uf) {

        if (uf.getPlanUf() != null) {

            PlanUfs planUf = uf.getPlanUf();

            for (PlanRas planRa : planRasAPI.findAll()) {
                System.out.println(planRa.getPlanUf().getId_planUf() + " : " + planUf.getId_planUf());
                if (planRa.getPlanUf().getId_planUf() == planUf.getId_planUf()) {
                    // create with Plan RA and uf
                    Ras newRa = new Ras(planRa, uf);
                    // add to list
                    rasAPI.save(newRa);
                }
            }
        }
    }

    public void deleteRasFromUf(Ufs uf) {
        for (Ras r : uf.getRas()) {
            rasAPI.deleteById(r.getId_ra());
        }
    }

    // POST: DELETE UFS
    @GetMapping("/programacion/uf/delete/{id}")
    public String deleteUf(@PathVariable Long id) {
        ufsAPI.deleteById(id);
        return "redirect:/programacion";
    }

    // ********** Actividades *****************

    // POST - CREAR/MODIFICAR ACTIVIDADES
    @PostMapping("programacion/activity/save")
    public String saveAct(Actividades newAct, Model model) {

        if (newAct.getId_act() == null) { // add new activity
            // set uf
            Ufs ufActivity = ufsAPI.findById(newAct.getUf().getId_uf());
            newAct.setUf(ufActivity);
            // set Tipo
            TiposActividades tipoActivity = tiposActividadesAPI.findById(newAct.getTipoActividad().getId_tipo_act());
            newAct.setTipoActividad(tipoActivity);

            actividadesAPI.save(newAct);

        } else { // update activity
            Actividades foundAct = actividadesAPI.findById(newAct.getId_act());
            // update data
            foundAct.setNombre(newAct.getNombre());
            foundAct.setCod_act(newAct.getCod_act());
            foundAct.setInicio_act(newAct.getInicio_act());
            foundAct.setFin_act(newAct.getFin_act());
            foundAct.setDescripcion_act(newAct.getDescripcion_act());

            Ufs ufActivity = ufsAPI.findById(newAct.getUf().getId_uf());
            foundAct.setUf(ufActivity);

            TiposActividades tipoActivity = tiposActividadesAPI.findById(newAct.getTipoActividad().getId_tipo_act());
            foundAct.setTipoActividad(tipoActivity);

            actividadesAPI.save(foundAct);
        }

        return "redirect:/programacion";
    }

    @GetMapping("programacion/activity/delete/{id}")
    public String deleteAct(@PathVariable Long id) {
        actividadesAPI.deleteById(id);
        return "redirect:/programacion";
    }

    // ********** PesoActRa *****************
    @PostMapping("programacion/pesoActRa/save")
    public String savePesoActRa(PesoActRa newPesoActRa, Model model) {
        Actividades actividad = actividadesAPI.findById(newPesoActRa.getActividad().getId_act());
        Ras ra = rasAPI.findById(newPesoActRa.getRa().getId_ra());
        selectedUfId = ra.getUf().getId_uf();
        PesoActRa pesoActRa = pesoActRaAPI.getPesoFromActRa(actividad, ra);
        // nuevo
        if (pesoActRa == null) {
            pesoActRaAPI.save(new PesoActRa(actividad, ra, newPesoActRa.getPorcent_pesoActRa()));
        } else { // actualización
            pesoActRa.setPorcent_pesoActRa(newPesoActRa.getPorcent_pesoActRa());
            pesoActRaAPI.save(pesoActRa);
        }
        return "redirect:/programacion";
    }

    // ********** Apartados *****************
    @PostMapping("programacion/rubrica/save")
    public String saveApartados(ApartadosCollection newApartadosAct, Model model) {
        List<Apartados> apartados = newApartadosAct.getApartados();
        Actividades actividad = actividadesAPI.findById(apartados.get(0).getActividad().getId_act());

        // lista de elementos con datos
        List<Apartados> apartadosConDatos = new ArrayList<Apartados>();
        for (Apartados a : apartados) {
            if (!a.getDescripcion().equals("")) {
                apartadosConDatos.add(a);
            }  
        }
        apartadosAPI.updateApartadosActividad(apartadosConDatos, actividad);

        return "redirect:/programacion";
    }

}
