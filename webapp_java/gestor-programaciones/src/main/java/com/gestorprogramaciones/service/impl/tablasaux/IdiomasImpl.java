package com.gestorprogramaciones.service.impl.tablasaux;

import com.gestorprogramaciones.models.tablasaux.Idiomas;
import com.gestorprogramaciones.repositories.tablasaux.IdiomasRepository;
import com.gestorprogramaciones.service.api.tablasaux.IdiomasAPI;
import com.gestorprogramaciones.service.impl.GenericServiceImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

@Service
public class IdiomasImpl extends GenericServiceImpl<Idiomas, Long> implements IdiomasAPI {

    @Autowired
    private IdiomasRepository idiomasRepository;

    @Override
    public JpaRepository<Idiomas, Long> getRespository() {
        return idiomasRepository;
    }

}
