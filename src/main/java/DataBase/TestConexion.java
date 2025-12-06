package DataBase;

import java.sql.Connection;

public class TestConexion {

    public static void main(String[] args) {
        ConexionBD conexionBD = ConexionBD.getInstance();
        Connection conn = conexionBD.getConnection();
        if (conn != null) {
            System.out.println("La conexión fue exitosa.");
        } else {
            System.out.println("La conexión falló.");
        }
    }
}
