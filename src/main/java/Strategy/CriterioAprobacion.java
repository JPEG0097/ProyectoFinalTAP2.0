package Strategy;

import Model.Alumno;
public interface CriterioAprobacion {
    // La interfaz ahora recibe el promedio como un double
    boolean esAprobado(double promedio);
}