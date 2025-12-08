package Config;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class AppConfig {
    private static final Properties propiedades = new Properties();
    private static boolean cargado = false;

    public static void cargarConfiguracion() {
        try (FileInputStream fis = new FileInputStream("config.properties")) {
            propiedades.load(fis);
            cargado = true;
        } catch (IOException e) {
            System.err.println("No se pudo cargar config.properties, usando valores por defecto");
            // Valores por defecto
            propiedades.setProperty("db.url", "jdbc:mysql://localhost:3306/sistema_calificaciones");
            propiedades.setProperty("db.user", "root");
            propiedades.setProperty("app.nombre", "Sistema de Calificaciones");
        }
    }

    public static String get(String clave) {
        if (!cargado) cargarConfiguracion();
        return propiedades.getProperty(clave);
    }

    public static int getInt(String clave) {
        return Integer.parseInt(get(clave));
    }

    public static boolean getBoolean(String clave) {
        return Boolean.parseBoolean(get(clave));
    }
}