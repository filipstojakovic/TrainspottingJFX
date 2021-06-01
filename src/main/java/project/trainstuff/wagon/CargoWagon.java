package project.trainstuff.wagon;

import project.trainstuff.trainpartinterface.ICargo;

public class CargoWagon extends Wagon implements ICargo
{
    private final String NAME = "CW";

    public CargoWagon()
    {
    }

    @Override
    public String getPartName()
    {
        return NAME;
    }
}