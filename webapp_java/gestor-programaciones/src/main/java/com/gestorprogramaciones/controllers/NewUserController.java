package com.gestorprogramaciones.controllers;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

import com.gestorprogramaciones.models.usuarios.Alumnos;
import com.gestorprogramaciones.models.usuarios.Docentes;
import com.gestorprogramaciones.service.PasswordManager;
import com.gestorprogramaciones.service.api.tablasaux.IdiomasAPI;
import com.gestorprogramaciones.service.api.usuarios.AlumnosAPI;
import com.gestorprogramaciones.service.api.usuarios.DocentesAPI;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class NewUserController {

    // TODO redireccionar alumnos a la página para los alumnos

    String currentNewUserType = "docente";
    String dniValidated = "";
    Docentes docenteEncontrado;
    Alumnos alumnoEncontrado;

    @Autowired
    private IdiomasAPI idiomas;
    @Autowired
    private DocentesAPI docentesAPI;
    @Autowired
    private AlumnosAPI alumnosAPI;

    @Autowired
    JdbcUserDetailsManager jdbcUserDetailsManager;

    // GET
    @GetMapping(value = { "/new_user" })
    public String new_user(@RequestParam(defaultValue = "docente") String newUserType, Model model) {
        // language
        model.addAttribute("languages", idiomas.findAll());
        // users
        model.addAttribute("nuevoDocente", new Docentes());
        model.addAttribute("nuevoAlumno", new Alumnos());
        model.addAttribute("docenteUsernameValidado", "");
        model.addAttribute("alumnoUsernameValidado", "");
        model.addAttribute("docenteDniValidated", "");
        model.addAttribute("alumnoDniValidated", "");
        model.addAttribute("validationDniDocente", false);
        model.addAttribute("validationDniAlumno", false);

        if (docenteEncontrado != null) {
            model.addAttribute("nuevoDocente", docenteEncontrado);
            model.addAttribute("validationDniDocente", false);
            model.addAttribute("disable_form", false);
            model.addAttribute("dniValidated", dniValidated);
        } else if (alumnoEncontrado != null) {
            model.addAttribute("nuevoAlumno", alumnoEncontrado);
            model.addAttribute("validationDniAlumno", false);
            model.addAttribute("disable_form", false);
            model.addAttribute("dniValidated", dniValidated);
        } else {
            // ??
        }
        dniValidated = "";

        if (newUserType == null) {
            newUserType = currentNewUserType;
        }

        model.addAttribute("currentNewUserType", newUserType);
        currentNewUserType = newUserType;
        model.addAttribute("disable_form", true);
        model.addAttribute("dniValidated", dniValidated);
        return "new_user";
    }

    // POST - CHECK DNI DOCENTE
    @PostMapping("/new_user/check_dni_docente")
    public String validateIdDocente(@ModelAttribute("nuevoDocente") Docentes nuevoDocente,
            BindingResult result, Model model) {
        model.addAttribute("nuevoDocente", new Docentes());
        model.addAttribute("nuevoAlumno", new Alumnos());
        model.addAttribute("docenteUsernameValidado", "");
        model.addAttribute("alumnoUsernameValidado", "");
        model.addAttribute("validationDniDocente", false);
        model.addAttribute("validationDniAlumno", false);
        model.addAttribute("currentNewUserType", currentNewUserType);

        if ((docenteEncontrado = docentesAPI.validateDniDocente(nuevoDocente)) != null) {
            dniValidated = nuevoDocente.getDni_docente();
            model.addAttribute("docenteDniValidated", "is-valid");
            model.addAttribute("validationDniDocente", false);
            model.addAttribute("disable_form", false);
            model.addAttribute("dniValidated", dniValidated);
        } else if (!dniValidated.equals("")) {
            dniValidated = "";
            model.addAttribute("docenteDniValidated", "");
            model.addAttribute("validationDniDocente", false);
            model.addAttribute("disable_form", true);
            model.addAttribute("dniValidated", dniValidated);
        } else {
            dniValidated = "";
            model.addAttribute("docenteDniValidated", "is-invalid");
            model.addAttribute("docenteUsernameValidado", "");
            model.addAttribute("validationDniDocente", true);
            model.addAttribute("disable_form", true);
            model.addAttribute("dniValidated", dniValidated);
        }
        return "new_user";
    }

    // POST - CHECK DNI ALUMNO
    @PostMapping("/new_user/check_dni_alumno")
    public String validateIdAlumno(@ModelAttribute("nuevoAlumno") Alumnos nuevoAlumno, Model model) {

        model.addAttribute("nuevoDocente", new Docentes());
        model.addAttribute("nuevoAlumno", new Alumnos());
        model.addAttribute("docenteUsernameValidado", "");
        model.addAttribute("alumnoUsernameValidado", "");
        model.addAttribute("validationDniDocente", false);
        model.addAttribute("validationDniAlumno", false);
        model.addAttribute("currentNewUserType", currentNewUserType);

        if ((alumnoEncontrado = alumnosAPI.validateDniAlumno(nuevoAlumno)) != null) {
            dniValidated = nuevoAlumno.getDni_alumno();
            model.addAttribute("alumnoDniValidated", "is-valid");
            model.addAttribute("validationDniAlumno", false);
            model.addAttribute("disable_form", false);
            model.addAttribute("dniValidated", dniValidated);
        } else if (!dniValidated.equals("")) {
            dniValidated = "";
            model.addAttribute("alumnoDniValidated", "");
            model.addAttribute("validationDniDocente", false);
            model.addAttribute("disable_form", true);
            model.addAttribute("dniValidated", dniValidated);
        } else {
            dniValidated = "";
            model.addAttribute("alumnoDniValidated", "is-invalid");
            model.addAttribute("alumnoUsernameValidado", "");
            model.addAttribute("validationDniAlumno", true);
            model.addAttribute("disable_form", true);
            model.addAttribute("dniValidated", dniValidated);
        }
        return "new_user";
    }

    // POST - DATA FORM DOCENTE
    @PostMapping("/new_user/create_user_docente")
    public String createUser(@ModelAttribute("nuevoDocente") Docentes nuevoDocente,
            @RequestParam String repeated_password_docente, Model model) {
        boolean usernameExiste, passwordsIguales, passwordValid;

        // authorities to be granted
        List<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();
        authorities.add(new SimpleGrantedAuthority("ROLE_DOCENTE"));
        //  set security user
        User user = new User(nuevoDocente.getUser_docente(), nuevoDocente.getPass_docente(), authorities);

        model.addAttribute("nuevoDocente", new Docentes());
        model.addAttribute("nuevoAlumno", new Alumnos());
        model.addAttribute("docenteUsernameValidado", "");
        model.addAttribute("alumnoUsernameValidado", "");
        model.addAttribute("validationDniDocente", false);
        model.addAttribute("validationDniAlumno", false);
        model.addAttribute("currentNewUserType", currentNewUserType);
        model.addAttribute("disable_form", true);

        usernameExiste = !docentesAPI.uniqueUserNameDocente(nuevoDocente.getUser_docente());
        passwordsIguales = PasswordManager.checkRepeatedPassword(nuevoDocente.getPass_docente(),
                repeated_password_docente);
        passwordValid = PasswordManager.checkPasswordValid(nuevoDocente.getPass_docente());

        // CHECK ERRORS
        if (usernameExiste || !passwordsIguales || !passwordValid) {
            // error username
            if (usernameExiste) {
                model.addAttribute("docenteUsernameValidado", "is-invalid");
            } else {
                model.addAttribute("docenteUsernameValidado", "is-valid");
            }
            // error password
            if (!passwordsIguales) {
                model.addAttribute("docentePasswordValidado", "is-invalid");
            } else {
                if (passwordValid) {
                    model.addAttribute("docentePasswordValidado", "is-valid");
                } else {
                    model.addAttribute("docentePasswordValidado", "is-invalid");
                }
            }
            // set up interface
            model.addAttribute("docenteDniValidated", "");
            model.addAttribute("validationDniDocente", false);
            model.addAttribute("disable_form", false);
            model.addAttribute("dniValidated", dniValidated);
            model.addAttribute("nuevoDocente", nuevoDocente);
            return "new_user";
        }

        // CREATE NEW USER
        if (passwordsIguales) {
            docenteEncontrado.setUser_docente(nuevoDocente.getUser_docente());
            docenteEncontrado.setPass_docente(PasswordManager.getHash(nuevoDocente.getPass_docente()));
            docentesAPI.save(docenteEncontrado);
            LoginController.usuarioDocente = docenteEncontrado;
            // create security user
            //jdbcUserDetailsManager.createUser(user);
            return "redirect:/modulos";
        }
        return "new_user";
    }

    // POST - DATA FORM ALUMNO
    @PostMapping("/new_user/create_user_alumno")
    public String createUser(@ModelAttribute("nuevoAlumno") Alumnos nuevoAlumno,
            @RequestParam String repeated_password_alumno, Model model) {
        boolean usernameExiste, passwordsIguales, passwordValid;

        // authorities to be granted
        List<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();
        authorities.add(new SimpleGrantedAuthority("ROLE_ALUMNO"));
        User user = new User(nuevoAlumno.getUser_alumno(), nuevoAlumno.getPass_alumno(), authorities);

        model.addAttribute("nuevoDocente", new Docentes());
        model.addAttribute("nuevoAlumno", new Alumnos());
        model.addAttribute("docenteUsernameValidado", "");
        model.addAttribute("alumnoUsernameValidado", "");
        model.addAttribute("validationDniDocente", false);
        model.addAttribute("validationDniAlumno", false);
        model.addAttribute("currentNewUserType", currentNewUserType);
        model.addAttribute("disable_form", true);

        usernameExiste = !alumnosAPI.uniqueUserNameAlumno(nuevoAlumno.getUser_alumno());
        passwordsIguales = PasswordManager.checkRepeatedPassword(nuevoAlumno.getPass_alumno(),
                repeated_password_alumno);
        passwordValid = PasswordManager.checkPasswordValid(nuevoAlumno.getPass_alumno());
        // CHECK ERRORS
        if (usernameExiste || !passwordsIguales || !passwordValid) {
            // error username
            if (usernameExiste) {
                model.addAttribute("alumnoUsernameValidado", "is-invalid");
            } else {
                model.addAttribute("alumnoUsernameValidado", "is-valid");
            }
            // error password
            if (!passwordsIguales) {
                model.addAttribute("alumnoPasswordValidado", "is-invalid");
            } else {
                if (passwordValid) {
                    model.addAttribute("alumnoPasswordValidado", "");
                } else {
                    model.addAttribute("alumnoPasswordValidado", "is-invalid");
                }
            }
            // set up interface
            model.addAttribute("alumnoDniValidated", "");
            model.addAttribute("validationDniAlumno", false);
            model.addAttribute("disable_form", false);
            model.addAttribute("dniValidated", dniValidated);
            model.addAttribute("nuevoAlumno", nuevoAlumno);
            return "new_user";
        }

        // CREATE NEW USER
        if (passwordsIguales) {
            alumnoEncontrado.setUser_alumno(nuevoAlumno.getUser_alumno());
            alumnoEncontrado.setPass_alumno(PasswordManager.getHash(nuevoAlumno.getPass_alumno()));
            alumnosAPI.save(alumnoEncontrado);
            LoginController.usuarioAlumno = alumnoEncontrado;
            // create security user
            jdbcUserDetailsManager.createUser(user);
            return "redirect:/app_login";
        }
        return "new_user";
    }
}