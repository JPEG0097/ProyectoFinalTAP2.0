package Strategy;

import Model.Parcial;
import java.util.List;

// Patrón Strategy para diferentes sistemas de evaluación
public interface SistemaEvaluacion {
    double calcularCalificacionFinal(List<Parcial> parciales);
    boolean estaAprobado(List<Parcial> parciales);
}

