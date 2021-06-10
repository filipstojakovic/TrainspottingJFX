package project.movementdialog;

import project.Util.Utils;
import project.constants.Constants;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;
import java.util.stream.Collectors;

import static project.constants.Constants.SAVED_TRAINS_DIR_PROP;

public class MovementDialogModel
{
    private Properties properties;

    public MovementDialogModel() throws IOException, URISyntaxException
    {
        properties = Utils.loadPropertie(Constants.CONFIGURATION_FILE);
    }

    public List<String> getAllFilesFromDir()
    {
        String trainsDir = properties.getProperty(SAVED_TRAINS_DIR_PROP);
        File directory = new File(trainsDir);
        var files = directory.listFiles();
        return Arrays.stream(files)
                .filter(File::isFile)
                .map(File::getName)
                .collect(Collectors.toList());
    }

    public String getFileContent(String fileName) throws IOException
    {
        String trainsDir = properties.getProperty(SAVED_TRAINS_DIR_PROP);
        String filePath = trainsDir + File.separator + fileName;

        //TODO: deserialize
        return String.join("\n", Files.readAllLines(Paths.get(filePath)));
    }
}
