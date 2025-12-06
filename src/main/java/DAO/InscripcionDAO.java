package DAO;

import DataBase.ConexionBD;
import Model.Inscripcion;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class InscripcionDAO {
    private Connection getConnection() { return ConexionBD.getInstance().getConnection(); }

    // ===================================================================
    // CREATE
    // ===================================================================
    public void save(Inscripcion inscripcion) {
        String sql = "INSERT INTO inscripciones (id_alumno, id_materia, fecha_inscripcion) VALUES (?, ?, ?)";
        Connection conn = getConnection();
        try (PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setInt(1, inscripcion.getIdAlumno());
            stmt.setInt(2, inscripcion.getIdMateria());
            stmt.setDate(3, Date.valueOf(inscripcion.getFechaInscripcion()));
            stmt.executeUpdate();
            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    inscripcion.setIdInscripcion(rs.getInt(1));
                }
            }
        } catch (SQLException e) { System.err.println("Error al guardar inscripción: " + e.getMessage()); }
    }

    // ===================================================================
    // READ (por ID)
    // ===================================================================
    public Inscripcion getById(int id) {
        Inscripcion inscripcion = null;
        String sql = "SELECT id_inscripcion, id_alumno, id_materia, fecha_inscripcion FROM inscripciones WHERE id_inscripcion = ?";
        Connection conn = getConnection();
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    inscripcion = new Inscripcion(
                            rs.getInt("id_inscripcion"),
                            rs.getInt("id_alumno"),
                            rs.getInt("id_materia"),
                            rs.getDate("fecha_inscripcion").toLocalDate()
                    );
                }
            }
        } catch (SQLException e) { System.err.println("Error al obtener inscripción por ID: " + e.getMessage()); }
        return inscripcion;
    }

    // ===================================================================
    // READ ALL
    // ===================================================================
    public List<Inscripcion> getAll() {
        List<Inscripcion> inscripciones = new ArrayList<>();
        String sql = "SELECT id_inscripcion, id_alumno, id_materia, fecha_inscripcion FROM inscripciones";
        Connection conn = getConnection();
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Inscripcion ins = new Inscripcion(
                        rs.getInt("id_inscripcion"),
                        rs.getInt("id_alumno"),
                        rs.getInt("id_materia"),
                        rs.getDate("fecha_inscripcion").toLocalDate()
                );
                inscripciones.add(ins);
            }
        } catch (SQLException e) { System.err.println("Error al obtener todas las inscripciones: " + e.getMessage()); }
        return inscripciones;
    }

    // ===================================================================
    // READ (por Alumno ID - Vital para el Servicio)
    // ===================================================================
    public List<Inscripcion> getByAlumnoId(int idAlumno) {
        List<Inscripcion> inscripciones = new ArrayList<>();
        String sql = "SELECT id_inscripcion, id_alumno, id_materia, fecha_inscripcion FROM inscripciones WHERE id_alumno = ?";
        Connection conn = getConnection();
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, idAlumno);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Inscripcion ins = new Inscripcion(
                            rs.getInt("id_inscripcion"),
                            rs.getInt("id_alumno"),
                            rs.getInt("id_materia"),
                            rs.getDate("fecha_inscripcion").toLocalDate()
                    );
                    inscripciones.add(ins);
                }
            }
        } catch (SQLException e) { System.err.println("Error al obtener inscripciones por alumno: " + e.getMessage()); }
        return inscripciones;
    }

    // ===================================================================
    // UPDATE
    // ===================================================================
    public void update(Inscripcion inscripcion) {
        String sql = "UPDATE inscripciones SET id_alumno = ?, id_materia = ?, fecha_inscripcion = ? WHERE id_inscripcion = ?";
        Connection conn = getConnection();
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, inscripcion.getIdAlumno());
            stmt.setInt(2, inscripcion.getIdMateria());
            stmt.setDate(3, Date.valueOf(inscripcion.getFechaInscripcion()));
            stmt.setInt(4, inscripcion.getIdInscripcion());
            stmt.executeUpdate();
        } catch (SQLException e) { System.err.println("Error al actualizar inscripción: " + e.getMessage()); }
    }

    // ===================================================================
    // DELETE
    // ===================================================================
    public void delete(int id) {
        String sql = "DELETE FROM inscripciones WHERE id_inscripcion = ?";
        Connection conn = getConnection();
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        } catch (SQLException e) { System.err.println("Error al eliminar inscripción: " + e.getMessage()); }
    }
}