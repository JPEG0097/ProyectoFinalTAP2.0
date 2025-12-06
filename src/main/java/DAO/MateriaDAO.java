package DAO;

import DataBase.ConexionBD;
import Model.Materia;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MateriaDAO {

    private Connection getConnection() { return ConexionBD.getInstance().getConnection(); }

    // ===================================================================
    // CREATE
    // ===================================================================
    public void save(Materia materia) {
        String sql = "INSERT INTO materias (nombre, codigo_materia, creditos) VALUES (?, ?, ?)";
        Connection conn = getConnection();
        try (PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, materia.getNombre());
            stmt.setString(2, materia.getCodigoMateria());
            stmt.setInt(3, materia.getCreditos());
            stmt.executeUpdate();
            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    materia.setIdMateria(rs.getInt(1));
                }
            }
        } catch (SQLException e) { System.err.println("Error al guardar materia: " + e.getMessage()); }
    }

    // ===================================================================
    // READ (por ID)
    // ===================================================================
    public Materia getById(int id) {
        Materia materia = null;
        String sql = "SELECT id_materia, nombre, codigo_materia, creditos FROM materias WHERE id_materia = ?";
        Connection conn = getConnection();
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    materia = new Materia();
                    materia.setIdMateria(rs.getInt("id_materia"));
                    materia.setNombre(rs.getString("nombre"));
                    materia.setCodigoMateria(rs.getString("codigo_materia"));
                    materia.setCreditos(rs.getInt("creditos"));
                }
            }
        } catch (SQLException e) { System.err.println("Error al obtener materia por ID: " + e.getMessage()); }
        return materia;
    }

    // ===================================================================
    // READ ALL
    // ===================================================================
    public List<Materia> getAll() {
        List<Materia> materias = new ArrayList<>();
        String sql = "SELECT id_materia, nombre, codigo_materia, creditos FROM materias";
        Connection conn = getConnection();
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Materia materia = new Materia();
                materia.setIdMateria(rs.getInt("id_materia"));
                materia.setNombre(rs.getString("nombre"));
                materia.setCodigoMateria(rs.getString("codigo_materia"));
                materia.setCreditos(rs.getInt("creditos"));
                materias.add(materia);
            }
        } catch (SQLException e) { System.err.println("Error al obtener todas las materias: " + e.getMessage()); }
        return materias;
    }

    // ===================================================================
    // UPDATE
    // ===================================================================
    public void update(Materia materia) {
        String sql = "UPDATE materias SET nombre = ?, codigo_materia = ?, creditos = ? WHERE id_materia = ?";
        Connection conn = getConnection();
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, materia.getNombre());
            stmt.setString(2, materia.getCodigoMateria());
            stmt.setInt(3, materia.getCreditos());
            stmt.setInt(4, materia.getIdMateria());
            stmt.executeUpdate();
        } catch (SQLException e) { System.err.println("Error al actualizar materia: " + e.getMessage()); }
    }

    // ===================================================================
    // DELETE
    // ===================================================================
    public void delete(int id) {
        String sql = "DELETE FROM materias WHERE id_materia = ?";
        Connection conn = getConnection();
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        } catch (SQLException e) { System.err.println("Error al eliminar materia: " + e.getMessage()); }
    }
}