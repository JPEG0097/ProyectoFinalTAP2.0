package Controller;

import DataBase.ConexionBD;
import Service.CalificacionesService; // Importamos el service
import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class MainController {

    @FXML
    private Label welcomeText;

    // Inicializamos el service para usar la lógica de negocio
    private final CalificacionesService calificacionService = new CalificacionesService();

    @FXML
    public void initialize() {
        // Este método se llama automáticamente después de que el FXML ha sido cargado.
        // Aquí podemos verificar la conexión al inicio de la aplicación.
        if (ConexionBD.getInstance().getConnection() != null) {
            welcomeText.setText("¡Sistema de Calificaciones! Conexión DB exitosa.");
        } else {
            welcomeText.setText("¡ERROR! Falló la conexión a la Base de Datos.");
        }
    }

}
