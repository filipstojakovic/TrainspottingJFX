package project.map;

import project.Util.Utils;
import project.constants.Constants;
import project.jsonparsers.StreetRoadJsonParser;
import project.jsonparsers.TrainStationJsonParser;
import project.streetstuff.StreetRoad;
import project.trainstuff.RailRoad;
import project.trainstuff.trainstation.TrainStation;

import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class MapModel
{

    public List<List<String>> getMap() throws Exception
    {
        List<String> mapList = Files.readAllLines(Utils.getFileFromResources(Constants.TRAINSTATION_MAP_TXT).toPath());
        mapList.remove(0);                          //trim first row
        mapList = mapList.subList(0, Constants.MAP_DIM); // trim after 30 rows

        List<List<String>> mapValues = new ArrayList<>();
        for (String row : mapList)
        {
            List<String> rowValues = Arrays.asList(row.split(" "));
            mapValues.add(rowValues);
        }
        return mapValues;
    }

    public HashMap<String, TrainStation> initializeRailRoads(List<RailRoad> railRoads)
    {
        HashMap<String, TrainStation> trainStationMap = TrainStationJsonParser.getTrainStationsFromJson("./src/main/resources/train_stations.json");
        for (var railRoad : railRoads)
        {
            String trainStationName = railRoad.getStartStationName();
            TrainStation trainStation = trainStationMap.get(trainStationName);

            trainStation.addTrainLine(railRoad);
        }
        return trainStationMap;
    }

    public List<StreetRoad> initializeStreetRoads()
    {
        return StreetRoadJsonParser.getStreetRoadsFromJson("./src/main/resources/roads/streetroads.json");
    }
}
