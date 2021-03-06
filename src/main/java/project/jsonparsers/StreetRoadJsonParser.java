package project.jsonparsers;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;
import project.map.Map;
import project.vehiclestuff.streetstuff.StreetRoad;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public abstract class StreetRoadJsonParser extends JsonParser
{
    public static List<StreetRoad> getStreetRoadsFromJson(String path) throws IOException, ParseException
    {
        List<StreetRoad> streetRoads = new ArrayList<>();
        JSONArray jsonArray = (JSONArray) getJsonObjectFromFile(path);
        for (Object obj : jsonArray)
        {
            JSONObject streetJson = (JSONObject) obj;
            StreetRoad streetRoad = getStreetRoad(streetJson);
            streetRoads.add(streetRoad);
        }

        return streetRoads;
    }

    private static StreetRoad getStreetRoad(JSONObject streetJson)
    {
        StreetRoad streetRoad = new StreetRoad();
        String streetName = (String) streetJson.get("name");
        streetRoad.setName(streetName);
        JSONArray streetJsonArray = (JSONArray) streetJson.get("line");
        for (Object obj : streetJsonArray)
        {
            JSONObject streetFieldJson = (JSONObject) obj;
            int x = ((Long) streetFieldJson.get("x")).intValue();
            int y = ((Long) streetFieldJson.get("y")).intValue();
            streetRoad.addStreetField(Map.getField(x, y));

        }
        return streetRoad;
    }
}
