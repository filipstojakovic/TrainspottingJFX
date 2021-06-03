package project.streetstuff.streetvehicle;

import project.streetstuff.StreetRoad;

public class Car extends StreetVehicle
{
    private static final String NAME = "CAR";
    public Car()
    {
    }

    public Car(StreetRoad streetRoad)
    {
        super(streetRoad);
    }

    @Override
    public String getVehicleName()
    {
        return NAME;
    }
}