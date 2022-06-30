package com.gestorprogramaciones.service.impl.cursos;

import com.gestorprogramaciones.models.cursos.TiposActividades;
import com.gestorprogramaciones.repositories.cursos.TiposActividadesRepository;
import com.gestorprogramaciones.service.api.cursos.TiposActividadesAPI;
import com.gestorprogramaciones.service.impl.GenericServiceImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

@Service
public class TiposActividadesImpl extends GenericServiceImpl<TiposActividades, Long> implements TiposActividadesAPI{

    @Autowired
    private TiposActividadesRepository TiposActividadesRepository;
    @Override
    public JpaRepository<TiposActividades, Long> getRespository() {
        return TiposActividadesRepository;
    }
    
}