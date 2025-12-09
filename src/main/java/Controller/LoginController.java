package Controller;

import DAO.UsuarioDAO;
import Model.Usuario;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;

public class LoginController {

    @FXML private TextField txtUsuario;
    @FXML private PasswordField txtContrasena;
    @FXML private Label lblMensaje;

    private final UsuarioDAO usuarioDAO = new UsuarioDAO();

    @FXML
    private void handleLogin() {
        String user = txtUsuario.getText();
        String pass = txtContrasena.getText();

        if (user.isEmpty() || pass.isEmpty()) {
            lblMensaje.setText("Debe ingresar usuario y contraseña.");
            return;
        }

        Usuario usuario = usuarioDAO.autenticar(user, pass);

        if (usuario != null) {
            // Tarea: Autenticación exitosa.
            abrirVentanaPrincipal(usuario);

            // Cerrar la ventana de login (opcional)
            ((Stage) txtUsuario.getScene().getWindow()).close();
        } else {
            lblMensaje.setText("Credenciales incorrectas o usuario no encontrado.");
        }
    }
    private void abrirVentanaPrincipal(Usuario usuario) {
        String fxmlFile;
        int width;
        int height;
        String title;

        // Lógica para determinar el TIPO DE VISTA y el FXML a cargar
        if (usuario.getRol().equals("administrador") || usuario.getRol().equals("docente")) {
            // Caso Administrador / Docente: Carga el menú principal (MainView)
            fxmlFile = "MainView.fxml";
            title = "Sistema de Calificaciones - " + usuario.getRol().toUpperCase();
            width = 1000;
            height = 650;
        } else {
            // Caso Alumno: Carga la vista restringida de historial
            fxmlFile = "AlumnoHistorialView.fxml";
            title = "Historial Académico de Alumno";
            width = 600;
            height = 400;
        }

        try {
            // --- 1. Cargar la vista y obtener el controlador ---
            // Usamos la clase MainApplication para obtener el recurso (ajusta a Principal.class si es necesario)
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/proyectofinaltap/" + fxmlFile));

            // 2. Cargar la vista
            Scene scene = new Scene(loader.load(), width, height);

            // 3. Obtener el controlador para inyectar datos (Solo se necesita si es Alumno)
            if (usuario.getRol().equals("alumno")) {
                AlumnoHistorialController controller = loader.getController();

                // 4. INYECTAR EL ID DEL ALUMNO
                // Pasa el idUsuario, que es el idAlumno para el rol 'alumno'
                controller.setAlumnoId(usuario.getIdUsuario());
            }

            // 5. Mostrar la ventana
            Stage stage = new Stage();
            stage.setTitle(title);
            stage.setScene(scene);
            stage.show();

        } catch (IOException e) {
            System.err.println("Error fatal al cargar la ventana principal: " + e.getMessage());
            e.printStackTrace();
        }
    }
}