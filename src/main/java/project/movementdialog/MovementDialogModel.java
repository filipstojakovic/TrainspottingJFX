package project.movementdialog;

import javafx.application.Platform;
import project.Util.Utils;
import project.constants.Constants;
import project.vehiclestuff.trainstuff.TrainHistory;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import static project.constants.Constants.TRAIN_HISTORY_DIR_PROP;

public class MovementDialogModel
{
    private Properties properties;

    public MovementDialogModel() throws IOException, URISyntaxException
    {
        properties = Utils.loadPropertie(Constants.CONFIGURATION_FILE);
    }

    public void getAllFilesFromDir(Consumer<List<String>> function)
    {
        new Thread(() ->
        {
            String trainsDir = properties.getProperty(TRAIN_HISTORY_DIR_PROP);
            Utils.createFolderIfNotExists(trainsDir);
            File directory = new File(trainsDir);
            var files = directory.listFiles();
            var fileList = Arrays.stream(files)
                    .filter(File::isFile)
                    .map(File::getName)
                    .collect(Collectors.toList());

            Platform.runLater(() -> function.accept(fileList));

        }).start();

    }

    public void getFileContent(String fileName, Consumer<String> function)
    {
        new Thread(() ->
        {
            String trainsDir = properties.getProperty(TRAIN_HISTORY_DIR_PROP);
            String filePath = trainsDir + File.separator + fileName;

            try (ObjectInputStream oos = new ObjectInputStream(new FileInputStream(filePath)))
            {
                TrainHistory trainHistory = (TrainHistory) oos.readObject();
                String details = trainHistory.toString();
                Platform.runLater(() -> function.accept(details));
            } catch (Exception ex)
            {
                ex.printStackTrace();
            }
        }).start();
    }
}
