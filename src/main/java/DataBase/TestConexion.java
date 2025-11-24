package DataBase;

import DataBase.MySQLConnection;
import java.sql.Connection;

public class TestConexion {

    public static void main(String[] args) {
        MySQLConnection conexionBD = MySQLConnection.getInstance();
        Connection conn = conexionBD.getConnection();
        if (conn != null) {
            System.out.println("La conexión fue exitosa.");
        } else {
            System.out.println("La conexión falló.");
        }
    }
}
