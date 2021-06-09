package project.jsonparsers;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import project.map.Field.Field;
import project.map.Field.RampField;
import project.map.Map;
import project.vehiclestuff.trainstuff.RailRoad;

import java.util.ArrayList;
import java.util.List;

public class RailRoadJsonParser extends JsonParser
{
    public static List<RailRoad> getRailRoadListFromJson(String path)
    {
        List<RailRoad> railRoads = new ArrayList<>();
        try
        {
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

                } catch (Exception ex)
                {
                    ex.printStackTrace();
                }
            }
        } catch (Exception ex)
        {
            ex.printStackTrace();
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
        for (int i = 0; i < lineJsonArray.size(); i++)
        {
            JSONObject fieldJson = (JSONObject) lineJsonArray.get(i);

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
