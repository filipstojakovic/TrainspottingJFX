package project.vehiclestuff.streetstuff.streetvehicle;

import project.vehiclestuff.streetstuff.StreetRoad;

public class Truck extends StreetVehicle
{
    private static final String NAME = "TCK";
    private double weightLimit;

    public Truck(StreetRoad streetRoad, int speed)
    {
        super(streetRoad, speed);
    }

    @Override
    public String getStreetVehicleName()
    {
        return NAME;
    }
}