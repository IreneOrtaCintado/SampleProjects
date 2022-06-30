package com.gestorprogramaciones.service.impl.grupos;

import com.gestorprogramaciones.models.grupos.Eventos;
import com.gestorprogramaciones.repositories.grupos.EventosRepository;
import com.gestorprogramaciones.service.api.grupos.EventosAPI;
import com.gestorprogramaciones.service.impl.GenericServiceImpl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

@Service
public class EventosImpl extends GenericServiceImpl<Eventos, Long> implements EventosAPI {

    @Autowired
    private EventosRepository EventosRepository;

    @Override
    public JpaRepository<Eventos, Long> getRespository() {
        return EventosRepository;
    }

    /**
     * Devuelve una lista con todos los eventos del grupo pasado por parámetro.
     * 
     * @param groupId
     * @return
     */
    public List<Eventos> searchGroupEvents(Long groupId) {
        List<Eventos> groupEvents = new ArrayList<Eventos>();
        for (Eventos e : this.findAll()) {
            if (e.getGrupo().getId_grupo() == groupId)
                groupEvents.add(e);
        }
        return groupEvents;
    }

    /**
     * Busca el evento pasado por parámetro en la BBDD usando su id.
     * Si lo enuentra devuelve el objeto de la BBDD, sino, null.
     * 
     * @param evento
     * @return
     */
    public Eventos findEvento(Eventos evento) {
        Eventos eventFound = null;
        if (evento.getId_evento() != null && evento.getId_evento() != 0)
            eventFound = this.findById(evento.getId_evento());
        return eventFound;
    }

}
