package com.gestorprogramaciones.service.api;

import java.io.Serializable;
import java.util.List;

/**
 * Esta interfaz emula lo que hace JPARepository. Evita repetir código común, lo
 * que mejora la escalabilidad.
 * 
 * T será el objeto, el tipo de dato.
 * 
 * Esta Interfaz será implementada en cada Service.
 */
public interface GenericServiceAPI<T, ID extends Serializable> {

    T save(T entity);

    void deleteById(ID id);

    void delete(T entity);

    T findById(ID id);

    List<T> findAll();

    Long count();
}