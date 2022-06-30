package com.gestorprogramaciones.service.api.tablasaux;

import com.gestorprogramaciones.models.tablasaux.Centros;
import com.gestorprogramaciones.service.api.GenericServiceAPI;

public interface CentrosAPI extends GenericServiceAPI<Centros, Long>{

    public Centros addCentro(Long idCentro, String nombre, String email);
    
}
