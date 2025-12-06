package DAO;

import DataBase.ConexionBD;
import Model.Grupo;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class GrupoDAO {
    private Connection getConnection() { return ConexionBD.getInstance().getConnection(); }

    // ===================================================================
    // CREATE
    // ===================================================================
    public void save(Grupo grupo) {
        String sql = "INSERT INTO grupos (nombre_grupo, semestre) VALUES (?, ?)";
        Connection conn = getConnection();
        try (PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, grupo.getNombreGrupo());
            stmt.setInt(2, grupo.getSemestre());
            stmt.executeUpdate();
            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    grupo.setIdGrupo(rs.getInt(1));
                }
            }
        } catch (SQLException e) { System.err.println("Error al guardar grupo: " + e.getMessage()); }
    }

    // ===================================================================
    // READ (por ID)
    // ===================================================================
    public Grupo getById(int id) {
        Grupo grupo = null;
        String sql = "SELECT id_grupo, nombre_grupo, semestre FROM grupos WHERE id_grupo = ?";
        Connection conn = getConnection();
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    grupo = new Grupo(
                            rs.getInt("id_grupo"),
                            rs.getString("nombre_grupo"),
                            rs.getInt("semestre")
                    );
                }
            }
        } catch (SQLException e) { System.err.println("Error al obtener grupo por ID: " + e.getMessage()); }
        return grupo;
    }

    // ===================================================================
    // READ ALL
    // ===================================================================
    public List<Grupo> getAll() {
        List<Grupo> grupos = new ArrayList<>();
        String sql = "SELECT id_grupo, nombre_grupo, semestre FROM grupos";
        Connection conn = getConnection();
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Grupo grupo = new Grupo(
                        rs.getInt("id_grupo"),
                        rs.getString("nombre_grupo"),
                        rs.getInt("semestre")
                );
                grupos.add(grupo);
            }
        } catch (SQLException e) { System.err.println("Error al obtener todos los grupos: " + e.getMessage()); }
        return grupos;
    }

    // ===================================================================
    // UPDATE
    // ===================================================================
    public void update(Grupo grupo) {
        String sql = "UPDATE grupos SET nombre_grupo = ?, semestre = ? WHERE id_grupo = ?";
        Connection conn = getConnection();
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, grupo.getNombreGrupo());
            stmt.setInt(2, grupo.getSemestre());
            stmt.setInt(3, grupo.getIdGrupo());
            stmt.executeUpdate();
        } catch (SQLException e) { System.err.println("Error al actualizar grupo: " + e.getMessage()); }
    }

    // ===================================================================
    // DELETE
    // ===================================================================
    public void delete(int id) {
        String sql = "DELETE FROM grupos WHERE id_grupo = ?";
        Connection conn = getConnection();
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        } catch (SQLException e) { System.err.println("Error al eliminar grupo: " + e.getMessage()); }
    }
}