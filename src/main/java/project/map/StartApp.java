package project.map;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;

public class StartApp extends Application
{
    public static void main(String[] args)
    {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception
    {
        javafx.scene.Parent root = FXMLLoader.load(getClass().getClassLoader().getResource("MapView.fxml"));
        primaryStage.setTitle("Trainspotting");
        primaryStage.setScene(new javafx.scene.Scene(root));
        primaryStage.setMaximized(true);
        primaryStage.show();
    }
}
