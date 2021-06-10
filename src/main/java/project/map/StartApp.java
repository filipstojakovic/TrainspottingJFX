package project.map;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

public class StartApp extends Application implements EventHandler<WindowEvent>
{

    public static void main(String[] args)
    {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage)
    {
        try
        {
            javafx.scene.Parent root = FXMLLoader.load(getClass().getClassLoader().getResource("./fxmls/MapView.fxml"));
            primaryStage.setTitle("Trainspotting");
            primaryStage.setScene(new javafx.scene.Scene(root));
            primaryStage.setResizable(false);
            primaryStage.setOnCloseRequest(this); // call public void handle()
            primaryStage.show();

        } catch (Exception ex)
        {
            ex.printStackTrace();
        }
    }

    @Override
    public void handle(WindowEvent windowEvent)
    {
        Platform.exit();
        System.exit(0);
    }


    //Note: NOT closing all stages for some reason
    //@Override
    //public void stop() throws Exception
    //{
    //    Platform.exit();
    //    System.exit(0);
    //    super.stop();
    //}
}
