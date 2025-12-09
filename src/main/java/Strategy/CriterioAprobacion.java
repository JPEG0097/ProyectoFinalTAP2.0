package Strategy;

import Model.Alumno;
public interface CriterioAprobacion {
    boolean esAprobado(double promedio);
}