package Controller;

import Service.CalificacionesService;
import Service.ReporteService;
import javafx.fxml.FXML;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Label;

import java.util.Map;

public class DashboardController {

    @FXML private Label lblTotalAlumnos;
    @FXML private Label lblTotalDocentes;
    @FXML private Label lblTotalMaterias;
    @FXML private BarChart<String, Number> chartCalificaciones;
    @FXML private PieChart chartDistribucionGrupos;

    private final CalificacionesService calificacionesService;
    private final ReporteService reporteService;

    public DashboardController() {
        this.calificacionesService = new CalificacionesService();
        this.reporteService = new ReporteService();
    }

    @FXML
    public void initialize() {
        cargarEstadisticas();
        cargarGraficos();
    }

    private void cargarEstadisticas() {
        // Implementar lógica para obtener y mostrar estadísticas
    }

    private void cargarGraficos() {
        cargarGraficoCalificaciones();
        cargarGraficoDistribucionGrupos();
    }

    private void cargarGraficoDistribucionGrupos() {
    }

    private void cargarGraficoCalificaciones() {

    }
}