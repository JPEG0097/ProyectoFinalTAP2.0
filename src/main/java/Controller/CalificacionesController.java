package Controller;

import DAO.AlumnoDAO;
import DAO.InscripcionDAO;
import DAO.ParcialDAO;
import Factory.MysqlDaoFact; // Usamos el nombre correcto del Factory
import Model.Alumno;
import Model.Inscripcion;
import Model.Parcial;
import Service.CalificacionesService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import java.util.List;
import java.util.Optional;

public class CalificacionesController {

    // Componentes FXML
    @FXML private ComboBox<Alumno> cbAlumno;
    @FXML private ComboBox<Inscripcion> cbInscripcion;
    @FXML private TextField txtCalificacion;
    @FXML private ComboBox<Integer> cbNumeroParcial; // <-- Componente para seleccionar 1, 2, 3 o 4

    // La tabla ahora muestra parciales (la unidad de nota más pequeña)
    @FXML private TableView<Parcial> tableViewParciales;
    @FXML private TableColumn<Parcial, Integer> colID;
    @FXML private TableColumn<Parcial, Integer> colInscripcionID;
    @FXML private TableColumn<Parcial, Double> colCalificacion;
    @FXML private TableColumn<Parcial, Integer> colNumeroParcial;

    // Dependencias
    private final MysqlDaoFact factory = new MysqlDaoFact();
    private final AlumnoDAO alumnoDAO = factory.createAlumnoDAO();
    private final InscripcionDAO inscripcionDAO = factory.createInscripcionDAO();
    private final ParcialDAO parcialDAO = factory.createParcialDAO();
    private final CalificacionesService calificacionService = new CalificacionesService();

    private ObservableList<Parcial> listaParciales;

    @FXML
    public void initialize() {
        // 1. Configurar ComboBox de Parciales (1, 2, 3, 4)
        cbNumeroParcial.setItems(FXCollections.observableArrayList(1, 2, 3, 4));

        // 2. Configurar TableView (Ahora mapea Parcial)
        colID.setCellValueFactory(new PropertyValueFactory<>("idParcial"));
        colInscripcionID.setCellValueFactory(new PropertyValueFactory<>("idInscripcion"));
        colCalificacion.setCellValueFactory(new PropertyValueFactory<>("calificacionParcial"));
        colNumeroParcial.setCellValueFactory(new PropertyValueFactory<>("numeroParcial"));

        // 3. Cargar datos iniciales
        cbAlumno.setItems(FXCollections.observableArrayList(alumnoDAO.getAll()));
        cargarParciales();

        // 4. Listener: Carga las inscripciones del alumno seleccionado
        cbAlumno.getSelectionModel().selectedItemProperty().addListener(
                (obs, oldVal, newVal) -> {
                    if (newVal != null) {
                        cargarInscripcionesPorAlumno(newVal.getIdAlumno());
                    } else {
                        cbInscripcion.getItems().clear();
                    }
                }
        );

        // 5. Listener: Carga datos de Parcial seleccionado en campos para edición
        tableViewParciales.getSelectionModel().selectedItemProperty().addListener(
                (obs, oldVal, newVal) -> {
                    if (newVal != null) {
                        cargarDatosParcial(newVal);
                    }
                }
        );
    }

    private void cargarInscripcionesPorAlumno(int idAlumno) {
        List<Inscripcion> inscripciones = inscripcionDAO.getByAlumnoId(idAlumno);
        cbInscripcion.setItems(FXCollections.observableArrayList(inscripciones));
        cbInscripcion.getSelectionModel().clearSelection();
    }

    private void cargarParciales() {
        listaParciales = FXCollections.observableArrayList(parcialDAO.getAll());
        tableViewParciales.setItems(listaParciales);
    }

    private void cargarDatosParcial(Parcial parcial) {
        // Encontrar la Inscripción y el Alumno asociados para precargar los ComboBoxes
        Inscripcion inscripcion = inscripcionDAO.getById(parcial.getIdInscripcion());
        // Usamos el ID del Usuario que creó la inscripción para obtener el objeto Alumno
        Alumno alumno = alumnoDAO.getByUserId(inscripcion.getIdAlumno());

        cbAlumno.getSelectionModel().select(alumno);
        cbInscripcion.getSelectionModel().select(inscripcion);
        cbNumeroParcial.getSelectionModel().select(Integer.valueOf(parcial.getNumeroParcial()));
        txtCalificacion.setText(String.valueOf(parcial.getCalificacionParcial()));
    }

