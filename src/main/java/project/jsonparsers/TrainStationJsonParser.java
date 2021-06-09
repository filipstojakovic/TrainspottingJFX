package project.jsonparsers;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;
import project.map.Field.Field;
import project.map.Map;
import project.vehiclestuff.trainstuff.trainstation.TrainStation;

import java.io.IOException;
import java.util.HashMap;

public class TrainStationJsonParser extends JsonParser
{
    public static HashMap<String, TrainStation> getTrainStationsFromJson(String path) throws IOException, ParseException
    {
        HashMap<String, TrainStation> trainStations = new HashMap<>();
        JSONArray jsonArray = (JSONArray) getJsonObjectFromFile(path);

        // go line by line
        for (Object value : jsonArray)
        {
            JSONObject jsonObject = (JSONObject) value;
            String name = (String) jsonObject.get("name");

            TrainStation trainStation = new TrainStation();
            trainStation.setStationName(name);
            JSONArray reachableJsonArray = (JSONArray) jsonObject.get("reachable");
            for (Object o : reachableJsonArray)
            {
                JSONObject reachableJsonObj = (JSONObject) o;
                String reachableStationName = (String) reachableJsonObj.get("destination");
                int x = ((Long) reachableJsonObj.get("xPosition")).intValue();
                int y = ((Long) reachableJsonObj.get("yPosition")).intValue();
                Field field = Map.getField(x, y);
                trainStation.addDestinationField(reachableStationName, field);
            }
            trainStations.put(name, trainStation);
        }

        return trainStations;
    }
}
