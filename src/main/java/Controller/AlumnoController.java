package Controller;

import DAO.AlumnoDAO;
import DAO.GrupoDAO;
import Factory.MysqlDaoFact;
import Model.Alumno;
import Model.Grupo;
import Model.Usuario;
import Service.CalificacionesService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

public class AlumnoController {

    // Componentes FXML
    @FXML private TextField txtNombre;
    @FXML private TextField txtMatricula;
    @FXML private ComboBox<Grupo> cbGrupo;
    @FXML private TableView<Alumno> tableViewAlumnos;
    @FXML private TableColumn<Alumno, Integer> colID;
    @FXML private TableColumn<Alumno, String> colNombre;
    @FXML private TableColumn<Alumno, String> colMatricula;
    @FXML private TableColumn<Alumno, Integer> colGrupoID;

    // Dependencias
    private final MysqlDaoFact factory = new MysqlDaoFact();
    private final AlumnoDAO alumnoDAO = factory.createAlumnoDAO();
    private final GrupoDAO grupoDAO = factory.createGrupoDAO();
    private final CalificacionesService calificacionService = new CalificacionesService();
    private ObservableList<Alumno> listaAlumnos;
    private ObservableList<Grupo> listaGrupos;


    @FXML
    public void initialize() {
        // Mapeo de columnas
        colID.setCellValueFactory(new PropertyValueFactory<>("idAlumno"));
        colNombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        colMatricula.setCellValueFactory(new PropertyValueFactory<>("matricula"));
        colGrupoID.setCellValueFactory(new PropertyValueFactory<>("idGrupo"));

        // Creación dinámica de la columna Promedio (No requiere ser declarada con @FXML)
        TableColumn<Alumno, Double> colPromedioDinamica = new TableColumn<>("Promedio");
        colPromedioDinamica.setCellValueFactory(data -> new javafx.beans.property.SimpleDoubleProperty(
                calificacionService.calcularPromedioGeneral(data.getValue().getIdUsuario())
        ).asObject());
        tableViewAlumnos.getColumns().add(colPromedioDinamica);

        cargarListas();

        // Listener para edición rápida
        tableViewAlumnos.getSelectionModel().selectedItemProperty().addListener(
                (obs, oldSelection, newSelection) -> {
                    if (newSelection != null) {
                        txtNombre.setText(newSelection.getNombre());
                        txtMatricula.setText(newSelection.getMatricula());

                        Grupo grupoSeleccionado = grupoDAO.getById(newSelection.getIdGrupo());
                        cbGrupo.getSelectionModel().select(grupoSeleccionado);
                    }
                }
        );
    }

    private void cargarListas() {
        listaAlumnos = FXCollections.observableArrayList(alumnoDAO.getAll());
        tableViewAlumnos.setItems(listaAlumnos);

        listaGrupos = FXCollections.observableArrayList(grupoDAO.getAll());
        cbGrupo.setItems(listaGrupos);
    }

    // ===================================================================
    // CRUD: CREATE
    // ===================================================================
    @FXML
    private void handleSaveAlumno() {
        String nombre = txtNombre.getText();
        String matricula = txtMatricula.getText();
        Grupo grupo = cbGrupo.getSelectionModel().getSelectedItem();

        // La contraseña debe obtenerse de alguna parte o generarse
        String contrasenaTemporal = "123456";

        if (nombre.isEmpty() || matricula.isEmpty() || grupo == null) {
            showAlert("Error", "Complete todos los campos y seleccione un grupo.", Alert.AlertType.ERROR);
            return;
        }

        // 1. Crear objetos: El idUsuario es 0, lo asigna el DAO al guardar
        Alumno nuevoAlumno = new Alumno(nombre, matricula, grupo.getIdGrupo(), 0);
        Usuario nuevoUsuario = new Usuario(matricula, contrasenaTemporal, "alumno");

        try {
            // 2. Registrar en dos pasos (UsuarioDAO luego AlumnoDAO)
            alumnoDAO.save(nuevoAlumno, nuevoUsuario);

            // 3. Actualizar la interfaz
            listaAlumnos.add(nuevoAlumno);
            limpiarCampos();
            showAlert("Éxito", "Alumno registrado.\nUsuario: " + matricula + "\nContraseña: " + contrasenaTemporal, Alert.AlertType.INFORMATION);
        } catch (Exception e) {
            showAlert("Error de BD", "No se pudo registrar (Matrícula/Usuario duplicado).", Alert.AlertType.ERROR);
        }
    }

    // ===================================================================
    // CRUD: UPDATE
    // ===================================================================
    @FXML
    private void handleUpdateAlumno() {
        Alumno selectedAlumno = tableViewAlumnos.getSelectionModel().getSelectedItem();

        if (selectedAlumno == null) {
            showAlert("Error", "Seleccione un alumno para actualizar.", Alert.AlertType.WARNING);
            return;
        }

        String nuevoNombre = txtNombre.getText();
        String nuevaMatricula = txtMatricula.getText();
        Grupo nuevoGrupo = cbGrupo.getSelectionModel().getSelectedItem();

        if (nuevoNombre.isEmpty() || nuevaMatricula.isEmpty() || nuevoGrupo == null) {
            showAlert("Error", "Complete todos los campos.", Alert.AlertType.ERROR);
            return;
        }

        // 1. Actualizar el objeto Model
        selectedAlumno.setNombre(nuevoNombre);
        selectedAlumno.setMatricula(nuevaMatricula);
        selectedAlumno.setIdGrupo(nuevoGrupo.getIdGrupo());

        // 2. Persistir en la BD
        try {
            alumnoDAO.update(selectedAlumno);
            tableViewAlumnos.refresh();
            showAlert("Éxito", "Alumno actualizado.", Alert.AlertType.INFORMATION);
        } catch (Exception e) {
            showAlert("Error de BD", "No se pudo actualizar (Matrícula duplicada).", Alert.AlertType.ERROR);
        }
    }

    // ===================================================================
    // CRUD: DELETE
    // ===================================================================
    @FXML
    private void handleDeleteAlumno() {
        Alumno selectedAlumno = tableViewAlumnos.getSelectionModel().getSelectedItem();

        if (selectedAlumno == null) {
            showAlert("Error", "Seleccione un alumno para eliminar.", Alert.AlertType.WARNING);
            return;
        }

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION,
                "¿Está seguro de eliminar a " + selectedAlumno.getNombre() + "?",
                ButtonType.YES, ButtonType.NO);
        alert.setTitle("Confirmar Eliminación");

        alert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.YES) {
                try {
                    // La BD eliminará las inscripciones por CASCADE
                    alumnoDAO.delete(selectedAlumno.getIdAlumno());

                    // Nota: Si el usuario de login también debe borrarse, debe llamar a usuarioDAO.delete(selectedAlumno.getIdUsuario())

                    listaAlumnos.remove(selectedAlumno);
                    showAlert("Éxito", "Alumno eliminado.", Alert.AlertType.INFORMATION);
                    limpiarCampos();
                } catch (Exception e) {
                    showAlert("Error de BD", "No se pudo eliminar el alumno.", Alert.AlertType.ERROR);
                }
            }
        });
    }

    // ===================================================================
    // REPORTES
    // ===================================================================
    @FXML
    private void handleExportCSV() {
        // Implementación del reporte CSV
    }

    // ===================================================================
    // UTILIDADES
    // ===================================================================
    private void limpiarCampos() {
        txtNombre.clear();
        txtMatricula.clear();
        cbGrupo.getSelectionModel().clearSelection();
    }

    private void showAlert(String title, String message, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}