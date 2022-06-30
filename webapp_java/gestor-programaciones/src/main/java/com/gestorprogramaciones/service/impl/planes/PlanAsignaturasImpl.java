package com.gestorprogramaciones.service.impl.planes;

import com.gestorprogramaciones.models.planes.PlanAsignaturas;
import com.gestorprogramaciones.repositories.planes.PlanAsignaturasRepository;
import com.gestorprogramaciones.service.api.planes.PlanAsignaturasAPI;
import com.gestorprogramaciones.service.impl.GenericServiceImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

@Service
public class PlanAsignaturasImpl extends GenericServiceImpl<PlanAsignaturas, Long> implements PlanAsignaturasAPI{

    @Autowired
    private PlanAsignaturasRepository PlanAsignaturasRepository;
    @Override
    public JpaRepository<PlanAsignaturas, Long> getRespository() {
        return PlanAsignaturasRepository;
    }
    
}
