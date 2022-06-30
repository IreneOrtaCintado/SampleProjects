package com.gestorprogramaciones.service.api.usuarios;

import com.gestorprogramaciones.models.usuarios.Docentes;
import com.gestorprogramaciones.service.api.GenericServiceAPI;

public interface DocentesAPI extends GenericServiceAPI<Docentes, Long>{

    boolean uniqueUserNameDocente(String userName);

    Docentes validateDniDocente(Docentes docente);

    Docentes findByUsername(String userName);
    Docentes findByDNI(String dni);
}
