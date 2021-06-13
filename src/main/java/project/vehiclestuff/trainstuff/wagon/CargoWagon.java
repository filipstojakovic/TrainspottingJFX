package project.vehiclestuff.trainstuff.wagon;

import project.vehiclestuff.trainstuff.trainpartinterface.ICargo;

public class CargoWagon extends Wagon implements ICargo
{
    public static final String NAME = "CW";

    public CargoWagon()
    {
    }

    @Override
    public String getPartName()
    {
        return NAME;
    }
}