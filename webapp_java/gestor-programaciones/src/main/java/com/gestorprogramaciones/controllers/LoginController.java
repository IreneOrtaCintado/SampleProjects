package com.gestorprogramaciones.controllers;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Locale;

import com.gestorprogramaciones.models.usuarios.Alumnos;
import com.gestorprogramaciones.models.usuarios.Docentes;
import com.gestorprogramaciones.service.PasswordManager;
import com.gestorprogramaciones.service.api.tablasaux.IdiomasAPI;
import com.gestorprogramaciones.service.api.usuarios.AlumnosAPI;
import com.gestorprogramaciones.service.api.usuarios.DocentesAPI;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class LoginController {

    // dirección en: MvcConfig, SecurityConfig
    static Docentes usuarioDocente;
    static Alumnos usuarioAlumno;
    String currentUserType;

    @Autowired
    private DocentesAPI docentesAPI;
    @Autowired
    private AlumnosAPI alumnosAPI;
    @Autowired
    private IdiomasAPI idiomas;

    // TODO security
    // TODO mensajes de error usuario o contraseña incorrectos
    // TODO opción olvidó contraseña

    public static Docentes getUsuarioDocente() {
        return usuarioDocente;
    }
    public static Alumnos getUsuarioAlumno() {
        return usuarioAlumno;
    }

    // GET
    @GetMapping(value = { "/", "/app_login" })
    public String loginGet(@RequestParam(defaultValue = "docente") String userType, String error, String logout, Model model) {
        model.addAttribute("languages", idiomas.findAll());
        model.addAttribute("login_docente", new Docentes());
        model.addAttribute("login_alumno", new Alumnos());
        model.addAttribute("currentUserType", userType);
        currentUserType = userType;
        // System.out.println(currentUserType);

        if (error != null)
			model.addAttribute("errorMsg", "Your username and password are invalid.");

		if (logout != null)
			model.addAttribute("msg", "You have been logged out successfully.");
            
        return "app_login";
    }

    // POST
    @PostMapping("/app_login/perform_login")
    public String login(@ModelAttribute("login_docente") Docentes login_docente,
            @ModelAttribute("login_alumno") Alumnos login_alumno,
            Model model) {
        String address;

        model.addAttribute("login_docente", new Docentes());
        model.addAttribute("login_alumno", new Alumnos());
        model.addAttribute("currentUserType", currentUserType);

        if (currentUserType.equals("docente"))
            address = validateLoginDocente(login_docente);
        else if (currentUserType.equals("alumno"))
            address = validateLoginAlumno(login_alumno);
        else
            address = "app_login";
        return address;
    }

    private String validateLoginDocente(Docentes docente) {
        boolean userExists = false, passwordCorrect = false;
        for (Docentes d : docentesAPI.findAll()) {
            if (d.getUser_docente() != null && d.getPass_docente() != null) {
                if (d.getUser_docente().equals(docente.getUser_docente())) {
                    userExists = true;
                    if (PasswordManager.checkPassword(d.getPass_docente(), docente.getPass_docente())) {
                        passwordCorrect = true;
                        usuarioDocente = d;
                        return "redirect:/modulos";
                    }
                }
            }
        }
        return "app_login";
    }

    private String validateLoginAlumno(Alumnos alumno) {
        boolean userExists = false, passwordCorrect = false;
        for (Alumnos a : alumnosAPI.findAll()) {
            if (a.getUser_alumno() != null && a.getPass_alumno() != null) {
                if (a.getUser_alumno().equals(alumno.getUser_alumno())) {
                    userExists = true;
                    if (PasswordManager.checkPassword(a.getPass_alumno(), alumno.getPass_alumno())) {
                        usuarioAlumno = a;
                        passwordCorrect = true;
                        return "redirect:/app_login";
                    }
                }
            }
        }
        return "app_login";
    }

    // POST - logout
    @PostMapping("/perform_logout")
    public String logout(@ModelAttribute("login_docente") Docentes login_docente,
            @ModelAttribute("login_alumno") Alumnos login_alumno,
            Model model) {

        return "app_login";
    }
}
