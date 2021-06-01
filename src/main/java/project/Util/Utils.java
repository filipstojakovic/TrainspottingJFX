package project.Util;

import java.io.File;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Paths;

public abstract class Utils
{
    // failed if files have whitespaces or special characters
    public static File getFileFromResources(String name) throws URISyntaxException
    {
        URL resource = Utils.class.getClassLoader().getResource(name);
        if (resource == null)
        {
            throw new IllegalArgumentException("file not found!");
        } else
        {
            //return new File(resource.getFile());
            return new File(resource.toURI());
        }
    }

    public static void createFolderIfNotExists(String path)
    {
        if (!Paths.get(path).toFile().exists())
        {
            Paths.get(path).toFile().mkdir();
        }
    }

}
