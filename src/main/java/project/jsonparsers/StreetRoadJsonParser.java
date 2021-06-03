package project.jsonparsers;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import project.map.Map;
import project.streetstuff.StreetRoad;

import java.util.ArrayList;
import java.util.List;

public abstract class StreetRoadJsonParser extends JsonParser
{
    public static List<StreetRoad> getStreetRoadsFromJson(String path)
    {
        List<StreetRoad> streetRoads = new ArrayList<>();
        try
        {
            JSONArray jsonArray = (JSONArray) getJsonObjectFromFile(path);
            for (Object obj : jsonArray)
            {
                JSONArray streetJsonArray = (JSONArray) obj;
                StreetRoad streetRoad = getStreetRoad(streetJsonArray);
                streetRoads.add(streetRoad);
            }

        } catch (Exception ex)
        {
            ex.printStackTrace();
        }

        return streetRoads;
    }

    private static StreetRoad getStreetRoad(JSONArray streetJsonArray)
    {
        StreetRoad streetRoad = new StreetRoad();
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
