package com.gestorprogramaciones.service.impl.grupos;

import com.gestorprogramaciones.models.grupos.TiposEventos;
import com.gestorprogramaciones.repositories.grupos.TiposEventosRepository;
import com.gestorprogramaciones.service.api.grupos.TiposEventosAPI;
import com.gestorprogramaciones.service.impl.GenericServiceImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

@Service
public class TiposEventosImpl extends GenericServiceImpl<TiposEventos, Long> implements TiposEventosAPI{

    @Autowired
    private TiposEventosRepository TiposEventosRepository;
    @Override
    public JpaRepository<TiposEventos, Long> getRespository() {
        return TiposEventosRepository;
    }
    
}
