package DAO;

import DataBase.ConexionBD;
import Model.Docente;
import Model.Usuario;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DocenteDAO {

    private Connection getConnection() { return ConexionBD.getInstance().getConnection(); }
    private final UsuarioDAO usuarioDAO = new UsuarioDAO();

    // ===================================================================
    // CREATE (Registro completo de Docente: Usuario + Docente)
    // ===================================================================
    public void save(Docente docente, Usuario usuario) {
        Connection conn = getConnection();
        try {
            usuario.setRol("docente");
            usuarioDAO.save(usuario);
            int idUsuario = usuario.getIdUsuario();
            docente.setIdDocente(idUsuario);

            String sql = "INSERT INTO docentes (id_docente, nombre) VALUES (?, ?)";
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setInt(1, idUsuario);
                stmt.setString(2, docente.getNombre());
                stmt.executeUpdate();
            }

        } catch (Exception e) { System.err.println("Error al registrar docente: " + e.getMessage()); }
    }

    // ===================================================================
    // READ (Obtener por ID)
    // ===================================================================
    public Docente getById(int id) {
        Docente docente = null;
        String sql = "SELECT id_docente, nombre FROM docentes WHERE id_docente = ?";
        Connection conn = getConnection();
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    docente = new Docente();
                    docente.setIdDocente(rs.getInt("id_docente"));
                    docente.setNombre(rs.getString("nombre"));
                }
            }
        } catch (SQLException e) { System.err.println("Error al obtener docente por ID: " + e.getMessage()); }
        return docente;
    }

    // ===================================================================
    // READ (Obtener todos)
    // ===================================================================
    public List<Docente> getAll() {
        List<Docente> docentes = new ArrayList<>();
        String sql = "SELECT id_docente, nombre FROM docentes";
        Connection conn = getConnection();
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Docente docente = new Docente();
                docente.setIdDocente(rs.getInt("id_docente"));
                docente.setNombre(rs.getString("nombre"));
                docentes.add(docente);
            }
        } catch (SQLException e) { System.err.println("Error al obtener todos los docentes: " + e.getMessage()); }
        return docentes;
    }

    // ===================================================================
    // UPDATE
    // ===================================================================
    public void update(Docente docente) {
        String sql = "UPDATE docentes SET nombre = ? WHERE id_docente = ?";
        Connection conn = getConnection();
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, docente.getNombre());
            stmt.setInt(2, docente.getIdDocente());
            stmt.executeUpdate();
        } catch (SQLException e) { System.err.println("Error al actualizar docente: " + e.getMessage()); }
    }

    // ===================================================================
    // DELETE
    // ===================================================================
    public void delete(int id) {
        String sql = "DELETE FROM docentes WHERE id_docente = ?";
        Connection conn = getConnection();
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        } catch (SQLException e) { System.err.println("Error al eliminar docente: " + e.getMessage()); }
    }
}