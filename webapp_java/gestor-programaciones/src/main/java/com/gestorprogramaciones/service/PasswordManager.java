package com.gestorprogramaciones.service;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.springframework.stereotype.Service;

@Service
public class PasswordManager {

    /**
     * Comprueba que la contraseña es igual en ambos campos.
     * 
     * @param pass_1
     * @param pass_2
     * @return
     */
    public static boolean checkRepeatedPassword(String pass_1, String pass_2) {
        boolean passwordsEqual = pass_1.equals(pass_2);
        return passwordsEqual;
    }

    /**
     * Comprueba que la contraseña cumple con las condiciones requeridas.
     * 
     * @param password
     * @return
     */
    public static boolean checkPasswordValid(String password) {
        boolean minLengthCheck = password.length() >= 8;

        return minLengthCheck;
    }

    /**
     * Obtiene el hash de la cadena pasada por parámetro.
     * 
     * @param origin
     * @return
     */
    public static String getHash(String origin) {
        String hash = "";
        byte originBytes[], resumen[];
        try {
            MessageDigest md = MessageDigest.getInstance("SHA");
            originBytes = origin.getBytes();
            md.update(originBytes);
            resumen = md.digest();
            hash = new String(resumen);
        } catch (NoSuchAlgorithmException e) {

            e.printStackTrace();
        }
        return hash;
    }

    /**
     * Se obtiene el hash de la segunda cadena pasada por parámetro y se compara si
     * es igual a la primera cadena.
     * 
     * @param storedPasswordHash Cadena hash almacenada en la BBDD.
     * @param password           Contraseña introducida por el usuario.
     * @return
     */
    public static boolean checkPassword(String storedPasswordHash, String password) {
        return storedPasswordHash.equals(getHash(password));
    }

}
