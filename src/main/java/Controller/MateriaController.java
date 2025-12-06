package Controller;

import DAO.MateriaDAO;
import Model.Materia;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

public class MateriaController {

    @FXML private TextField txtNombre;
    @FXML private TextField txtCodigo;
    @FXML private TextField txtCreditos; // Para manejar el valor numérico
    @FXML private TableView<Materia> tableViewMaterias;
    @FXML private TableColumn<Materia, Integer> colID;
    @FXML private TableColumn<Materia, String> colNombre;
    @FXML private TableColumn<Materia, String> colCodigo;
    @FXML private TableColumn<Materia, Integer> colCreditos;

    private final MateriaDAO materiaDAO = new MateriaDAO();
    private ObservableList<Materia> listaMaterias;

    @FXML
    public void initialize() {
        // Mapeo de columnas a propiedades del objeto Model.Materia
        colID.setCellValueFactory(new PropertyValueFactory<>("idMateria"));
        colNombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        colCodigo.setCellValueFactory(new PropertyValueFactory<>("codigoMateria"));
        colCreditos.setCellValueFactory(new PropertyValueFactory<>("creditos"));

        cargarMaterias();
    }

    private void cargarMaterias() {
        listaMaterias = FXCollections.observableArrayList(materiaDAO.getAll());
        tableViewMaterias.setItems(listaMaterias);
    }

    @FXML
    private void handleSaveMateria() {
        String nombre = txtNombre.getText();
        String codigo = txtCodigo.getText();
        String creditosStr = txtCreditos.getText();

        if (nombre.isEmpty() || codigo.isEmpty() || creditosStr.isEmpty()) {
            showAlert("Error de Validación", "Complete todos los campos.", Alert.AlertType.ERROR);
            return;
        }

        int creditos;
        try {
            creditos = Integer.parseInt(creditosStr);
        } catch (NumberFormatException e) {
            showAlert("Error de Formato", "Los créditos deben ser un número entero.", Alert.AlertType.ERROR);
            return;
        }

        Materia nuevaMateria = new Materia(0, nombre, codigo, creditos);

        materiaDAO.save(nuevaMateria);
        listaMaterias.add(nuevaMateria);

        txtNombre.clear();
        txtCodigo.clear();
        txtCreditos.clear();

        showAlert("Éxito", "Materia registrada.", Alert.AlertType.INFORMATION);
    }

    @FXML
    private void handleUpdateMateria() {
        Materia selectedMateria = tableViewMaterias.getSelectionModel().getSelectedItem();

        if (selectedMateria == null) {
            showAlert("Error", "Seleccione una materia de la tabla para actualizar.", Alert.AlertType.WARNING);
            return;
        }

        // 1. Obtener datos de los campos
        String nombre = txtNombre.getText();
        String codigo = txtCodigo.getText();
        String creditosStr = txtCreditos.getText();

        if (nombre.isEmpty() || codigo.isEmpty() || creditosStr.isEmpty()) {
            showAlert("Error de Validación", "Complete todos los campos para actualizar.", Alert.AlertType.ERROR);
            return;
        }

        try {
            int creditos = Integer.parseInt(creditosStr);

            // 2. Aplicar los nuevos valores al objeto
            selectedMateria.setNombre(nombre);
            selectedMateria.setCodigoMateria(codigo);
            selectedMateria.setCreditos(creditos);

            // 3. Persistir en la BD (MateriaDAO.update)
            materiaDAO.update(selectedMateria);

            // 4. Refrescar la tabla para mostrar el cambio en la interfaz
            tableViewMaterias.refresh();

            showAlert("Éxito", "Materia actualizada correctamente.", Alert.AlertType.INFORMATION);
            limpiarCampos();
        } catch (NumberFormatException e) {
            showAlert("Error de Formato", "Los créditos deben ser un número entero válido.", Alert.AlertType.ERROR);
        } catch (Exception e) {
            // Atrapa errores de SQL como código duplicado
            showAlert("Error de BD", "No se pudo actualizar la materia. El código de materia podría estar duplicado.", Alert.AlertType.ERROR);
        }
    }

    // Dentro de MateriaController.java

    @FXML
    private void handleDeleteMateria() {
        Materia selectedMateria = tableViewMaterias.getSelectionModel().getSelectedItem();

        if (selectedMateria == null) {
            showAlert("Error", "Seleccione una materia de la tabla para eliminar.", Alert.AlertType.WARNING);
            return;
        }

        // 1. Cuadro de diálogo de confirmación
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION,
                "¿Está seguro de eliminar la materia " + selectedMateria.getNombre() + "?",
                ButtonType.YES, ButtonType.NO);
        alert.setTitle("Confirmar Eliminación");

        alert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.YES) {
                try {
                    // 2. Eliminar de la BD
                    materiaDAO.delete(selectedMateria.getIdMateria());

                    // 3. Eliminar de la lista de la interfaz
                    listaMaterias.remove(selectedMateria);

                    showAlert("Éxito", "Materia eliminada.", Alert.AlertType.INFORMATION);
                    limpiarCampos();
                } catch (Exception e) {
                    // Esto puede ocurrir si hay inscripciones o calificaciones asociadas
                    showAlert("Error de BD", "No se pudo eliminar la materia. Asegúrese de que no tenga inscripciones activas.", Alert.AlertType.ERROR);
                }
            }
        });
    }

    // Dentro de MateriaController.java

    private void limpiarCampos() {
        txtNombre.clear();
        txtCodigo.clear();
        txtCreditos.clear();
    }
    // NOTA: Debes implementar handleDeleteMateria y handleUpdateMateria

    private void showAlert(String title, String message, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}