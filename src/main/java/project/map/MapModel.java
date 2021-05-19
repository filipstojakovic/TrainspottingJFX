package project.map;

import project.constants.Constants;
import project.Util.Utils;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MapModel
{

    public List<List<String>> getMap() throws Exception
    {
        List<String> mapList = Files.readAllLines(Utils.getFileFromResources(Constants.TRAINSTATION_MAP_TXT).toPath());
        mapList.remove(0);
        mapList = mapList.subList(0, Constants.MAP_DIM);

        List<List<String>> mapValues = new ArrayList<>();
        for (String row : mapList)
        {
            List<String> rowValues = Arrays.asList(row.split(" "));
            mapValues.add(rowValues);
        }
        return mapValues;
    }
}
