package project.movementdialog;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import project.Util.GenericLogger;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;

public class MovementDialogController implements Initializable, EventHandler<MouseEvent>
{
    private MovementDialogModel movementDialogModel;

    @FXML
    private TextArea textArea;

    @FXML
    private ListView<String> listView;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle)
    {
        try
        {
            movementDialogModel = new MovementDialogModel();
            movementDialogModel.getAllFilesFromDir(fileList ->
            {
                if (fileList.isEmpty())
                {
                    textArea.setText("Currently there is no train history");
                    return;
                }
                listView.getItems().addAll(fileList);
                listView.setOnMouseClicked(this);
                listView.setOnKeyPressed(keyEvent ->
                {
                    if (keyEvent.getCode().equals(KeyCode.ENTER))
                    {
                        handle(null);
                    }
                });
            });

        } catch (IOException | URISyntaxException ex)
        {
            GenericLogger.asyncLog(this.getClass(), Level.WARNING, "Unable to start Movement Dialog correctly" + getClass(), ex);
        }
    }

    @Override
    public void handle(MouseEvent mouseEvent)
    {
        var selectedItems = listView.getSelectionModel().getSelectedItems();
        if (selectedItems == null || selectedItems.isEmpty())
            return;

        String selectedItem = selectedItems.get(0);
        movementDialogModel.getFileContent(selectedItem, text -> textArea.setText(text));
    }

    @FXML
    void closeBtnClicked(ActionEvent event)
    {
        System.out.println("Close button clicked");
        Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
        stage.close();
    }

}
