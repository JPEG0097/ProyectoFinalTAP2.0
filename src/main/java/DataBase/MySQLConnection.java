package DataBase;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class MySQLConnection {

    private static MySQLConnection instance;
    private Connection connection;

    private static final String URL = "jdbc:mysql://localhost:3306/sistema_calificaciones";
    private static final String USER = "root";
    private static final String PASSWORD = "12345Juanpi";

    private MySQLConnection() {
        try {
            connection = DriverManager.getConnection(URL, USER, PASSWORD);
            System.out.println("Conexión a MySQL exitosa.");
        } catch (SQLException e) {
            System.out.println("Error al conectar con MySQL: " + e.getMessage());
        }
    }

    public static MySQLConnection getInstance() {
        if (instance == null) {
            instance = new MySQLConnection();
        }
        return instance;
    }

    public Connection getConnection() {
        return connection;
    }
}
