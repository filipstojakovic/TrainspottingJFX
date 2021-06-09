package project.movementdialog;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class MovementDialogController implements Initializable
{
    @FXML
    private TextArea textArea;

    boolean isDialogActive;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle)
    {
        isDialogActive = true;
        startWatchingFile();
    }

    private void startWatchingFile()
    {

    }

    public void close()
    {
        isDialogActive = false;
    }

    @FXML
    void closeBtnClicked(ActionEvent event)
    {
        System.out.println("Close button clicked");
        Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
        close();
        stage.close();
    }

}
