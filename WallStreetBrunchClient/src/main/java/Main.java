package sample;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    @Override public void start(Stage primaryStage) throws Exception
    {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/sample.fxml"));
        Parent root = loader.load();
        Controller ctrl = loader.getController();

        primaryStage.setTitle("Vinci Investments - The Wall Street Brunch");
        primaryStage.setScene(new Scene(root, 1900, 900));
        primaryStage.sizeToScene();
        primaryStage.show();
    }
}
