package project.jsonparsers;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import project.vehiclestuff.trainstuff.Train;
import project.vehiclestuff.trainstuff.TrainPart;
import project.vehiclestuff.trainstuff.locomotive.*;
import project.vehiclestuff.trainstuff.trainstation.TrainStation;
import project.vehiclestuff.trainstuff.wagon.*;

import java.util.*;

public class TrainJsonParser extends JsonParser
{
    public static final String TRAIN_SPEED = "train_speed";
    public static final String TRAIN_PARTS = "train_parts";
    public static final String TYPE = "type";
    public static final String STATION_ORDER = "stationOrder";

    public static Train getTrainPartsFromJson(final HashMap<String, TrainStation> trainstationHashMap, String trainPath)
    {
        Train train = new Train();
        try
        {
            JSONObject obj = (JSONObject) getJsonObjectFromFile(trainPath);
            int trainSpeed = ((Long) obj.get(TRAIN_SPEED)).intValue();
            train.setTrainSpeed(trainSpeed);
            List<TrainPart> trainPartList = new ArrayList<>();
            JSONArray jsonArray = (JSONArray) obj.get(TRAIN_PARTS);
            for (Object arrayObject : jsonArray)
            {
                JSONObject jsonObject = (JSONObject) arrayObject;
                TrainPart trainPart = getTrainPartByClassName((String) jsonObject.get(TYPE));
                //if(trainPart instanceof Locomotive)
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
            train = null;
        }

        return train;
    }

    private static Queue<TrainStation> getStationOrder(HashMap<String, TrainStation> trainstationHashMap, JSONObject obj)
    {
        Queue<TrainStation> destinationOrder = new LinkedList<>();
        JSONArray jsonArray = (JSONArray) obj.get(STATION_ORDER);
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
        else if (PassengerRestaurantWagon.class.getSimpleName().equals(className))
            return new PassengerRestaurantWagon();
        else if (PassengerSeatWagon.class.getSimpleName().equals(className))
            return new PassengerSeatWagon();
        else if (PassengerSleepWagon.class.getSimpleName().equals(className))
            return new PassengerSleepWagon();
        else if (SpecialUseWagon.class.getSimpleName().equals(className))
            return new SpecialUseWagon();

        return null;
    }


}
