package project.vehiclestuff.trainstuff.wagon;

import project.vehiclestuff.trainstuff.trainpartinterface.IManeuver;
import project.vehiclestuff.trainstuff.trainpartinterface.IUniversal;

public class SpecialUseWagon extends Wagon implements IUniversal, IManeuver
{
    private final String NAME = "SUW";

    public SpecialUseWagon()
    {
    }

    @Override
    public String getPartName()
    {
        return NAME;
    }
}