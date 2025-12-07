
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class Main extends Application {

    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("/org/example/proyectofinaltap/LoginView.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 800, 600);
        stage.setTitle("Gestion de calificaciones");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        //inicia la conexión (Singleton)
        DataBase.ConexionBD.getInstance();

        launch(args);
    }
}