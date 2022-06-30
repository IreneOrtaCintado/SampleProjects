package com.gestorprogramaciones.service.impl;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import com.gestorprogramaciones.service.api.GenericServiceAPI;

/**
 * Implementación de la interfaz GenericServiceAPI.
 */
@Service
public abstract class GenericServiceImpl<T, ID extends Serializable> implements GenericServiceAPI<T, ID> {

    @Override
    public T save(T entity) {
        return getRespository().save(entity);
    }

    @Override
    public void deleteById(ID id) {
        getRespository().deleteById(id);
    }

    @Override
    public void delete(T entity) {
        getRespository().delete(entity);
    }

    @Override
    public T findById(ID id) {
        Optional<T> obj = getRespository().findById(id);
        if (obj.isPresent())
            return obj.get();
        return null;
    }

    @Override
    public List<T> findAll() {
        List<T> returnList = new ArrayList<>();
        getRespository().findAll().forEach(obj -> returnList.add(obj));
        return returnList;
    }

    @Override
    public Long count() {
        return getRespository().count();
    }

    public abstract JpaRepository<T, ID> getRespository();

}
