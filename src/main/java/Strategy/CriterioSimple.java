package Strategy;

import Model.Alumno;

public class CriterioSimple implements CriterioAprobacion {

    @Override
    public boolean esAprobado(double promedio) {
        return promedio >= 7.0;
    }
}