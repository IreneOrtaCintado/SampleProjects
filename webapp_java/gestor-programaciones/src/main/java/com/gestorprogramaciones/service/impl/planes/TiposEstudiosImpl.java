package com.gestorprogramaciones.service.impl.planes;

import com.gestorprogramaciones.models.planes.TiposEstudios;
import com.gestorprogramaciones.repositories.planes.TiposEstudiosRepository;
import com.gestorprogramaciones.service.api.planes.TiposEstudiosAPI;
import com.gestorprogramaciones.service.impl.GenericServiceImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

@Service
public class TiposEstudiosImpl extends GenericServiceImpl<TiposEstudios, Long> implements TiposEstudiosAPI{

    @Autowired
    private TiposEstudiosRepository TiposEstudiosRepository;
    @Override
    public JpaRepository<TiposEstudios, Long> getRespository() {
        return TiposEstudiosRepository;
    }
    
}
