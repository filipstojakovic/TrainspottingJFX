package project.jsonparsers;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import project.trainstuff.Train;
import project.trainstuff.TrainPart;
import project.trainstuff.locomotive.CargoLocomotive;
import project.trainstuff.locomotive.ManeuverLocomotive;
import project.trainstuff.locomotive.PassengerLocomotive;
import project.trainstuff.locomotive.UniversalLocomotive;
import project.trainstuff.trainstation.TrainStation;
import project.trainstuff.wagon.*;

import java.util.*;

public class TrainJsonParser extends JsonParser
{
    public static Train getTrainPartsFromJson(HashMap<String, TrainStation> trainstationHashMap, String trainPath)
    {
        Train train = new Train();
        try
        {
            //JSONObject obj = (JSONObject) getJsonObjectFromFile(trainPath);
            JSONObject obj = (JSONObject) getJsonObjectFromFile("./src/main/resources/trains/line1.json");

            List<TrainPart> trainPartList = new ArrayList<>();
            JSONArray jsonArray = (JSONArray) obj.get("train");
            for (Object arrayObject : jsonArray)
            {
                JSONObject jsonObject = (JSONObject) arrayObject;
                TrainPart trainPart = getTrainPartByClassName((String) jsonObject.get("type"));
                //todo: if locomotive need Engine
                //train signature
                trainPartList.add(trainPart);
            }

            Queue<TrainStation> stationsOrder = getStationOrder(trainstationHashMap, obj);

            train.setDestinationStationsOrder(stationsOrder);
            train.setTrainPartList(trainPartList);

        } catch (Exception ex)
        {
            ex.printStackTrace();
        }

        return train;
    }

    private static Queue<TrainStation> getStationOrder(HashMap<String, TrainStation> trainstationHashMap, JSONObject obj)
    {
        Queue<TrainStation> destinationOrder = new LinkedList<>();
        JSONArray jsonArray = (JSONArray) obj.get("stationOrder");
        for (int i = 0; i < jsonArray.size(); i++)
        {
            String stationName = (String) jsonArray.get(i);
            destinationOrder.add(trainstationHashMap.get(stationName));
        }
        return destinationOrder;
    }

    public static TrainPart getTrainPartByClassName(String className)
    {
        //        Class myClass = Class.forName(className); // must use Class.getName(); that contains packages
        //        Constructor<?> ctor = myClass.getConstructor();
        //        return (TrainPart) ctor.newInstance();

        if (CargoLocomotive.class.getSimpleName().equals(className))
            return new CargoLocomotive();
        else if (ManeuverLocomotive.class.getSimpleName().equals(className))
            return new ManeuverLocomotive();
        else if (PassengerLocomotive.class.getSimpleName().equals(className))
            return new PassengerLocomotive();
        else if (UniversalLocomotive.class.getSimpleName().equals(className))
            return new UniversalLocomotive();

        else if (CargoWagon.class.getSimpleName().equals(className))
            return new CargoWagon();
        else if (PassengerBadWagon.class.getSimpleName().equals(className))
            return new PassengerBadWagon();
        else if (PassengerRestoranWagon.class.getSimpleName().equals(className))
            return new PassengerRestoranWagon();
        else if (PassengerSeatWagon.class.getSimpleName().equals(className))
            return new PassengerSeatWagon();
        else if (PassengerSleepWagon.class.getSimpleName().equals(className))
            return new PassengerSleepWagon();
        else if (SpecialUseWagon.class.getSimpleName().equals(className))
            return new SpecialUseWagon();

        return null;
    }


}
