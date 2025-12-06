package Controller;

import DAO.AlumnoDAO;
import DAO.DocenteDAO; // Añadido para registrar docentes
import DAO.GrupoDAO;
import DAO.UsuarioDAO;
import Factory.MysqlDaoFact; // Usamos el nombre correcto del Factory
import Model.Alumno;
import Model.Docente; // Añadido
import Model.Grupo;
import Model.Usuario;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.util.List;

public class RegistroController {

    @FXML private TextField txtNombreUsuario;
    @FXML private PasswordField txtContrasena;
    @FXML private ComboBox<String> cbRol;
    @FXML private TextField txtMatricula;
    @FXML private TextField txtNombreCompleto;
    @FXML private ComboBox<Grupo> cbGrupo;

    // Usamos el nombre correcto del Factory
    private final MysqlDaoFact factory = new MysqlDaoFact();
    private final UsuarioDAO usuarioDAO = factory.createUsuarioDAO();
    private final AlumnoDAO alumnoDAO = factory.createAlumnoDAO();
    private final DocenteDAO docenteDAO = factory.createDocenteDAO(); // Añadido
    private final GrupoDAO grupoDAO = factory.createGrupoDAO();


    @FXML
    public void initialize() {
        // 1. Cargar roles en el ComboBox
        cbRol.setItems(FXCollections.observableArrayList("administrador", "docente", "alumno"));

        // 2. Cargar grupos en el ComboBox de Alumnos
        cbGrupo.setItems(FXCollections.observableArrayList(grupoDAO.getAll()));

        // 3. Listener para controlar la visibilidad
        cbRol.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            boolean isAlumno = "alumno".equals(newVal);

            // Si es 'alumno', mostramos los campos específicos
            txtMatricula.setDisable(!isAlumno);
            txtNombreCompleto.setDisable(!isAlumno);
            cbGrupo.setDisable(!isAlumno);

            // Si es 'docente', solo necesitamos el nombre completo (el nombre de usuario es el login)
            if ("docente".equals(newVal)) {
                txtNombreCompleto.setDisable(false);
            }
        });

        // Inicialmente, deshabilitar campos de Alumno
        txtMatricula.setDisable(true);
        txtNombreCompleto.setDisable(true);
        cbGrupo.setDisable(true);
    }

    /**
     * Tarea: Maneja el proceso de registro de Usuario (Paso 1) y la entidad asociada (Paso 2).
     */
    @FXML
    private void handleRegister() {
        String user = txtNombreUsuario.getText();
        String pass = txtContrasena.getText();
        String rol = cbRol.getSelectionModel().getSelectedItem();

        if (user.isEmpty() || pass.isEmpty() || rol == null) {
            showAlert("Error", "Debe completar Usuario, Contraseña y Rol.", Alert.AlertType.ERROR);
            return;
        }

        // 1. Crear y guardar el Usuario (Paso 1)
        Usuario nuevoUsuario = new Usuario(user, pass, rol);
        try {
            usuarioDAO.save(nuevoUsuario); // Guarda y obtiene el id_usuario (PK)

            // 2. Registrar la entidad específica (Paso 2)
            if ("alumno".equals(rol)) {
                // El método registrarAlumnoAsociado maneja la validación de sus campos
                registrarAlumnoAsociado(nuevoUsuario);
            } else if ("docente".equals(rol)) {
                registrarDocenteAsociado(nuevoUsuario);
            }
            // Si es 'administrador', no necesita registro en una tabla separada.

            showAlert("Éxito", "Usuario " + user + " (" + rol + ") registrado correctamente.", Alert.AlertType.INFORMATION);
            limpiarCampos();

        } catch (Exception e) {
            // Si ocurre un error, muestra un mensaje detallado
            showAlert("Error de Registro", "Error al registrar: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    /**
     * Tarea: Completa el registro del alumno en la tabla 'alumnos'.
     */
    private void registrarAlumnoAsociado(Usuario usuario) throws Exception {
        String matricula = txtMatricula.getText();
        String nombreComp = txtNombreCompleto.getText();
        Grupo grupo = cbGrupo.getSelectionModel().getSelectedItem();

        if (matricula.isEmpty() || nombreComp.isEmpty() || grupo == null) {
            // Lanza una excepción si los campos específicos del alumno faltan
            throw new Exception("Faltan datos específicos del alumno (Matrícula, Nombre, Grupo).");
        }

        // Crear el objeto Alumno con el ID de Usuario asignado
        Alumno nuevoAlumno = new Alumno(nombreComp, matricula, grupo.getIdGrupo(), usuario.getIdUsuario());

        // Guardar el registro en la tabla 'alumnos'
        alumnoDAO.save(nuevoAlumno, usuario); // Llamamos al DAO que hace la inserción en la tabla de alumnos
    }

    /**
     * Tarea: Completa el registro del docente en la tabla 'docentes'.
     */
    private void registrarDocenteAsociado(Usuario usuario) throws Exception {
        String nombreComp = txtNombreCompleto.getText();

        if (nombreComp.isEmpty()) {
            throw new Exception("Falta el nombre completo del docente.");
        }

        // Crear el objeto Docente con el ID de Usuario asignado
        Docente nuevoDocente = new Docente(nombreComp);

        // Guardar el registro en la tabla 'docentes'
        docenteDAO.save(nuevoDocente, usuario); // Llamamos al DAO que hace la inserción en la tabla de docentes
    }


    private void limpiarCampos() {
        txtNombreUsuario.clear();
        txtContrasena.clear();
        txtMatricula.clear();
        txtNombreCompleto.clear();
        cbRol.getSelectionModel().clearSelection();
        cbGrupo.getSelectionModel().clearSelection();

        // Vuelve a deshabilitar los campos específicos
        txtMatricula.setDisable(true);
        txtNombreCompleto.setDisable(true);
        cbGrupo.setDisable(true);
    }

    private void showAlert(String title, String message, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}