package com.gestorprogramaciones.service.impl.planes;

import com.gestorprogramaciones.models.planes.PlanEstudios;
import com.gestorprogramaciones.repositories.planes.PlanEstudiosRepository;
import com.gestorprogramaciones.service.api.planes.PlanEstudiosAPI;
import com.gestorprogramaciones.service.impl.GenericServiceImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

@Service
public class PlanEstudiosImpl extends GenericServiceImpl<PlanEstudios, Long> implements PlanEstudiosAPI{

    @Autowired
    private PlanEstudiosRepository PlanEstudiosRepository;
    @Override
    public JpaRepository<PlanEstudios, Long> getRespository() {
        return PlanEstudiosRepository;
    }
    
}
