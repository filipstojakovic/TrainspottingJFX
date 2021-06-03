package project.streetstuff.streetvehicle;

import project.streetstuff.StreetRoad;

public class Truck extends StreetVehicle
{
    private static final String NAME = "TCK";

    public Truck()
    {
    }

    public Truck(StreetRoad streetRoad)
    {
        super(streetRoad);
    }

    @Override
    public String getVehicleName()
    {
        return NAME;
    }
}