package Controller;

import DAO.GrupoDAO;
import Factory.MysqlDaoFact;
import Model.Grupo;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

public class GrupoController {

    // Componentes FXML de la Vista
    @FXML private TextField txtNombreGrupo;
    @FXML private TextField txtSemestre;
    @FXML private TableView<Grupo> tableViewGrupos;
    @FXML private TableColumn<Grupo, Integer> colID;
    @FXML private TableColumn<Grupo, String> colNombre;
    @FXML private TableColumn<Grupo, Integer> colSemestre;

    // Dependencia del DAO, obtenida del Factory
    private final GrupoDAO grupoDAO = new MysqlDaoFact().createGrupoDAO();
    private ObservableList<Grupo> listaGrupos;

    @FXML
    public void initialize() {
        // 1. Configurar las columnas de la tabla (mapping de propiedades del Model.Grupo)
        colID.setCellValueFactory(new PropertyValueFactory<>("idGrupo"));
        colNombre.setCellValueFactory(new PropertyValueFactory<>("nombreGrupo"));
        colSemestre.setCellValueFactory(new PropertyValueFactory<>("semestre"));

        // 2. Cargar los datos iniciales
        cargarGrupos();

        // 3. Listener para cargar datos en los campos al seleccionar un grupo
        tableViewGrupos.getSelectionModel().selectedItemProperty().addListener(
                (obs, oldSelection, newSelection) -> {
                    if (newSelection != null) {
                        txtNombreGrupo.setText(newSelection.getNombreGrupo());
                        txtSemestre.setText(String.valueOf(newSelection.getSemestre()));
                    }
                }
        );
    }

    private void cargarGrupos() {
        listaGrupos = FXCollections.observableArrayList(grupoDAO.getAll());
        tableViewGrupos.setItems(listaGrupos);
    }

    /**
     * Tarea del programa: Registrar un nuevo grupo.
     */
    @FXML
    private void handleSaveGrupo() {
        String nombre = txtNombreGrupo.getText();
        String semestreStr = txtSemestre.getText();

        if (nombre.isEmpty() || semestreStr.isEmpty()) {
            showAlert("Error", "Complete todos los campos.", Alert.AlertType.ERROR);
            return;
        }

        int semestre;
        try {
            semestre = Integer.parseInt(semestreStr);
        } catch (NumberFormatException e) {
            showAlert("Error de Formato", "El semestre debe ser un número entero.", Alert.AlertType.ERROR);
            return;
        }

        Grupo nuevoGrupo = new Grupo(nombre, semestre);

        try {
            grupoDAO.save(nuevoGrupo);
            listaGrupos.add(nuevoGrupo);
            limpiarCampos();
            showAlert("Éxito", "Grupo registrado.", Alert.AlertType.INFORMATION);
        } catch (Exception e) {
            showAlert("Error de BD", "No se pudo registrar el grupo. ¿Nombre duplicado?", Alert.AlertType.ERROR);
        }
    }

    /**
     * Tarea del programa: Actualizar los datos del grupo seleccionado.
     */
    @FXML
    private void handleUpdateGrupo() {
        Grupo selectedGrupo = tableViewGrupos.getSelectionModel().getSelectedItem();

        if (selectedGrupo == null) {
            showAlert("Error", "Seleccione un grupo para actualizar.", Alert.AlertType.WARNING);
            return;
        }

        String nombre = txtNombreGrupo.getText();
        String semestreStr = txtSemestre.getText();

        try {
            int semestre = Integer.parseInt(semestreStr);

            selectedGrupo.setNombreGrupo(nombre);
            selectedGrupo.setSemestre(semestre);

            grupoDAO.update(selectedGrupo);
            tableViewGrupos.refresh();

            showAlert("Éxito", "Grupo actualizado correctamente.", Alert.AlertType.INFORMATION);
        } catch (NumberFormatException e) {
            showAlert("Error de Formato", "El semestre debe ser un número entero válido.", Alert.AlertType.ERROR);
        } catch (Exception e) {
            showAlert("Error de BD", "No se pudo actualizar el grupo.", Alert.AlertType.ERROR);
        }
    }

    /**
     * Tarea del programa: Eliminar el grupo seleccionado.
     */
    @FXML
    private void handleDeleteGrupo() {
        Grupo selectedGrupo = tableViewGrupos.getSelectionModel().getSelectedItem();

        if (selectedGrupo == null) {
            showAlert("Error", "Seleccione un grupo para eliminar.", Alert.AlertType.WARNING);
            return;
        }

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION,
                "¿Está seguro de eliminar el grupo " + selectedGrupo.getNombreGrupo() + "?",
                ButtonType.YES, ButtonType.NO);
        alert.setTitle("Confirmar Eliminación");

        alert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.YES) {
                // La BD se encarga de poner a NULL el id_grupo en los alumnos asignados.
                grupoDAO.delete(selectedGrupo.getIdGrupo());
                listaGrupos.remove(selectedGrupo);
                showAlert("Éxito", "Grupo eliminado.", Alert.AlertType.INFORMATION);
                limpiarCampos();
            }
        });
    }

    private void limpiarCampos() {
        txtNombreGrupo.clear();
        txtSemestre.clear();
    }

    private void showAlert(String title, String message, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
