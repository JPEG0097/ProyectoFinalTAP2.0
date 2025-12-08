package Controller;

import DAO.AlumnoDAO;
import DAO.MateriaDAO;
import DAO.InscripcionDAO;
import Factory.MysqlDaoFact;
import Model.Alumno;
import Model.Materia;
import Model.Inscripcion;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import java.time.LocalDate;

public class InscripcionController {

    // Componentes FXML de la Vista
    @FXML private ComboBox<Alumno> cbAlumno;
    @FXML private ComboBox<Materia> cbMateria;
    @FXML private DatePicker dpFechaInscripcion;
    @FXML private TableView<Inscripcion> tableViewInscripciones;
    @FXML private TableColumn<Inscripcion, Integer> colID;
    @FXML private TableColumn<Inscripcion, Integer> colAlumnoID;
    @FXML private TableColumn<Inscripcion, Integer> colMateriaID;
    @FXML private TableColumn<Inscripcion, LocalDate> colFecha;

    // Dependencias
    private final MysqlDaoFact factory = new MysqlDaoFact();
    private final AlumnoDAO alumnoDAO = factory.createAlumnoDAO();
    private final MateriaDAO materiaDAO = factory.createMateriaDAO();
    private final InscripcionDAO inscripcionDAO = factory.createInscripcionDAO();

    private ObservableList<Inscripcion> listaInscripciones;

    @FXML
    public void initialize() {
        // 1. Configurar las columnas de la tabla
        colID.setCellValueFactory(new PropertyValueFactory<>("idInscripcion"));
        colAlumnoID.setCellValueFactory(new PropertyValueFactory<>("idAlumno"));
        colMateriaID.setCellValueFactory(new PropertyValueFactory<>("idMateria"));
        colFecha.setCellValueFactory(new PropertyValueFactory<>("fechaInscripcion"));

        // 2. Cargar ComboBoxes (Alumnos y Materias)
        cbAlumno.setItems(FXCollections.observableArrayList(alumnoDAO.getAll()));
        cbMateria.setItems(FXCollections.observableArrayList(materiaDAO.getAll()));

        // 3. Cargar la tabla
        cargarInscripciones();
    }

    private void cargarInscripciones() {
        listaInscripciones = FXCollections.observableArrayList(inscripcionDAO.getAll());
        tableViewInscripciones.setItems(listaInscripciones);
    }

    /**
     * Tarea del programa: Registrar una nueva inscripción (Alumno cursa Materia).
     */
    @FXML
    private void handleSaveInscripcion() {
        Alumno alumno = cbAlumno.getSelectionModel().getSelectedItem();
        Materia materia = cbMateria.getSelectionModel().getSelectedItem();
        LocalDate fecha = dpFechaInscripcion.getValue();

        if (alumno == null || materia == null || fecha == null) {
            showAlert("Error de Validación", "Debe seleccionar un alumno, una materia y una fecha.", Alert.AlertType.ERROR);
            return;
        }

        Inscripcion nuevaInscripcion = new Inscripcion(
                alumno.getIdAlumno(),
                materia.getIdMateria(),
                fecha
        );

        try {
            inscripcionDAO.save(nuevaInscripcion);
            listaInscripciones.add(nuevaInscripcion);
            showAlert("Éxito", "Inscripción registrada con éxito.", Alert.AlertType.INFORMATION);
            limpiarCampos();
        } catch (Exception e) {
            showAlert("Error de BD", "No se pudo registrar la inscripción. El alumno ya podría estar inscrito en esa materia.", Alert.AlertType.ERROR);
        }
    }

    /**
     * Tarea del programa: Eliminar la inscripción seleccionada.
     */
    @FXML
    private void handleDeleteInscripcion() {
        Inscripcion selectedInscripcion = tableViewInscripciones.getSelectionModel().getSelectedItem();

        if (selectedInscripcion == null) {
            showAlert("Error", "Seleccione una inscripción de la tabla para eliminar.", Alert.AlertType.WARNING);
            return;
        }

        // Confirmación
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION,
                "¿Está seguro de eliminar esta inscripción? Esto eliminará cualquier calificación asociada.",
                ButtonType.YES, ButtonType.NO);
        alert.setTitle("Confirmar Eliminación");

        alert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.YES) {
                // DELETE CASCADE en la BD asegura que la Calificación se elimine también.
                inscripcionDAO.delete(selectedInscripcion.getIdInscripcion());
                listaInscripciones.remove(selectedInscripcion);
                showAlert("Éxito", "Inscripción eliminada.", Alert.AlertType.INFORMATION);
            }
        });
    }

    private void limpiarCampos() {
        cbAlumno.getSelectionModel().clearSelection();
        cbMateria.getSelectionModel().clearSelection();
        dpFechaInscripcion.setValue(null);
    }

    private void showAlert(String title, String message, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public void handleRefresh(ActionEvent actionEvent) {
    }
}
