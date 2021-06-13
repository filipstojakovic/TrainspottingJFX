package project.vehiclestuff.streetstuff.streetvehicle;

import project.vehiclestuff.streetstuff.StreetRoad;

public class Car extends StreetVehicle
{
    public static final String NAME = "CAR";

    private int numOfDoors;

    public Car(StreetRoad streetRoad, int speed)
    {
        super(streetRoad, speed);
    }

    @Override
    public String getStreetVehicleName()
    {
        return NAME;
    }

    public int getNumOfDoors()
    {
        return numOfDoors;
    }

    public void setNumOfDoors(int numOfDoors)
    {
        this.numOfDoors = numOfDoors;
    }
}