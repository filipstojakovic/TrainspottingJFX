package project.vehiclestuff.trainstuff.trainstation;

import project.map.Field.Field;
import project.vehiclestuff.trainstuff.RailRoad;
import project.vehiclestuff.trainstuff.Train;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

public class TrainStation
{
    private String stationName;
    private List<RailRoad> trainRailRoads;

    private HashMap<String,Train> parkedTrains;
    private HashMap<String, Field> destinationFields;

    public TrainStation()
    {
        parkedTrains = new HashMap<>();
        destinationFields = new HashMap<>();
    }

    public Field getStartingFieldForDestination(String stationName)
    {
        return destinationFields.get(stationName);
    }

    public void addDestinationField(String destinationStationName, Field destinationField)
    {
        if (destinationField == null)
            destinationFields = new HashMap<>();
        destinationFields.put(destinationStationName, destinationField);
    }

    public void addTrainLine(RailRoad trainLine)
    {
        if (trainRailRoads == null)
            trainRailRoads = new ArrayList<>();
        trainRailRoads.add(trainLine);
    }

    public void addParkedTrain(Train train)
    {
        parkedTrains.put(train.getTrainName(),train);
    }

    public void removeParkedTrain(Train train)
    {
        parkedTrains.remove(train.getTrainName());
    }

    public String getStationName()
    {
        return stationName;
    }

    public void setStationName(String stationName)
    {
        this.stationName = stationName;
    }

    public List<RailRoad> getTrainRailRoads()
    {
        return trainRailRoads;
    }

    public void setTrainRailRoads(List<RailRoad> trainRailRoads)
    {
        this.trainRailRoads = trainRailRoads;
    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TrainStation that = (TrainStation) o;
        return stationName.equals(that.stationName) && trainRailRoads.equals(that.trainRailRoads) && parkedTrains.equals(that.parkedTrains) && destinationFields.equals(that.destinationFields);
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(stationName, trainRailRoads, parkedTrains, destinationFields);
    }
}
