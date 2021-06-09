package project.vehiclestuff.trainstuff.locomotive;

import project.vehiclestuff.trainstuff.trainpartinterface.ICargo;

public class CargoLocomotive extends Locomotive implements ICargo
{
    private final String NAME = "CL";

    public CargoLocomotive()
    {
    }

    @Override
    public String getPartName()
    {
        return NAME;
    }
}