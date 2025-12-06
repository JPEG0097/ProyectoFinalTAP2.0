package DAO;

import DataBase.ConexionBD;
import Model.Alumno;
import Model.Usuario;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AlumnoDAO {

    private Connection getConnection() { return ConexionBD.getInstance().getConnection(); }
    private final UsuarioDAO usuarioDAO = new UsuarioDAO();

    // ===================================================================
    // CREATE (Registro completo de Alumno: Usuario + Alumno)
    // ===================================================================
    /**
     * Registra un nuevo login de Usuario (rol 'alumno') y luego los datos del Alumno.
     */
    public void save(Alumno alumno, Usuario usuario) {
        Connection conn = getConnection();
        try {
            // 1. Registrar el Usuario (se genera el id_usuario)
            usuario.setRol("alumno");
            usuarioDAO.save(usuario);

            int idUsuario = usuario.getIdUsuario();
            alumno.setIdUsuario(idUsuario);

            // 2. Registrar los datos específicos del Alumno
            String sql = "INSERT INTO alumnos (id_usuario, nombre, matricula, id_grupo) VALUES (?, ?, ?, ?)";
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setInt(1, idUsuario);
                stmt.setString(2, alumno.getNombre());
                stmt.setString(3, alumno.getMatricula());
                stmt.setInt(4, alumno.getIdGrupo());
                stmt.executeUpdate();
            }

        } catch (Exception e) {
            System.err.println("Error al registrar alumno (2-step process): " + e.getMessage());
        }
    }

    // ===================================================================
    // READ (Obtener por ID de Usuario - VITAL para Login de Alumno)
    // ===================================================================
    public Alumno getByUserId(int idUsuario) {
        Alumno alumno = null;
        String sql = "SELECT id_alumno, id_usuario, nombre, matricula, id_grupo FROM alumnos WHERE id_usuario = ?";
        Connection conn = getConnection();
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, idUsuario);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    alumno = new Alumno();
                    alumno.setIdAlumno(rs.getInt("id_alumno"));
                    alumno.setIdUsuario(rs.getInt("id_usuario"));
                    alumno.setNombre(rs.getString("nombre"));
                    alumno.setMatricula(rs.getString("matricula"));
                    alumno.setIdGrupo(rs.getInt("id_grupo"));
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener alumno por ID de Usuario: " + e.getMessage());
        }
        return alumno;
    }

    // ===================================================================
    // READ ALL
    // ===================================================================
    public List<Alumno> getAll() {
        List<Alumno> alumnos = new ArrayList<>();
        String sql = "SELECT id_alumno, id_usuario, nombre, matricula, id_grupo FROM alumnos";
        Connection conn = getConnection();
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Alumno alumno = new Alumno();
                alumno.setIdAlumno(rs.getInt("id_alumno"));
                alumno.setIdUsuario(rs.getInt("id_usuario"));
                alumno.setNombre(rs.getString("nombre"));
                alumno.setMatricula(rs.getString("matricula"));
                alumno.setIdGrupo(rs.getInt("id_grupo"));
                alumnos.add(alumno);
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener todos los alumnos: " + e.getMessage());
        }
        return alumnos;
    }

    // ===================================================================
    // UPDATE
    // ===================================================================
    public void update(Alumno alumno) {
        String sql = "UPDATE alumnos SET nombre = ?, matricula = ?, id_grupo = ? WHERE id_alumno = ?";
        Connection conn = getConnection();
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, alumno.getNombre());
            stmt.setString(2, alumno.getMatricula());
            stmt.setInt(3, alumno.getIdGrupo());
            stmt.setInt(4, alumno.getIdAlumno());
            stmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Error al actualizar alumno: " + e.getMessage());
        }
    }

    // ===================================================================
    // DELETE
    // ===================================================================
    public void delete(int id) {
        String sql = "DELETE FROM alumnos WHERE id_alumno = ?";
        Connection conn = getConnection();
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Error al eliminar alumno: " + e.getMessage());
        }
    }
}