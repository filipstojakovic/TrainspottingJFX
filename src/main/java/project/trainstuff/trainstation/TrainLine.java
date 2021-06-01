package project.trainstuff.trainstation;

import project.map.Field.Field;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class TrainLine
{
    private String startStationName;
    private String endStationName;
    private boolean isOccupied = false;
    private List<Field> lineRoute;
    private List<Field> ramps;

    public TrainLine()
    {
    }

    public TrainLine(String startStationName, String endStationName, List<Field> lineRoute, List<Field> ramps)
    {
        this.startStationName = startStationName;
        this.endStationName = endStationName;
        this.lineRoute = lineRoute;
        this.ramps = ramps;
    }

    //create reverse copy
    public TrainLine createReverseLine()
    {
        String start = endStationName;
        String end = startStationName;
        List<Field> reverseLine = new ArrayList<>(lineRoute); //deep copy
        Collections.reverse(reverseLine);
        return new TrainLine(start, end, ramps, reverseLine);
    }

    public Field getStartingField()
    {
        return lineRoute.get(0);
    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TrainLine trainLine = (TrainLine) o;
        return startStationName.equals(trainLine.startStationName) && endStationName.equals(trainLine.endStationName) && lineRoute.equals(trainLine.lineRoute) && Objects.equals(ramps, trainLine.ramps);
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(startStationName, endStationName, lineRoute, ramps);
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

    public boolean isOccupied()
    {
        return isOccupied;
    }

    public void setOccupied(boolean occupied)
    {
        isOccupied = occupied;
    }

    public List<Field> getRamps()
    {
        return ramps;
    }

    public void setRamps(List<Field> ramps)
    {
        this.ramps = ramps;
    }

    public List<Field> getLineRoute()
    {
        return lineRoute;
    }

    public void setLineRoute(List<Field> lineRoute)
    {
        this.lineRoute = lineRoute;
    }
}
