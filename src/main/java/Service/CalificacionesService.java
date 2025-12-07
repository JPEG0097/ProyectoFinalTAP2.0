package Service;

import DAO.InscripcionDAO;
import DAO.ParcialDAO;
import DAO.AlumnoDAO;
import Factory.MysqlDaoFact;
import Model.Inscripcion;
import Model.Parcial;
import Strategy.CriterioAprobacion;
import Strategy.CriterioSimple;
import java.util.List;
import java.util.OptionalDouble;

public class CalificacionesService {

    private final MysqlDaoFact factory = new MysqlDaoFact();
    private final ParcialDAO parcialDAO = factory.createParcialDAO();
    private final InscripcionDAO inscripcionDAO = factory.createInscripcionDAO();
    private final AlumnoDAO alumnoDAO = factory.createAlumnoDAO();

    // Patrón Strategy
    private final CriterioAprobacion criterioActual = new CriterioSimple();
    private static final double NOTA_MINIMA_APROBACION = 7.0; // Usada en la evaluación individual

    // Setter para cambiar la estrategia (opcional, si el profesor lo requiere)
    public void setCriterioAprobacion(CriterioAprobacion nuevoCriterio) {
        // Debes implementar esta lógica si la usas
        // this.criterioActual = nuevoCriterio;
    }

    /**
     * Tarea: Calcula el promedio final de UNA SOLA materia (Inscripción)
     * promediando las notas parciales.
     */
    public double calcularPromedioMateria(int idInscripcion) {
        // 1. Obtener los registros de parciales para esa inscripción
        List<Parcial> parciales = parcialDAO.getParcialesByInscripcionId(idInscripcion);

        if (parciales.isEmpty()) {
            return 0.0;
        }

        // 2. Promediar las notas de los parciales encontrados
        OptionalDouble avg = parciales.stream()
                .mapToDouble(Parcial::getCalificacionParcial)
                .average();

        // Redondea a 2 decimales y maneja el caso de que no haya datos (orElse)
        return Math.round(avg.orElse(0.0) * 100.0) / 100.0;
    }

    /**
     * Tarea: Evalúa si el promedio final de UNA materia es aprobado.
     */
    public boolean esMateriaAprobada(double promedioMateria) {
        // Regla de aprobación individual (ej: 7.0 o más)
        return promedioMateria >= NOTA_MINIMA_APROBACION;
    }

    /**
     * Tarea: Calcula el promedio general del alumno (promedio de los promedios por materia).
     */
    public double calcularPromedioGeneral(int idAlumno) {
        // 1. Obtener todas las inscripciones del alumno
        List<Inscripcion> inscripciones = inscripcionDAO.getByAlumnoId(idAlumno);

        if (inscripciones.isEmpty()) {
            return 0.0;
        }

        // 2. Obtener el promedio de cada materia inscrita y calcular el promedio general
        OptionalDouble promedio = inscripciones.stream()
                // Se usa el método auxiliar para obtener el promedio de los parciales de cada materia
                .mapToDouble(ins -> calcularPromedioMateria(ins.getIdInscripcion()))
                .filter(avg -> avg > 0.0) // Ignorar materias sin notas
                .average();

        return Math.round(promedio.orElse(0.0) * 100.0) / 100.0;
    }

    /**
     * Tarea: Evalúa si el alumno aprueba globalmente (usando el Patrón Strategy).
     */
    public boolean esAprobado(int idAlumno) {
        double promedio = calcularPromedioGeneral(idAlumno);
        // Ejecuta la estrategia de aprobación global (CriterioSimple por defecto)
        return criterioActual.esAprobado(promedio);
    }
}