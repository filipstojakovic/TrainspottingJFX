package project.vehiclestuff.trainstuff.wagon;

public class PassengerSleepWagon extends PassengerWagon
{
    public static final String NAME = "PSS";

    public PassengerSleepWagon()
    {
    }

    @Override
    public String getPartName()
    {
        return NAME;
    }
}