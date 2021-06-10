package project.movementdialog;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

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
            listView.getItems().addAll(movementDialogModel.getAllFilesFromDir());
            listView.setOnMouseClicked(this);

        } catch (Exception ex)
        {
            ex.printStackTrace();
        }
    }

    @Override
    public void handle(MouseEvent mouseEvent)
    {
        var selectedItems = listView.getSelectionModel().getSelectedItems();
        if (selectedItems == null || selectedItems.isEmpty())
            return;

        String selectedItem = selectedItems.get(0);
        try
        {
            String fileContent = movementDialogModel.getFileContent(selectedItem);
            textArea.setText(fileContent);

        } catch (IOException ex)
        {
            textArea.setText("Unable to get file content");
            ex.printStackTrace();
        }
    }

    @FXML
    void closeBtnClicked(ActionEvent event)
    {
        System.out.println("Close button clicked");
        Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
        stage.close();
    }

}