    /**
     * Tarea: Registrar o Actualizar una nota Parcial.
     */
    @FXML
    private void handleSaveCalificacion() {
        Inscripcion inscripcion = cbInscripcion.getSelectionModel().getSelectedItem();
        Integer numParcial = cbNumeroParcial.getSelectionModel().getSelectedItem();
        String calificacionStr = txtCalificacion.getText();

        if (inscripcion == null || numParcial == null || calificacionStr.isEmpty()) {
            showAlert("Error", "Seleccione inscripción, número de parcial y digite la nota.", Alert.AlertType.ERROR);
            return;
        }

        double calificacionValor;
        try {
            calificacionValor = Double.parseDouble(calificacionStr);
            if (calificacionValor < 0 || calificacionValor > 10) { throw new NumberFormatException(); }
        } catch (NumberFormatException e) {
            showAlert("Error", "La calificación debe ser un número entre 0 y 10.", Alert.AlertType.ERROR);
            return;
        }

        // 1. Intentar encontrar si la nota ya existe (para UPDATE)
        List<Parcial> parciales = parcialDAO.getParcialesByInscripcionId(inscripcion.getIdInscripcion());
        Optional<Parcial> parcialExistente = parciales.stream()
                .filter(p -> p.getNumeroParcial() == numParcial)
                .findFirst();

        try {
            if (parcialExistente.isPresent()) {
                // Tarea: Actualizar
                Parcial p = parcialExistente.get();
                p.setCalificacionParcial(calificacionValor);
                parcialDAO.update(p);
                showAlert("Éxito", "Parcial " + numParcial + " actualizado.", Alert.AlertType.INFORMATION);
            } else {
                // Tarea: Crear
                Parcial nuevoParcial = new Parcial(inscripcion.getIdInscripcion(), numParcial, calificacionValor);
                parcialDAO.save(nuevoParcial);
                showAlert("Éxito", "Parcial " + numParcial + " registrado.", Alert.AlertType.INFORMATION);
            }

            // 2. Recalcular y refrescar (Usamos el ID de Alumno de la Inscripción)
            calificacionService.calcularPromedioGeneral(inscripcion.getIdAlumno());
            cargarParciales();
            limpiarCampos();

        } catch (Exception e) {
            showAlert("Error de BD", "Ocurrió un error al guardar/actualizar la nota: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    /**
     * Tarea: Eliminar una nota parcial seleccionada.
     */
    @FXML
    private void handleDeleteCalificacion() {
        Parcial selectedParcial = tableViewParciales.getSelectionModel().getSelectedItem();

        if (selectedParcial == null) {
            showAlert("Error", "Seleccione un Parcial de la tabla para eliminar.", Alert.AlertType.WARNING);
            return;
        }

        // Obtenemos el ID de alumno ANTES de borrar
        Inscripcion inscripcion = inscripcionDAO.getById(selectedParcial.getIdInscripcion());
        int idAlumnoAfectado = inscripcion != null ? inscripcion.getIdAlumno() : 0;

        try {
            parcialDAO.delete(selectedParcial.getIdParcial());

            // Recalcular y refrescar
            if (idAlumnoAfectado != 0) {
                calificacionService.calcularPromedioGeneral(idAlumnoAfectado);
            }
            cargarParciales();
            showAlert("Éxito", "Parcial eliminado. Promedio recalculado.", Alert.AlertType.INFORMATION);

        } catch (Exception e) {
            showAlert("Error de BD", "No se pudo eliminar el parcial. " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    /**
     * Tarea: Consulta de Historial (Muestra el promedio).
     */
    @FXML
    private void handleConsultarPromedio() {
        Alumno alumno = cbAlumno.getSelectionModel().getSelectedItem();
        if (alumno == null) {
            showAlert("Error", "Seleccione un alumno para consultar el promedio.", Alert.AlertType.WARNING);
            return;
        }

        // Usamos el ID de la tabla 'alumnos' para buscar las inscripciones del servicio.
        double promedio = calificacionService.calcularPromedioGeneral(alumno.getIdAlumno());
        boolean aprobado = calificacionService.esAprobado(alumno.getIdAlumno());

        showAlert("Promedio de " + alumno.getNombre(),
                "Promedio General: " + String.format("%.2f", promedio) + "\n" +
                        "Estado: " + (aprobado ? "APROBADO" : "REPROBADO"),
                Alert.AlertType.INFORMATION);
    }

    private void limpiarCampos() {
        cbInscripcion.getSelectionModel().clearSelection();
        cbNumeroParcial.getSelectionModel().clearSelection();
        txtCalificacion.clear();
    }

    private void showAlert(String title, String message, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}