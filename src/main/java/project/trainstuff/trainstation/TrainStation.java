package project.trainstuff.trainstation;

import project.map.Field.Field;
import project.trainstuff.RailRoad;
import project.trainstuff.Train;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

public class TrainStation
{
    private String stationName;
    private List<RailRoad> trainRailRoads;
    private List<Train> parkedTrains;

    private HashMap<String, Field> destinationFields;

    public TrainStation()
    {
        parkedTrains = new ArrayList<>();
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

    public List<Train> getParkedTrains()
    {
        return parkedTrains;
    }

    public void setParkedTrains(List<Train> parkedTrains)
    {
        this.parkedTrains = parkedTrains;
    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TrainStation that = (TrainStation) o;
        return stationName.equals(that.stationName) && Objects.equals(trainRailRoads, that.trainRailRoads) && Objects.equals(parkedTrains, that.parkedTrains) && Objects.equals(destinationFields, that.destinationFields);
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(stationName, trainRailRoads, parkedTrains, destinationFields);
    }
}
