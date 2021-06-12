package project.Util;

import project.constants.Constants;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;
import java.util.Random;

public abstract class Utils
{
    private static final Random random = new Random();
    public static final String DATE_FORMAT_PATTERN = "HH:mm:ss dd.MM.yyyy";

    // failed if files have whitespaces or special characters
    public static File getFileFromResources(String name) throws URISyntaxException
    {
        URL resource = Utils.class.getClassLoader().getResource(name);
        if (resource == null)
        {
            throw new IllegalArgumentException("File not found!");
        } else
        {
            //return new File(resource.getFile());
            return new File(resource.toURI());
        }
    }

    public static void createDefaultConfigFileIfNotExist() throws IOException
    {
        Utils.createFolderIfNotExists(Constants.CONFIGURATION_DIR);
        File file = new File(Constants.CONFIGURATION_FILE);
        if (!file.exists())
        {
            Files.writeString(file.toPath(), Constants.DEFAULT_CONFIG_FILE);
        }
    }

    public static int getRandomNumBetween(int lowBound, int upperBound)
    {
        return lowBound + random.nextInt(upperBound);
    }

    public static void createFolderIfNotExists(String path)
    {
        if (!Paths.get(path).toFile().exists())
        {
            Paths.get(path).toFile().mkdirs();
        }
    }

    public static String formatDate(long timeInMilliSeconds)
    {
        Date date = new Date(timeInMilliSeconds);
        DateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT_PATTERN);
        return dateFormat.format(date);
    }

    public static synchronized Properties loadPropertie(String path) throws IOException, URISyntaxException
    {
        Properties properties;
        try (InputStream inputStream = new FileInputStream(new File(path)))
        {
            properties = new Properties();
            properties.load(inputStream);
        }
        return properties;
    }
}
