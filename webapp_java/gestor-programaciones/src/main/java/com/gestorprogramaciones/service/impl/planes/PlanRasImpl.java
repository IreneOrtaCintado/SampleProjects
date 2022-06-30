package com.gestorprogramaciones.service.impl.planes;

import com.gestorprogramaciones.models.planes.PlanRas;
import com.gestorprogramaciones.repositories.planes.PlanRasRepository;
import com.gestorprogramaciones.service.api.planes.PlanRasAPI;
import com.gestorprogramaciones.service.impl.GenericServiceImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

@Service
public class PlanRasImpl extends GenericServiceImpl<PlanRas, Long> implements PlanRasAPI{

    @Autowired
    private PlanRasRepository PlanRasRepository;
    @Override
    public JpaRepository<PlanRas, Long> getRespository() {
        return PlanRasRepository;
    }
    
}
