package com.gestorprogramaciones.service.impl.planes;

import com.gestorprogramaciones.models.planes.PlanUfs;
import com.gestorprogramaciones.repositories.planes.PlanUfsRepository;
import com.gestorprogramaciones.service.api.planes.PlanUfsAPI;
import com.gestorprogramaciones.service.impl.GenericServiceImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

@Service
public class PlanUfsImpl extends GenericServiceImpl<PlanUfs, Long> implements PlanUfsAPI{

    @Autowired
    private PlanUfsRepository PlanUfsRepository;
    @Override
    public JpaRepository<PlanUfs, Long> getRespository() {
        return PlanUfsRepository;
    }
    
}
