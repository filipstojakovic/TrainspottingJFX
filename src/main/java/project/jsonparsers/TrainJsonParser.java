package project.jsonparsers;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;
import project.vehiclestuff.trainstuff.Train;
import project.vehiclestuff.trainstuff.TrainPart;
import project.vehiclestuff.trainstuff.locomotive.CargoLocomotive;
import project.vehiclestuff.trainstuff.locomotive.ManeuverLocomotive;
import project.vehiclestuff.trainstuff.locomotive.PassengerLocomotive;
import project.vehiclestuff.trainstuff.locomotive.UniversalLocomotive;
import project.vehiclestuff.trainstuff.trainstation.TrainStation;
import project.vehiclestuff.trainstuff.wagon.*;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class TrainJsonParser extends JsonParser
{
    public static final String TRAIN_SPEED = "train_speed";
    public static final String TRAIN_PARTS = "train_parts";
    public static final String TYPE = "type";
    public static final String STATION_ORDER = "stationOrder";

    public static final int MIN_TRAIN_SPEED = 500;

    public static Train getTrainPartsFromJson(final HashMap<String, TrainStation> trainstationHashMap, String trainPath)
    {
        Train train;
        try
        {
            File file = new File(trainPath);
            JSONObject obj = (JSONObject) getJsonObjectFromFile(file);
            train = new Train(file.getName());

            int trainSpeed = ((Long) obj.get(TRAIN_SPEED)).intValue();
            //if (trainSpeed < MIN_TRAIN_SPEED)
            //    throw new TrainNotValidException("Train speed is set to " + trainSpeed + " (minimal speed is " + MIN_TRAIN_SPEED + ")");
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

        } catch (ParseException | IOException e)
        {
            train = null;
            e.printStackTrace();
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
