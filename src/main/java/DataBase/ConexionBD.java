package DataBase;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConexionBD {

    private static ConexionBD instance;
    private Connection connection;

    private static final String URL = "jdbc:mysql://localhost:3306/sistema_calificaciones";
    private static final String USER = "root";
    private static final String PASSWORD = "12345Juanpi";

    private ConexionBD() {
        try {
            connection = DriverManager.getConnection(URL, USER, PASSWORD);
            System.out.println("Conexión a MySQL exitosa.");
        } catch (SQLException e) {
            System.err.println("Error al conectar con MySQL. Revise si el servidor está activo y las credenciales: " + e.getMessage());
        }
    }

    public static ConexionBD getInstance() {
        if (instance == null) {
            instance = new ConexionBD();
        }
        return instance;
    }

    public Connection getConnection() {
        return connection;
    }

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