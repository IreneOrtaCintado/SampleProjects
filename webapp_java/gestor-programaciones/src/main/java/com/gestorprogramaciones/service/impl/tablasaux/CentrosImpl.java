package com.gestorprogramaciones.service.impl.tablasaux;

import com.gestorprogramaciones.models.tablasaux.Centros;
import com.gestorprogramaciones.repositories.tablasaux.CentrosRepository;
import com.gestorprogramaciones.service.api.tablasaux.CentrosAPI;
import com.gestorprogramaciones.service.impl.GenericServiceImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

@Service
public class CentrosImpl extends GenericServiceImpl<Centros, Long> implements CentrosAPI {

    @Autowired
    private CentrosRepository centrosRepository;

    @Override
    public JpaRepository<Centros, Long> getRespository() {
        return centrosRepository;
    }

    /**
     * Añade un centro con los datos pasdos por aparámetro a la BBDD. En caso de que
     * ya exista un centro con el id indicado, obtiene sus datos.
     * 
     * Devuelve el centro creado o el encontrado.
     * 
     * @param idCentro
     * @param nombre
     * @param email
     * @return
     */
    public Centros addCentro(Long idCentro, String nombre, String email) {
        Centros centroCurso;
        // nuevo centro
        if (idCentro < -1) {
            // add to DB
            centroCurso = this.save(new Centros(nombre, email));
        }
        // centro existente
        else {
            // find in DB
            centroCurso = this.findById(idCentro);
        }
        return centroCurso;
    }
}
