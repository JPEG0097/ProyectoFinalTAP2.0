package Controller;

import DAO.InscripcionDAO;
import DAO.MateriaDAO;
import DAO.ParcialDAO;
import DAO.AlumnoDAO;
import Factory.MysqlDaoFact;
import Model.Alumno;
import Model.Inscripcion;
import Model.Materia;
import Service.CalificacionesService;
import Service.PdfService;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.collections.FXCollections;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.FileChooser;

import java.io.File;
import java.util.List;

public class AlumnoHistorialController {

    @FXML private Label lblPromedioGeneral;
    @FXML private Label lblEstado;
    @FXML private TableView<HistorialItem> tableViewHistorial;
    @FXML private TableColumn<HistorialItem, String> colMateria;
    @FXML private TableColumn<HistorialItem, String> colCodigo;
    @FXML private TableColumn<HistorialItem, Double> colCalificacion;
    @FXML private TableColumn<HistorialItem, String> colEstado;

    private final MysqlDaoFact factory = new MysqlDaoFact();
    private final InscripcionDAO inscripcionDAO = factory.createInscripcionDAO();
    private final MateriaDAO materiaDAO = factory.createMateriaDAO();
    private final ParcialDAO parcialDAO = factory.createParcialDAO();
    private final AlumnoDAO alumnoDAO = factory.createAlumnoDAO();
    private final CalificacionesService calificacionService = new CalificacionesService();

    private int idAlumnoLogueado;

    public static class HistorialItem {
        private String materia;
        private String codigo;
        private double promedioFinal;
        private String estado;

        public HistorialItem(String materia, String codigo, double promedioFinal, String estado) {
            this.materia = materia;
            this.codigo = codigo;
            this.promedioFinal = promedioFinal;
            this.estado = estado;
        }

        public String getMateria() { return materia; }
        public String getCodigo() { return codigo; }
        public double getPromedioFinal() { return promedioFinal; }
        public String getEstado() { return estado; }
    }

    public void setAlumnoId(int idAlumno) {
        this.idAlumnoLogueado = idAlumno;
        cargarDatosHistorial();
    }

    @FXML
    public void initialize() {
        colMateria.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getMateria()));
        colCodigo.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getCodigo()));
        colCalificacion.setCellValueFactory(data -> new javafx.beans.property.SimpleDoubleProperty(data.getValue().getPromedioFinal()).asObject());
        colEstado.setCellValueFactory(new PropertyValueFactory<>("estado"));
    }

    private void cargarDatosHistorial() {
        if (idAlumnoLogueado == 0) return;

        double promedioGlobal = calificacionService.calcularPromedioGeneral(idAlumnoLogueado);
        boolean aprobadoGlobal = calificacionService.esAprobado(idAlumnoLogueado);

        lblPromedioGeneral.setText(String.format("%.2f", promedioGlobal));
        lblEstado.setText(aprobadoGlobal ? "APROBADO" : "REPROBADO");
        lblEstado.setStyle(aprobadoGlobal ?
                "-fx-text-fill: green; -fx-font-weight: bold;" :
                "-fx-text-fill: red; -fx-font-weight: bold;");

        List<Inscripcion> inscripciones = inscripcionDAO.getByAlumnoId(idAlumnoLogueado);

        List<HistorialItem> historial = inscripciones.stream()
                .map(ins -> {
                    Materia materia = materiaDAO.getById(ins.getIdMateria());
                    double promedio = calificacionService.calcularPromedioMateria(ins.getIdInscripcion());
                    String estado = calificacionService.esMateriaAprobada(promedio) ? "APROBADO" : "REPROBADO";

                    return new HistorialItem(
                            (materia != null ? materia.getNombre() : "Materia no encontrada"),
                            (materia != null ? materia.getCodigoMateria() : "N/A"),
                            promedio,
                            estado
                    );
                })
                .toList();

        tableViewHistorial.setItems(FXCollections.observableArrayList(historial));
    }

        @FXML
    private void descargarPDF() {

        try {
            PdfService pdfService = new PdfService();

            Alumno alumno = alumnoDAO.getByUserId(idAlumnoLogueado);


            FileChooser chooser = new FileChooser();
            chooser.setTitle("Guardar Historial Académico");
            chooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Archivo PDF", "*.pdf"));
            chooser.setInitialFileName("Historial_" + alumno.getMatricula() + ".pdf");

            File destino = chooser.showSaveDialog(null);
            if (destino == null) return;

            pdfService.generarHistorialPDF(alumno, destino.getAbsolutePath());

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setHeaderText("PDF generado");
            alert.setContentText("El archivo se guardó correctamente en:\n" + destino.getAbsolutePath());
            alert.showAndWait();

        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText("Error al generar PDF");
            alert.setContentText(e.getMessage());
            alert.showAndWait();
        }
    }
}
