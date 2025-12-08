package DAO;

import DataBase.ConexionBD;
import Model.Usuario;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UsuarioDAO {
    private Connection getConnection() { return ConexionBD.getInstance().getConnection(); }

    // CREATE (Autenticación)
    public Usuario autenticar(String nombreUsuario, String contrasenaIngresada) {
        String sql = "SELECT id_usuario, nombre_usuario, rol FROM usuarios WHERE nombre_usuario = ? AND (contraseña = ? OR contraseña = SHA1(?))";
        Usuario usuario = null;
        Connection conn = getConnection();
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, nombreUsuario);
            stmt.setString(2, contrasenaIngresada);
            stmt.setString(3, contrasenaIngresada);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    usuario = new Usuario();
                    usuario.setIdUsuario(rs.getInt("id_usuario"));
                    usuario.setNombreUsuario(rs.getString("nombre_usuario"));
                    usuario.setRol(rs.getString("rol"));
                }
            }
        } catch (SQLException e) { System.err.println("Error al autenticar: " + e.getMessage()); }
        return usuario;
    }

    // CREATE (Registro)
    public void save(Usuario usuario) {
        String sql = "INSERT INTO usuarios (nombre_usuario, contraseña, rol) VALUES ("
                // Si ya viene como hash SHA-1 (40 hex), lo insertamos tal cual; de lo contrario, aplicamos SHA1 en SQL
                + "? , "
                + "CASE WHEN LENGTH(?) = 40 AND ? REGEXP '^[0-9a-fA-F]{40}$' THEN ? ELSE SHA1(?) END, "
                + "? )";
        Connection conn = getConnection();
        try (PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, usuario.getNombreUsuario());
            stmt.setString(2, usuario.getContraseña());
            stmt.setString(3, usuario.getContraseña());
            stmt.setString(4, usuario.getContraseña());
            stmt.setString(5, usuario.getContraseña());
            stmt.setString(6, usuario.getRol());
            stmt.executeUpdate();
            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    usuario.setIdUsuario(rs.getInt(1));
                }
            }
        } catch (SQLException e) { System.err.println("Error al guardar usuario: " + e.getMessage()); }
    }

    // READ ALL
    public List<Usuario> getAll() {
        List<Usuario> usuarios = new ArrayList<>();
        String sql = "SELECT id_usuario, nombre_usuario, rol FROM usuarios";
        Connection conn = getConnection();
        try (Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Usuario usuario = new Usuario();
                usuario.setIdUsuario(rs.getInt("id_usuario"));
                usuario.setNombreUsuario(rs.getString("nombre_usuario"));
                usuario.setRol(rs.getString("rol"));
                usuarios.add(usuario);
            }
        } catch (SQLException e) { System.err.println("Error al obtener usuarios: " + e.getMessage()); }
        return usuarios;
    }

    // UPDATE, DELETE (implementación similar a otros DAOs)
    public void update(Usuario usuario) { /* ... */ }
    public void delete(int id) { /* ... */ }
}