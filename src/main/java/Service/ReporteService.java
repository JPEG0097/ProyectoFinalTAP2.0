package Service;

import DAO.AlumnoDAO;
import DAO.GrupoDAO;
import Factory.MysqlDaoFact;
import Model.*;
import java.util.List;

public class ReporteService {
    private final AlumnoDAO alumnoDAO;
    private final GrupoDAO grupoDAO;

    public ReporteService() {
        this.alumnoDAO = new MysqlDaoFact().createAlumnoDAO();
        this.grupoDAO = new MysqlDaoFact().createGrupoDAO();
    }

    public void generarReporteGrupo(int idGrupo, String formato) {
        // Generar reporte por grupo en diferentes formatos
    }

    public void generarReporteEstadisticas() {
        // Estadísticas generales del sistema
    }
}