package project.trainstuff.wagon;

public class PassengerSleepWagon extends PassengerWagon
{
    private final String NAME = "PSLW";

    public PassengerSleepWagon()
    {
    }

    @Override
    public String getPartName()
    {
        return NAME;
    }
}