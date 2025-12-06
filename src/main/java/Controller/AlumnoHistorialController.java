package Controller;

import DAO.InscripcionDAO;
import DAO.MateriaDAO;
import DAO.ParcialDAO; // Nuevo DAO para parciales
import Factory.MysqlDaoFact; // Nombre de Factory corregido
import Model.Inscripcion;
import Model.Materia;
import Service.CalificacionesService;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.collections.FXCollections;
import javafx.scene.control.cell.PropertyValueFactory; // Necesario para la nueva columna Estado

import java.util.List;

public class AlumnoHistorialController {

    // Componentes FXML
    @FXML private Label lblPromedioGeneral;
    @FXML private Label lblEstado;
    @FXML private TableView<HistorialItem> tableViewHistorial;
    @FXML private TableColumn<HistorialItem, String> colMateria;
    @FXML private TableColumn<HistorialItem, String> colCodigo;
    @FXML private TableColumn<HistorialItem, Double> colCalificacion;
    @FXML private TableColumn<HistorialItem, String> colEstado; // <-- Columna para el estado individual

    // Dependencias
    private final MysqlDaoFact factory = new MysqlDaoFact(); // Usar el nombre correcto
    private final InscripcionDAO inscripcionDAO = factory.createInscripcionDAO();
    private final MateriaDAO materiaDAO = factory.createMateriaDAO();
    private final ParcialDAO parcialDAO = factory.createParcialDAO(); // Usar ParcialDAO
    private final CalificacionesService calificacionService = new CalificacionesService();

    private int idAlumnoLogueado;

    /**
     * Clase auxiliar (Modelo temporal) para combinar datos de Inscripción, Materia y Calificación.
     */
    public static class HistorialItem {
        private String materia;
        private String codigo;
        private double promedioFinal; // Usamos 'promedioFinal' para la nota
        private String estado;      // Estado individual

        public HistorialItem(String materia, String codigo, double promedioFinal, String estado) {
            this.materia = materia;
            this.codigo = codigo;
            this.promedioFinal = promedioFinal;
            this.estado = estado;
        }
        // Getters para PropertyValueFactory
        public String getMateria() { return materia; }
        public String getCodigo() { return codigo; }
        public double getPromedioFinal() { return promedioFinal; } // Corregido: getter ahora es PromedioFinal
        public String getEstado() { return estado; }
    }

    /**
     * Método público para inyectar el ID del alumno desde el LoginController.
     */
    public void setAlumnoId(int idAlumno) {
        this.idAlumnoLogueado = idAlumno;
        cargarDatosHistorial();
    }

    @FXML
    public void initialize() {
        // 1. Configurar las celdas de la tabla para mapear la clase HistorialItem
        colMateria.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getMateria()));
        colCodigo.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getCodigo()));

        // Mapeo de la nota: ahora usamos getPromedioFinal
        colCalificacion.setCellValueFactory(data -> new javafx.beans.property.SimpleDoubleProperty(data.getValue().getPromedioFinal()).asObject());

        // Mapeo de la columna de estado
        colEstado.setCellValueFactory(new PropertyValueFactory<>("estado"));

        // La carga de datos real se realiza en setAlumnoId, que se llama después de la inicialización.
    }

    /**
     * Carga el promedio, estado y la lista de calificaciones del alumno logueado.
     */
    private void cargarDatosHistorial() {
        if (idAlumnoLogueado == 0) return; // Salir si el ID no se inyectó

        // 1. Calcular Promedio y Estado GLOBAL (Lógica de Negocio)
        double promedioGlobal = calificacionService.calcularPromedioGeneral(idAlumnoLogueado);
        boolean aprobadoGlobal = calificacionService.esAprobado(idAlumnoLogueado);

        // Mostrar resumen GLOBAL
        lblPromedioGeneral.setText(String.format("%.2f", promedioGlobal));
        lblEstado.setText(aprobadoGlobal ? "APROBADO" : "REPROBADO");
        lblEstado.setStyle(aprobadoGlobal ? "-fx-text-fill: green; -fx-font-weight: bold;" : "-fx-text-fill: red; -fx-font-weight: bold;");

        // 2. Obtener todas las inscripciones del alumno
        List<Inscripcion> inscripciones = inscripcionDAO.getByAlumnoId(idAlumnoLogueado);

        // 3. Transformar inscripciones en objetos HistorialItem (Calculando nota y estado INDIVIDUAL)
        List<HistorialItem> historial = inscripciones.stream()
                .map(ins -> {
                    Materia materia = materiaDAO.getById(ins.getIdMateria());

                    // CALCULA EL PROMEDIO DE LOS 4 PARCIALES
                    double promedioMateria = calificacionService.calcularPromedioMateria(ins.getIdInscripcion());

                    // EVALÚA EL ESTADO INDIVIDUAL
                    String estadoMateria = calificacionService.esMateriaAprobada(promedioMateria) ? "APROBADO" : "REPROBADO";

                    String nombreMateria = (materia != null) ? materia.getNombre() : "Materia No Encontrada";
                    String codigoMateria = (materia != null) ? materia.getCodigoMateria() : "N/A";

                    // Crea la fila con el promedio final de la materia y su estado
                    return new HistorialItem(nombreMateria, codigoMateria, promedioMateria, estadoMateria);
                })
                .toList();

        // 4. Mostrar en la tabla
        tableViewHistorial.setItems(FXCollections.observableArrayList(historial));
    }
}