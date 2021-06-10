package project.vehiclestuff.trainstuff.wagon;

public class PassengerSleepWagon extends PassengerWagon
{
    private final String NAME = "PSS";

    public PassengerSleepWagon()
    {
    }

    @Override
    public String getPartName()
    {
        return NAME;
    }
}