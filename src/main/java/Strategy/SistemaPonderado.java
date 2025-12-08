package Strategy;

import Model.Parcial;

import java.util.List;

public class SistemaPonderado implements SistemaEvaluacion {
    private final double[] ponderaciones = {0.2, 0.3, 0.3, 0.2}; // Para 4 parciales

    @Override
    public double calcularCalificacionFinal(List<Parcial> parciales) {
        double suma = 0;
        for (int i = 0; i < Math.min(parciales.size(), ponderaciones.length); i++) {
            suma += parciales.get(i).getCalificacionParcial() * ponderaciones[i];
        }
        return suma;
    }

    @Override
    public boolean estaAprobado(List<Parcial> parciales) {
        return calcularCalificacionFinal(parciales) >= 7.0;
    }
}
