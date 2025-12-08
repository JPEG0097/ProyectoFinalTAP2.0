package Util;

import java.time.LocalDate;
import java.util.regex.Pattern;

public class Validador {
    private static final Pattern EMAIL_PATTERN =
            Pattern.compile("^[A-Za-z0-9+_.-]+@(.+)$");

    private static final Pattern MATRICULA_PATTERN =
            Pattern.compile("^[A-Za-z0-9]{6,10}$");

    public static boolean validarEmail(String email) {
        return email != null && EMAIL_PATTERN.matcher(email).matches();
    }

    public static boolean validarMatricula(String matricula) {
        return matricula != null && MATRICULA_PATTERN.matcher(matricula).matches();
    }

    public static boolean validarRangoCalificacion(double calificacion) {
        return calificacion >= 0 && calificacion <= 10;
    }

    public static boolean validarFecha(LocalDate fecha) {
        return fecha != null && !fecha.isAfter(LocalDate.now());
    }
}