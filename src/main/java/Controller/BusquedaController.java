package Controller;

import DAO.AlumnoDAO;
import Factory.MysqlDaoFact;
import Model.Alumno;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import java.util.List;
import java.util.stream.Collectors;

public class BusquedaController {

    @FXML private TextField txtBusqueda;
    @FXML private ComboBox<String> cbCriterio;
    @FXML private TableView<Alumno> tableViewResultados;

    private final AlumnoDAO alumnoDAO;

    public BusquedaController() {
        this.alumnoDAO = new MysqlDaoFact().createAlumnoDAO();
    }

    @FXML
    public void initialize() {
        cbCriterio.setItems(FXCollections.observableArrayList(
                "Nombre", "Matrícula", "Grupo", "Promedio"
        ));
    }

    @FXML
    private void handleBuscar() {
        String criterio = cbCriterio.getValue();
        String texto = txtBusqueda.getText();

        List<Alumno> resultados = alumnoDAO.getAll().stream()
                .filter(alumno -> coincideCriterio(alumno, criterio, texto))
                .collect(Collectors.toList());

        tableViewResultados.setItems(FXCollections.observableArrayList(resultados));
    }

    private boolean coincideCriterio(Alumno alumno, String criterio, String texto) {
        if (texto.isEmpty()) return true;

        return switch (criterio) {
            case "Nombre" -> alumno.getNombre().toLowerCase().contains(texto.toLowerCase());
            case "Matrícula" -> alumno.getMatricula().contains(texto);
            default -> false;
        };
    }
}