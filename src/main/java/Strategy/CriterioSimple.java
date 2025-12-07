package Strategy;

public class CriterioSimple implements CriterioAprobacion {

    @Override
    public boolean esAprobado(double promedio) { // <-- RECIBE EL PROMEDIO CALCULADO
        return promedio >= 7.0;
    }
}