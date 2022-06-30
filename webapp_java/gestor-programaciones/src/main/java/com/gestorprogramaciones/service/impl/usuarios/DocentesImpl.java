package com.gestorprogramaciones.service.impl.usuarios;

import java.util.List;

import com.gestorprogramaciones.models.usuarios.Docentes;
import com.gestorprogramaciones.repositories.usuarios.DocentesRepository;
import com.gestorprogramaciones.service.api.usuarios.DocentesAPI;
import com.gestorprogramaciones.service.impl.GenericServiceImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

@Service
public class DocentesImpl extends GenericServiceImpl<Docentes, Long> implements DocentesAPI {

    @Autowired
    private DocentesRepository docentesRepository;

    @Override
    public JpaRepository<Docentes, Long> getRespository() {
        return docentesRepository;
    }

    /**
     * Devuelve un boleano indicando si el nombre de usuario pasado por parámetro ya
     * está siendo usado por otro docente.
     */
    @Override
    public boolean uniqueUserNameDocente(String userName) {

        List<Docentes> docentes = this.findAll();

        for (Docentes d : docentes) {
            if (d.getUser_docente() != null && d.getUser_docente().equalsIgnoreCase(userName)) {
                return false;
            }
        }
        return true;
    }

    /**
     * Comprueba que el dni del alumno está registrado en el sistema y
     * que aún no tiene un usuario.
     * 
     * Devuelve un objeto con el docente encontrado o null si no lo encuentra.
     * 
     * @param docente
     * @return
     */
    @Override
    public Docentes validateDniDocente(Docentes docente) {

        for (Docentes d : this.findAll()) {
            if (d.getDni_docente() != null) {

                if (d.getDni_docente().equals(docente.getDni_docente())) {
                    if (d.getUser_docente() == null) {
                        return d;
                    } else
                        return null;
                }
            }
        }
        return null;
    }

    /**
     * Devuelve un docente de la base de datos con el nombre de usuario pasado por
     * parámetro.
     * 
     * Devuelve NULL si no existe ninguno con ese usuario.
     */
    @Override
    public Docentes findByUsername(String userName) {
        List<Docentes> docentes = this.findAll();

        for(Docentes d: docentes){
            if(d.getUser_docente() != null && d.getUser_docente().equals(userName)){
                return d;
            }
        }

        return null;
    }

    /**
     * Devuelve un docente de la base de datos con el nombre de usuario pasado por
     * parámetro.
     * 
     * Devuelve NULL si no existe ninguno con ese usuario.
     */
    @Override
    public Docentes findByDNI(String dni) {
        List<Docentes> docentes = this.findAll();

        for(Docentes d: docentes){
            if(d.getDni_docente() != null && d.getDni_docente().equals(dni)){
                return d;
            }
        }
        
        return null;
    }

}