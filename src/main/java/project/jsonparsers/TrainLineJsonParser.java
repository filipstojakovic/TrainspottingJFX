package project.jsonparsers;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import project.map.Field.Field;
import project.map.Field.RailField;
import project.map.Field.RampField;
import project.trainstuff.trainstation.TrainLine;

import java.util.ArrayList;
import java.util.List;

public class TrainLineJsonParser extends JsonParser
{
    public static List<TrainLine> createRoadListFromJson(String path)
    {
        List<TrainLine> trainLines = new ArrayList<>();
        try
        {
            JSONArray jsonArray = (JSONArray) getJsonObjectFromFile(path);

            for (int i = 0; i < jsonArray.size(); i++) // go line by line
            {
                JSONObject jsonObject = (JSONObject) jsonArray.get(i);
                try
                {
                    TrainLine trainLine = getTrainLineFromJson(jsonObject);
                    trainLines.add(trainLine);
                    trainLines.add(trainLine.createReverseLine());

                } catch (Exception ex)
                {
                    ex.printStackTrace();
                }
            }
        } catch (Exception ex)
        {
            ex.printStackTrace();
        }
        return trainLines;
    }

    private static TrainLine getTrainLineFromJson(JSONObject jsonObject)
    {

        String start = (String) jsonObject.get("start");
        String end = (String) jsonObject.get("end");
        List<Field> railRoad = new ArrayList<>();
        List<Field> ramps = new ArrayList<>();

        JSONArray lineJsonArray = (JSONArray) jsonObject.get("line");
        for (int i = 0; i < lineJsonArray.size(); i++)
        {
            JSONObject fieldJson = (JSONObject) lineJsonArray.get(i);

            int xPosition = ((Long) fieldJson.get("xPosition")).intValue();
            int yPosition = ((Long) fieldJson.get("yPosition")).intValue();

            railRoad.add(new RailField(xPosition, yPosition));
            if (fieldJson.containsKey("isRamp") && (boolean) fieldJson.get("isRamp"))
            {
                ramps.add(new RampField(xPosition, yPosition));
            }
        }

        return new TrainLine(start, end, railRoad, ramps);
    }
}
