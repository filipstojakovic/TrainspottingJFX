package project.jsonparsers;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;
import project.Util.GenericLogger;
import project.map.Field.Field;
import project.map.Field.RampField;
import project.map.Map;
import project.vehiclestuff.trainstuff.RailRoad;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class RailRoadJsonParser extends JsonParser
{
    // res/roads/railroads.json
    public static List<RailRoad> getRailRoadListFromJson(String path) throws IOException, ParseException
    {
        List<RailRoad> railRoads = new ArrayList<>();
        JSONArray jsonArray = (JSONArray) getJsonObjectFromFile(path);

        // go line by line
        for (Object obj : jsonArray)
        {
            JSONObject jsonObject = (JSONObject) obj;
            try
            {
                RailRoad trainLine = getRailRoadFromJson(jsonObject);
                railRoads.add(trainLine);
                railRoads.add(trainLine.createReverseLine());

            } catch (ClassCastException ex)
            {
                GenericLogger.createAsyncLog(RailRoadJsonParser.class, ex);
            }
        }
        return railRoads;
    }

    private static RailRoad getRailRoadFromJson(JSONObject jsonObject)
    {

        String start = (String) jsonObject.get("start");
        String end = (String) jsonObject.get("end");
        List<Field> railRoad = new ArrayList<>();
        List<RampField> ramps = new ArrayList<>();

        JSONArray lineJsonArray = (JSONArray) jsonObject.get("line");
        for (Object obj : lineJsonArray)
        {
            JSONObject fieldJson = (JSONObject) obj;

            int xPosition = ((Long) fieldJson.get("xPosition")).intValue();
            int yPosition = ((Long) fieldJson.get("yPosition")).intValue();

            railRoad.add(Map.getField(xPosition, yPosition));
            if (fieldJson.containsKey("isRamp") && (boolean) fieldJson.get("isRamp"))
            {
                ramps.add((RampField) Map.getField(xPosition, yPosition));
            }
        }

        return new RailRoad(start, end, railRoad, ramps);
    }
}
