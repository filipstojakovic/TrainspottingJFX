package project.vehiclestuff.streetstuff;

import java.util.ArrayList;
import java.util.List;

public class Street
{
    private String streetName;
    private int speedLimit;

    private int maxNumOfVehicles;
    private List<StreetRoad> roads;

    public Street()
    {
        roads = new ArrayList<>();
    }


    public Street(String streetName, StreetRoad roads)
    {
        this();
        addStreetRoad(roads);
        this.streetName = streetName;
    }

    public void addStreetRoad(StreetRoad streetRoad)
    {
        roads.add(streetRoad);
    }

    public String getStreetName()
    {
        return streetName;
    }

    public void setStreetName(String streetName)
    {
        this.streetName = streetName;
    }

    public synchronized int getSpeedLimit()
    {
        return speedLimit;
    }

    public synchronized void setSpeedLimit(int speedLimit)
    {
        this.speedLimit = speedLimit;
    }

    public List<StreetRoad> getRoads()
    {
        return roads;
    }

    public void setRoads(List<StreetRoad> roads)
    {
        this.roads = roads;
    }

    public synchronized int getMaxNumOfVehicles()
    {
        return maxNumOfVehicles;
    }

    public synchronized void setMaxNumOfVehicles(int maxNumOfVehicles)
    {
        this.maxNumOfVehicles = maxNumOfVehicles;
    }
}
