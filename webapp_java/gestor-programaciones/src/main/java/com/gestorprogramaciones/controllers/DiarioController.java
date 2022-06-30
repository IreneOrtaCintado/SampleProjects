package com.gestorprogramaciones.controllers;

import java.util.ArrayList;
import java.util.List;

import com.gestorprogramaciones.models.cursos.Cursos;
import com.gestorprogramaciones.models.grupos.Eventos;
import com.gestorprogramaciones.models.grupos.Grupos;
import com.gestorprogramaciones.models.usuarios.Docentes;
import com.gestorprogramaciones.service.api.cursos.CursosAPI;
import com.gestorprogramaciones.service.api.grupos.EventosAPI;
import com.gestorprogramaciones.service.api.grupos.GruposAPI;
import com.gestorprogramaciones.service.api.grupos.TiposEventosAPI;
import com.gestorprogramaciones.service.api.tablasaux.IdiomasAPI;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class DiarioController {

    //  TODO optimizar el acceso a los datos (menos consultas a la BBDD)
    //  TODO dropdown tipo de evento, clase por defecto o vacío?
    //  TODO modificar titulo modales

    private static Cursos currentCurso;
    private static Docentes usuarioDocente;
    private Grupos selectedGroup;

    @Autowired
    private GruposAPI gruposAPI;
    @Autowired
    private EventosAPI eventosAPI;
    @Autowired
    private TiposEventosAPI tiposEventosAPI;
    @Autowired
    private CursosAPI cursosAPI;
    @Autowired
    private IdiomasAPI idiomas;

    @GetMapping("/diario/{id}")
    public String diarioLoad(@PathVariable Long id) {
        currentCurso = cursosAPI.findById(id);
        usuarioDocente = LoginController.usuarioDocente;
        return "redirect:/diario";
    }

    // GET
    @GetMapping("/diario")
    public String diario(@RequestParam(defaultValue = "-1") String selected_group,
            @RequestParam(defaultValue = "-1") Long editGroupId,
            @RequestParam(defaultValue = "-1") Long id_evento_delete,
            Model model) {
        // language
        model.addAttribute("languages", idiomas.findAll());
        // mostrar curso
        if(currentCurso==null){
            return "diario";
        }
        String codigoModulo = currentCurso.getPlanAsignatura().getCod_planAsignatura();
        String codigoEstudios = currentCurso.getPlanAsignatura().getPlanEstudio().getCod_planEstudio();
        String anyoModulo = currentCurso.getAnyo();
        model.addAttribute("codigoModulo", codigoModulo);
        model.addAttribute("codigoEstudios", codigoEstudios);
        model.addAttribute("anyoModulo", anyoModulo);
        
        if (LoginController.getUsuarioDocente() != null) {
            model.addAttribute("user", LoginController.getUsuarioDocente().getNombre());
        }
        /*
         * if (usuarioDocente == null)
         * usuarioDocente = LoginController.getUsuarioDocente();
         * model.addAttribute("user", usuarioDocente.getNombre());
         */

        // modal editar grupo
        if (editGroupId != null && editGroupId != -1) {
            model.addAttribute("newGrupo", gruposAPI.findById(editGroupId));
        } else {
            model.addAttribute("newGrupo", new Grupos());
        }

        // buscar grupos curso
        List<Grupos> listaGrupos = gruposAPI.findGruposByCurso(currentCurso);
        model.addAttribute("grupos", listaGrupos);

        // mostrar información del grupo y sus eventos
        if (!listaGrupos.isEmpty()) {
            if (selected_group.equals("-1")) {
                // seleccionar primer grupo por defecto
                selectedGroup = listaGrupos.get(0);
            } else {
                selectedGroup = gruposAPI.findById(Long.parseLong(selected_group));
            }
            // info grupo
            model.addAttribute("currentGroupId", selectedGroup.getId_grupo());
            model.addAttribute("currentGroup", selectedGroup);
            // info eventos
            model.addAttribute("eventos", eventosAPI.searchGroupEvents(selectedGroup.getId_grupo()));

            //System.out.println(selectedGroup.getId_grupo());
        } else {
            // info grupo
            model.addAttribute("currentGroupId", "");
            model.addAttribute("currentGroup", new Grupos());
            // info eventos
            model.addAttribute("eventos", new ArrayList<Eventos>());
        }
        // modal eventos
        model.addAttribute("eventoFormObj", new Eventos());
        model.addAttribute("tiposEventos", tiposEventosAPI.findAll());
        return "/diario";
    }

    // POST: CREATE/MODIFY EVENTO
    @PostMapping("/diario/evento/save")
    public String saveEvento(Eventos evento, Model model) {
        Eventos foundEvent = eventosAPI.findEvento(evento);
        if (foundEvent == null) {
            System.out.println("Evento null");
            evento.setTipoEvento(tiposEventosAPI.findById(evento.getTipoEvento().getId_tipoEvento()));
            evento.setGrupo(gruposAPI.findById(selectedGroup.getId_grupo()));
            if (evento.getComenta() == null)
                evento.setComenta("");
            if (evento.getHorasReal() == null)
                evento.setHorasReal(0f);
            eventosAPI.save(evento);
        } else {
            System.out.println(evento.getHorasReal());
            foundEvent.setTipoEvento(tiposEventosAPI.findById(evento.getTipoEvento().getId_tipoEvento()));
            foundEvent.setFecha(evento.getFecha());
            foundEvent.setHorasReal(evento.getHorasReal());
            foundEvent.setHorasPrev(evento.getHorasPrev());
            foundEvent.setComenta(evento.getComenta());
            eventosAPI.save(foundEvent);
        }
        return "redirect:/diario";
    }

    // POST: DELETE EVENTO
    @GetMapping("/diario/evento/delete/{id}")
    public String deleteEventoId(@PathVariable Long id) {
        eventosAPI.deleteById(id);
        return "redirect:/diario";
    }

    // POST: MODIFY GRUPO
    @PostMapping("/diario/grupo/save")
    public String saveGrupo(Grupos grupo, Model model) {
        Grupos foundGroup = gruposAPI.findGrupo(grupo);
        if (foundGroup == null) {
            gruposAPI.save(grupo);
        } else {
            foundGroup.setNombre(grupo.getNombre());
            gruposAPI.save(foundGroup);
        }
        return "redirect:/diario";
    }

    // POST: DELETE GRUPO
    @GetMapping("/diario/grupo/delete/{id}")
    public String deleteGrupoId(@PathVariable Long id) {
        //  eliminar grupo - NO FUNCIONA SIEMPRE???
        gruposAPI.deleteById(id);
        return "redirect:/diario";
    }
}
