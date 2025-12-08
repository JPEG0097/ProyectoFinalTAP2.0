package Security;

import Model.Usuario;

public class SessionManager {
    private static Usuario usuarioActual;
    private static String token;

    public static void iniciarSesion(Usuario usuario) {
        usuarioActual = usuario;
        token = generarToken(usuario);
        registrarActividad("Inicio de sesión");
    }

    public static void cerrarSesion() {
        registrarActividad("Cierre de sesión");
        usuarioActual = null;
        token = null;
    }

    public static boolean tienePermiso(String permiso) {
        if (usuarioActual == null) return false;

        return switch (usuarioActual.getRol()) {
            case "administrador" -> true; // Admins tienen todos los permisos
            case "docente" -> permiso.startsWith("docente.");
            case "alumno" -> permiso.startsWith("alumno.");
            default -> false;
        };
    }

    private static String generarToken(Usuario usuario) {
        return usuario.getIdUsuario() + "-" + System.currentTimeMillis();
    }

    private static void registrarActividad(String actividad) {
        // Registrar en log de auditoría
    }
}