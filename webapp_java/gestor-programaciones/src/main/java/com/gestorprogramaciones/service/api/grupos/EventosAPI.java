package com.gestorprogramaciones.service.api.grupos;

import java.util.List;

import com.gestorprogramaciones.models.grupos.Eventos;
import com.gestorprogramaciones.service.api.GenericServiceAPI;

public interface EventosAPI extends GenericServiceAPI<Eventos, Long>{
    
    public List<Eventos> searchGroupEvents(Long groupId);
    
    public Eventos findEvento(Eventos evento);
}
