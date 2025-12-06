package DataBase;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConexionBD { // Renombrada de MySQLConnection

    private static ConexionBD instance;
    private Connection connection;

    private static final String URL = "jdbc:mysql://localhost:3306/sistema_calificaciones";
    private static final String USER = "root";
    private static final String PASSWORD = "12345Juanpi"; // ¡Asegúrate de que esta sea tu contraseña real!

    private ConexionBD() {
        try {
            // El driver ya no necesita cargarse con Class.forName() en las versiones modernas de JDBC
            connection = DriverManager.getConnection(URL, USER, PASSWORD);
            System.out.println("Conexión a MySQL exitosa.");
        } catch (SQLException e) {
            System.err.println("Error al conectar con MySQL. Revise si el servidor está activo y las credenciales: " + e.getMessage());
            // Si la conexión falla, es un error fatal.
        }
    }

    public static ConexionBD getInstance() {
        if (instance == null) {
            instance = new ConexionBD();
        }
        return instance;
    }

    public Connection getConnection() {
        // Validación para asegurar que la conexión no sea nula si falló al inicio
        return connection;
    }

    // Método para cerrar la conexión (Importante para un Singleton)
    public void closeConnection() {
        if (connection != null) {
            try {
                connection.close();
                System.out.println("Conexión a MySQL cerrada.");
            } catch (SQLException e) {
                System.err.println("Error al cerrar la conexión: " + e.getMessage());
            }
        }
    }
}