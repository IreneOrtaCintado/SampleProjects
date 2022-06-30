package com.gestorprogramaciones.service;

import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.gestorprogramaciones.models.usuarios.Docentes;

public class UserDocente  { //implements UserDetails

    private Docentes docente;

    public UserDocente(Docentes docente){
        this.docente = docente;
    }
    
//  SPRING SECURITY
/*     private Collection<? extends GrantedAuthority> authorities;
    private boolean accountNonExpired = false;
    private boolean accountNonLocked = false;
    private boolean credentialsNonExpired = false;
    private boolean enabled = false; */

    /* public void setAuthorities(Collection<? extends GrantedAuthority> authorities) {
        this.authorities = authorities;
	}

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    } */


    public String getPassword() {
        return docente.getPass_docente();
    }


    public String getUsername() {
        return docente.getUser_docente();
    }
/* 
    @Override
    public boolean isAccountNonExpired() {
        return accountNonExpired;
    }

    @Override
    public boolean isAccountNonLocked() {
        return accountNonLocked;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return credentialsNonExpired;
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }
 */
    
}
