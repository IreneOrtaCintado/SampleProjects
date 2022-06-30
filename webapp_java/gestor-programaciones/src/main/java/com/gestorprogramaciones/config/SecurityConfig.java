package com.gestorprogramaciones.config;

import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;

import com.gestorprogramaciones.models.usuarios.Docentes;
import com.gestorprogramaciones.service.UserDocente;

/**
 * Spring Security configuration
 * 
 * https://www.baeldung.com/spring-security-login
 * 
 * https://www.baeldung.com/spring-boot-security-autoconfiguration
 * 
 * https://www.javainuse.com/spring/boot_security_jdbc_authentication_program
 */
@Configuration
@EnableWebSecurity // @EnableWebSecurity: very important if we disable the default security
                   // configuration (when overriding WebSecurityConfigurerAdapter) The application
                   // will fail to start if it's missing.
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    // form html: atributo modelAttribute="user" for registration

    @Autowired
    DataSource dataSource;

    /**
     * Enable jdbc authentication.
     * 
     * @param auth
     * @throws Exception
     */
    @Autowired
    public void configAuthentication(AuthenticationManagerBuilder auth) throws Exception {
        auth.jdbcAuthentication().dataSource(dataSource);
    }

    @Bean
    public JdbcUserDetailsManager jdbcUserDetailsManager() throws Exception {
        JdbcUserDetailsManager jdbcUserDetailsManager = new JdbcUserDetailsManager();
        jdbcUserDetailsManager.setDataSource(dataSource);
        return jdbcUserDetailsManager;
    }

    /**
     * Da acceso a todos los usuarios (incluso no autorizados) para usar todo en
     * "resources".
     */
    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers("/resources/**").anyRequest();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .antMatchers("/new_user").permitAll()
                .antMatchers("/diario").hasAnyRole("USER", "ADMIN", "DOCENTE")
                .antMatchers("/programacion").hasAnyRole("ADMIN")
                .antMatchers("/evaluacion").hasAnyRole("ADMIN", "DOCENTE")
                .anyRequest().authenticated()
                .and()
                .formLogin().loginPage("/app_login").permitAll()
                .and()
                .logout().permitAll();

        http.csrf().disable();
    }

    // @Autowired
    // public void configureGlobal(AuthenticationManagerBuilder authenticationMgr)
    // throws Exception {
    // authenticationMgr.inMemoryAuthentication().withUser("admin").password("admin").authorities("ROLE_USER").and()
    // .withUser("javainuse").password("javainuse").authorities("ROLE_USER",
    // "ROLE_ADMIN");
    // }

    /////////////////////
    /**
     * Authentication manager: users, passwords and roles
     */
    /*
     * @Override
     * protected void configure(final AuthenticationManagerBuilder auth) throws
     * Exception {
     * // authentication manager in memory
     * 
     * auth.inMemoryAuthentication()
     * .withUser("testAlumnoA")
     * .password(passwordEncoder().encode("passA"))
     * .roles("USER")
     * .and()
     * .withUser("testDocenteA")
     * .password(passwordEncoder().encode("testDocenteA"))
     * .roles("DOCENTE")
     * .and()
     * .withUser("proyecto")
     * .password(passwordEncoder().encode("1234"))
     * .roles("USER")
     * .and()
     * .withUser("admin")
     * .password(passwordEncoder().encode("adminPass"))
     * .roles("ADMIN");
     * }
     */

    /**
     * http builder configurations for authorize requests and form login
     * 
     * the more specific rules need to come first, followed by the more general ones
     */
    /*
     * @Override
     * protected void configure(final HttpSecurity http) throws Exception {
     * 
     * http
     * .authorizeRequests().antMatchers("/app_login*").permitAll()
     * .anyRequest()
     * .authenticated()
     * .and()
     * .httpBasic();
     */
    /*
     * http
     * // authorize requests
     * .csrf().disable()
     * .authorizeRequests()
     * .antMatchers("/admin/**").hasRole("ADMIN")
     * .antMatchers("/anonymous*").anonymous()
     * .antMatchers("/app_login*").permitAll()
     * .antMatchers("/new_user*").permitAll()
     * .anyRequest().authenticated()
     * 
     * .and()
     * // form login and logout
     * .formLogin()
     * .loginPage("/app_login.html") // the custom login page
     * .loginProcessingUrl("/app_login/perform_login") // the URL to submit the
     * username and password to (post)
     * .defaultSuccessUrl("/modulos.html", true) // the landing page after a
     * successful login
     * .failureUrl("/app_login.html?error=true") // the landing page after an
     * unsuccessful login
     * // .failureHandler(authenticationFailureHandler())
     * .and()
     * .logout()
     * .logoutUrl("/perform_logout") // the custom logout
     * .deleteCookies("JSESSIONID");
     * // .logoutSuccessHandler(logoutSuccessHandler())
     * ;
     */
    // }

    /*
     * private LogoutSuccessHandler logoutSuccessHandler() {
     * return null;
     * }
     * 
     * private AuthenticationFailureHandler authenticationFailureHandler() {
     * return null;
     * }
     */

    /**
     * Password encoder.
     * 
     * @return
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * https://stackoverflow.com/questions/32244745/how-to-add-new-user-to-spring-security-at-runtime
     */
    /*
     * public void createUserDocente(Docentes docente) {
     * // Create the authorities for the user
     * List<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();
     * authorities.add(new SimpleGrantedAuthority("DOCENTE"));
     * // Instantiate the user (with a class implementing UserDetails)
     * UserDocente userDocente = new UserDocente(docente);
     * userDocente.setAuthorities(authorities); // usar en password:
     * passwordEncoder.encode("s3cr3t")
     * UserDetails user = userDocente;
     * // Save the user somewhere useful. The JdbcUserDetailsManager can save a user
     * to
     * // a database easily.
     * // userDetailsManager.createUser(user);
     * // Create a UsernamePasswordAuthenticationToken
     * Authentication authentication = new UsernamePasswordAuthenticationToken(user,
     * null, authorities);
     * // Add the Authentication to the SecurityContext
     * SecurityContextHolder.getContext().setAuthentication(authentication);
     * }
     */
}