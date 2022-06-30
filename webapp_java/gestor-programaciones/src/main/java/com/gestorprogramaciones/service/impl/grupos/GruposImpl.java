package com.gestorprogramaciones.service.impl.grupos;

import com.gestorprogramaciones.models.cursos.AlumnosGruposCurso;
import com.gestorprogramaciones.models.cursos.Cursos;
import com.gestorprogramaciones.models.grupos.Grupos;
import com.gestorprogramaciones.models.usuarios.Docentes;
import com.gestorprogramaciones.repositories.grupos.GruposRepository;
import com.gestorprogramaciones.service.api.cursos.AlumnosGruposCursoAPI;
import com.gestorprogramaciones.service.api.grupos.GruposAPI;
import com.gestorprogramaciones.service.impl.GenericServiceImpl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

@Service
public class GruposImpl extends GenericServiceImpl<Grupos, Long> implements GruposAPI {

    @Autowired
    private GruposRepository GruposRepository;
    @Autowired
    private AlumnosGruposCursoAPI alumnosGruposCursoAPI;

    @Override
    public JpaRepository<Grupos, Long> getRespository() {
        return GruposRepository;
    }

    /**
     * Busca el grupo pasado por parámetro en la BBDD usando su id.
     * Si lo enuentra devuelve el objeto de la BBDD, sino, null.
     * 
     * @param grupo
     * @return
     */
    public Grupos findGrupo(Grupos grupo) {
        Grupos groupFound = null;
        if (grupo.getId_grupo() != null && grupo.getId_grupo() != 0)
            groupFound = this.findById(grupo.getId_grupo());
        return groupFound;
    }

    /**
     * Devuelve una lista con los grupos del docente pasado por parámetro.
     * 
     * @param docente
     * @return
     */
    public List<Grupos> findGruposByDocente(Docentes docente) {
        List<Grupos> gruposDocente = new ArrayList<Grupos>();
        List<Grupos> allGrupos = this.findAll();
        for (Grupos g : allGrupos) {
            for (AlumnosGruposCurso agc : g.getAlumnosGruposCursos()) {
                if (agc.getCurso().getDocente().getId_docente().equals(docente.getId_docente())) {
                    if (!gruposDocente.contains(g)) {
                        gruposDocente.add(g);
                    }
                }
            }
        }
        return gruposDocente;
    }

    /**
     * Devuelve una lista con los grupos del curso pasado por parámetro.
     * 
     * @param curso
     * @return
     */
    public List<Grupos> findGruposByCurso(Cursos curso) {
        List<Grupos> gruposCurso = new ArrayList<Grupos>();
        List<Grupos> allGrupos = this.findAll();
        if (curso != null) {
            for (Grupos g : allGrupos) {
                for (AlumnosGruposCurso agc : g.getAlumnosGruposCursos()) {
                    if (agc.getCurso().getId_curso() == curso.getId_curso()) {
                        if (!gruposCurso.contains(g)) {
                            gruposCurso.add(g);
                        }
                    }
                }
            }
        }
        return gruposCurso;
    }

    /**
     * Elimina los grupos de la lista pasada si no tienen ningún curso asociado en la tabla AlumnosGruposCurso
     */
    public void deleteGruposSinCurso(List<Grupos> listaGrupos) {
        for (Grupos g : listaGrupos) {
            if (alumnosGruposCursoAPI.findGroupCoursesByGroup(g).isEmpty()) {
                this.deleteById(g.getId_grupo());
            }
        }
    }
}
