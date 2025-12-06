package DAO;

import DataBase.ConexionBD;
import Model.Parcial;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ParcialDAO {
    private Connection getConnection() { return ConexionBD.getInstance().getConnection(); }

    // ===================================================================
    // CREATE
    // ===================================================================
    public void save(Parcial parcial) {
        String sql = "INSERT INTO parciales (id_inscripcion, numero_parcial, calificacion_parcial) VALUES (?, ?, ?)";
        Connection conn = getConnection();
        try (PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setInt(1, parcial.getIdInscripcion());
            stmt.setInt(2, parcial.getNumeroParcial());
            stmt.setDouble(3, parcial.getCalificacionParcial());
            stmt.executeUpdate();

            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    parcial.setIdParcial(rs.getInt(1));
                }
            }
        } catch (SQLException e) { System.err.println("Error al guardar parcial: " + e.getMessage()); }
    }

    // ===================================================================
    // READ (por ID)
    // ===================================================================
    public Parcial getById(int id) {
        Parcial parcial = null;
        String sql = "SELECT id_parcial, id_inscripcion, numero_parcial, calificacion_parcial FROM parciales WHERE id_parcial = ?";
        Connection conn = getConnection();
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    parcial = new Parcial();
                    parcial.setIdParcial(rs.getInt("id_parcial"));
                    parcial.setIdInscripcion(rs.getInt("id_inscripcion"));
                    parcial.setNumeroParcial(rs.getInt("numero_parcial"));
                    parcial.setCalificacionParcial(rs.getDouble("calificacion_parcial"));
                }
            }
        } catch (SQLException e) { System.err.println("Error al obtener parcial por ID: " + e.getMessage()); }
        return parcial;
    }

    // ===================================================================
    // READ ALL
    // ===================================================================
    public List<Parcial> getAll() {
        List<Parcial> parciales = new ArrayList<>();
        String sql = "SELECT id_parcial, id_inscripcion, numero_parcial, calificacion_parcial FROM parciales";
        Connection conn = getConnection();
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Parcial p = new Parcial();
                p.setIdParcial(rs.getInt("id_parcial"));
                p.setIdInscripcion(rs.getInt("id_inscripcion"));
                p.setNumeroParcial(rs.getInt("numero_parcial"));
                p.setCalificacionParcial(rs.getDouble("calificacion_parcial"));
                parciales.add(p);
            }
        } catch (SQLException e) { System.err.println("Error al obtener todos los parciales: " + e.getMessage()); }
        return parciales;
    }

    // ===================================================================
    // READ (por Inscripción) - VITAL PARA EL SERVICIO
    // ===================================================================
    public List<Parcial> getParcialesByInscripcionId(int idInscripcion) {
        List<Parcial> parciales = new ArrayList<>();
        String sql = "SELECT id_parcial, numero_parcial, calificacion_parcial FROM parciales WHERE id_inscripcion = ?";
        Connection conn = getConnection();
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, idInscripcion);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Parcial p = new Parcial();
                    p.setIdParcial(rs.getInt("id_parcial"));
                    p.setNumeroParcial(rs.getInt("numero_parcial"));
                    p.setCalificacionParcial(rs.getDouble("calificacion_parcial"));
                    parciales.add(p);
                }
            }
        } catch (SQLException e) { System.err.println("Error al obtener parciales por Inscripción: " + e.getMessage()); }
        return parciales;
    }

    // ===================================================================
    // UPDATE
    // ===================================================================
    public void update(Parcial parcial) {
        String sql = "UPDATE parciales SET numero_parcial = ?, calificacion_parcial = ? WHERE id_parcial = ?";
        Connection conn = getConnection();
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, parcial.getNumeroParcial());
            stmt.setDouble(2, parcial.getCalificacionParcial());
            stmt.setInt(3, parcial.getIdParcial());
            stmt.executeUpdate();
        } catch (SQLException e) { System.err.println("Error al actualizar parcial: " + e.getMessage()); }
    }

    // ===================================================================
    // DELETE
    // ===================================================================
    public void delete(int id) {
        String sql = "DELETE FROM parciales WHERE id_parcial = ?";
        Connection conn = getConnection();
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        } catch (SQLException e) { System.err.println("Error al eliminar parcial: " + e.getMessage()); }
    }
}
