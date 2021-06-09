package project.Util;

import java.io.File;
import java.io.FileWriter;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Paths;

public abstract class Utils
{
    public static final Object LOCK = new Object();

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

    public static void writeToFile(String text, String filePath, boolean apppend)
    {
        synchronized (LOCK)
        {
            try (FileWriter fw = new FileWriter(filePath, apppend))
            {
                fw.write(text);
            } catch (Exception ex)
            {
                ex.printStackTrace();
            }
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
