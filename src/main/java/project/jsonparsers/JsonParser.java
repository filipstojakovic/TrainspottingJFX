package project.jsonparsers;

import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public abstract class JsonParser
{
    protected static JSONParser parser = new JSONParser();

    protected static Object getJsonObjectFromFile(String path) throws IOException, ParseException
    {
        Object json;
        try (FileReader fileReader = new FileReader(path))
        {
            json = parser.parse(fileReader);
        }
        return json;
    }

    protected static Object getJsonObjectFromFile(File path) throws IOException, ParseException
    {
        return getJsonObjectFromFile(path.getPath());
    }
}
