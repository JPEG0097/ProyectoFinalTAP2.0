package Service;

import DAO.*;
import Factory.MysqlDaoFact;
import Model.Alumno;
import Model.Inscripcion;
import Model.Parcial;
import java.util.List;

public class AlumnoService {
    private final AlumnoDAO alumnoDAO;
    private final InscripcionDAO inscripcionDAO;
    private final ParcialDAO parcialDAO;

    public AlumnoService() {
        this.alumnoDAO = new MysqlDaoFact().createAlumnoDAO();
        this.inscripcionDAO = new MysqlDaoFact().createInscripcionDAO();
        this.parcialDAO = new MysqlDaoFact().createParcialDAO();
    }

    public void matricularAlumnoEnMateria(int idAlumno, int idMateria) {
        // Lógica de negocio para matrícula
        // Verificar límites de créditos, prerequisitos, etc.
    }

    public List<Inscripcion> obtenerMateriasCursando(int idAlumno) {
        // Obtener materias activas
        return List.of();
    }

    public double calcularPromedioPorSemestre(int idAlumno, int semestre) {
        // Cálculo específico por semestre
        return 0;
    }
}