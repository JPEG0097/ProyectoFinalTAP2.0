package Controller;

import DataBase.ConexionBD;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.IOException;

public class Principal {

    @FXML private BorderPane mainBorderPane;
    @FXML private Label lblStatus;

    @FXML
    public void initialize() {
        // Verifica la conexión al inicio y actualiza la barra de estado
        if (ConexionBD.getInstance().getConnection() != null) {
            lblStatus.setText("Estado: Conexión a BD exitosa.");
        } else {
            lblStatus.setText("Estado: ERROR DE CONEXIÓN A BD.");
        }
        // Carga la vista de Alumnos por defecto al inicio
        showAlumnoView();
    }

    // --- Métodos de Navegación ---

    /** Carga la vista de un archivo FXML dado y la coloca en el centro del BorderPane. */
    private void loadView(String fxmlFileName) {
        try {
            // La ruta es relativa al paquete de recursos de JavaFX
            Pane view = FXMLLoader.load(getClass().getResource("/org/example/proyectofinaltap/" + fxmlFileName));
            mainBorderPane.setCenter(view);
        } catch (IOException e) {
            System.err.println("Error al cargar la vista " + fxmlFileName + ": " + e.getMessage());
            e.printStackTrace();
            // Muestra un mensaje de error en el centro si el archivo FXML no se encuentra.
            mainBorderPane.setCenter(new Label("ERROR: No se pudo cargar la vista " + fxmlFileName));
        }
    }

    @FXML
    public void showGrupoView() {
        loadView("GrupoView.fxml");
    }

    @FXML
    public void showAlumnoView() {
        loadView("AlumnoView.fxml");
    }

    @FXML
    public void showMateriaView() {
        // Asumiendo que tienes un MateriaView.fxml creado (similar a AlumnoView)
        loadView("MateriaView.fxml");
    }

    @FXML
    public void showInscripcionView() {
        loadView("InscripcionView.fxml");
    }

    @FXML
    public void showCalificacionView() {
        loadView("CalificacionView.fxml");
    }

    @FXML
    public void showRegistroView() {
        // Este método carga el formulario para registrar Alumnos, Docentes o Admins.
        loadView("RegistroView.fxml");
    }

    @FXML
    public void handleLogout() {
        try {
            // 1. Cargar la vista de Login
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/proyectofinaltap/LoginView.fxml"));

            // 2. Crear un nuevo Stage (Ventana) para el Login
            Stage loginStage = new Stage();
            loginStage.setTitle("Iniciar Sesión");
            loginStage.setScene(new Scene(loader.load(), 400, 300)); // Usamos el tamaño del login
            loginStage.show();

            // 3. Obtener la ventana actual (MainView) y cerrarla
            Stage currentStage = (Stage) mainBorderPane.getScene().getWindow();
            currentStage.close();

            // Opcional: Cerrar la conexión a la BD al salir de la aplicación si el programa fuera a detenerse aquí.
            // DataBase.ConexionBD.getInstance().closeConnection();

        } catch (IOException e) {
            System.err.println("Error al cargar la vista de Login: " + e.getMessage());
        }
    }
}