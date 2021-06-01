package project.trainstuff.wagon;

import project.trainstuff.trainpartinterface.IManeuver;
import project.trainstuff.trainpartinterface.IUniversal;

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