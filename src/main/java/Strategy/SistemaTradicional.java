package Strategy;

import Model.Parcial;

import java.util.List;

public class SistemaTradicional implements SistemaEvaluacion {
    @Override
    public double calcularCalificacionFinal(List<Parcial> parciales) {
        return parciales.stream()
                .mapToDouble(Parcial::getCalificacionParcial)
                .average()
                .orElse(0.0);
    }

    @Override
    public boolean estaAprobado(List<Parcial> parciales) {
        return calcularCalificacionFinal(parciales) >= 6.0;
    }
}
