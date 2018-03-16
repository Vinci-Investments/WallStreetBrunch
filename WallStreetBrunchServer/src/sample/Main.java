package sample;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;

public class Main extends Application
{
    @Override public void start(Stage primaryStage) throws Exception
    {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("sample.fxml"));
        Parent root = loader.load();
        primaryStage.setTitle("VI WSBrunch Server");
        primaryStage.setScene(new Scene(root));

        Controller ctrl = loader.getController();
        PrintStream ps = new PrintStream(new Console(ctrl.getLogTxtArea()), true);
        System.setOut(ps);
        System.setErr(ps);

        primaryStage.sizeToScene();
        primaryStage.setResizable(false);
        primaryStage.show();

        System.out.println("SERVER INITIALIZED. MAY THE BRUNCH BE GOOD");
        ctrl.getProductListFromCluster();
    }

    public static class Console extends OutputStream
    {
        private TextArea output;

        public Console(TextArea ta) {
            this.output = ta;
        }

        @Override public void write(int i) throws IOException {
            Platform.runLater(() -> output.appendText(String.valueOf((char) i)));
        }
    }
}
