package project.vehiclestuff.trainstuff;

import project.map.Field.Field;
import project.map.Field.RampField;

import java.util.*;

public class RailRoad
{
    private String startStationName;
    private String endStationName;
    private List<Field> roadFields;
    private List<RampField> ramps;

    //private HashMap<String, Train> trainsOnRailRoad;
    private List<Train> trainsOnRailRoad;

    public RailRoad()
    {
    }

    public RailRoad(String startStationName, String endStationName, List<Field> roadFields, List<RampField> ramps)
    {
        this.startStationName = startStationName;
        this.endStationName = endStationName;
        this.roadFields = roadFields;
        this.ramps = ramps;
        trainsOnRailRoad = new ArrayList<>();
    }

    public synchronized void addTrainOnRoad(Train train)
    {
        trainsOnRailRoad.add(train);
    }

    public synchronized void removeTrainFromRailRoad(Train train)
    {
        trainsOnRailRoad.remove(train);
    }

    public synchronized Train getLastTrainOnRoad()
    {
        return !trainsOnRailRoad.isEmpty() ? trainsOnRailRoad.get(trainsOnRailRoad.size() - 1) : null;
    }

    //create reverse copy
    public RailRoad createReverseLine()
    {
        String start = endStationName;
        String end = startStationName;

        List<Field> reverseLine = new ArrayList<>(roadFields); //deep copy
        Collections.reverse(reverseLine);

        List<RampField> reverseRamps = new ArrayList<>(ramps); //deep copy
        Collections.reverse(reverseRamps);

        return new RailRoad(start, end, reverseLine, reverseRamps);
    }

    public synchronized boolean isRailRoadEmpty()
    {
        return trainsOnRailRoad.isEmpty();
    }

    public Field getStartingField()
    {
        return roadFields.get(0);
    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RailRoad trainLine = (RailRoad) o;
        return startStationName.equals(trainLine.startStationName)
                && endStationName.equals(trainLine.endStationName)
                && roadFields.equals(trainLine.roadFields)
                && Objects.equals(ramps, trainLine.ramps);
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(startStationName, endStationName, roadFields, ramps);
    }

    public String getStartStationName()
    {
        return startStationName;
    }

    public void setStartStationName(String startStationName)
    {
        this.startStationName = startStationName;
    }

    public String getEndStationName()
    {
        return endStationName;
    }

    public void setEndStationName(String endStationName)
    {
        this.endStationName = endStationName;
    }

    public List<RampField> getRamps()
    {
        return ramps;
    }

    public void setRamps(List<RampField> ramps)
    {
        this.ramps = ramps;
    }

    public List<Field> getRoadFields()
    {
        return roadFields;
    }

    public void setRoadFields(List<Field> roadFields)
    {
        this.roadFields = roadFields;
    }
}
