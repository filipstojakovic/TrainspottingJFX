package project.map;

import org.json.simple.parser.ParseException;
import project.Util.Utils;
import project.constants.Constants;
import project.jsonparsers.RailRoadJsonParser;
import project.jsonparsers.StreetRoadJsonParser;
import project.jsonparsers.TrainStationJsonParser;
import project.spawners.TrainSpawner;
import project.vehiclestuff.streetstuff.Street;
import project.vehiclestuff.streetstuff.StreetRoad;
import project.vehiclestuff.trainstuff.RailRoad;
import project.vehiclestuff.trainstuff.trainstation.TrainStation;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.util.*;

public class MapModel
{
    private Properties properties;


    public MapModel() throws Exception
    {
        properties = Utils.loadPropertie(Constants.CONFIGURATION_FILE);
    }

    public List<List<String>> getMap() throws Exception
    {
        //get path from properties
        String mapFile = properties.getProperty(Constants.TRAINSTATION_MAP_PROP);
        List<String> mapList = Files.readAllLines(Utils.getFileFromResources(mapFile).toPath());
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

    public HashMap<String, TrainStation> initializeTrainStationMap(List<RailRoad> railRoads) throws IOException, ParseException, URISyntaxException
    {
        String trainStationFile = properties.getProperty(Constants.TRAINSTATION_JSON_PROP);
        String trainStationPath = Utils.getFileFromResources(trainStationFile).getPath();
        HashMap<String, TrainStation> trainStationMap = TrainStationJsonParser.getTrainStationsFromJson(trainStationPath);
        for (var railRoad : railRoads)
        {
            String trainStationName = railRoad.getStartStationName();
            TrainStation trainStation = trainStationMap.get(trainStationName);

            trainStation.addTrainLine(railRoad);
        }
        return trainStationMap;
    }

    public TrainSpawner initTrainSpawner(HashMap<String, TrainStation> trainStationMap) throws URISyntaxException
    {
        String trainDir = properties.getProperty(Constants.TRAINS_DIR_PROP);
        return new TrainSpawner(trainDir, trainStationMap);
    }

    public List<RailRoad> initRailRoads()
    {
        return RailRoadJsonParser.getRailRoadListFromJson("./src/main/resources/mapstuff/roads/railroads.json");
    }

    public List<Street> initializeStreets()
    {
        List<Street> streets = new ArrayList<>();
        List<StreetRoad> streetRoads = StreetRoadJsonParser.getStreetRoadsFromJson("./src/main/resources/mapstuff/roads/streetroads.json");
        for (StreetRoad streetRoad : streetRoads)
        {
            Street street = doesStreetAlreadyExist(streetRoad.getName(), streets);
            if (street != null)
                street.addStreetRoad(streetRoad);
            else
            {
                street = new Street(streetRoad.getName(), streetRoad);
                streets.add(street);
            }
        }
        return streets;
    }

    private Street doesStreetAlreadyExist(String name, List<Street> streets)
    {
        return streets.stream()
                .filter(x -> name.equals(x.getStreetName()))
                .findFirst().orElse(null);
    }
}
