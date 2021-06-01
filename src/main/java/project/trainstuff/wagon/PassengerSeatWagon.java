package project.trainstuff.wagon;

public class PassengerSeatWagon extends PassengerWagon
{
    private final String NAME = "PSW";
    public Integer passengerSpace;

    public PassengerSeatWagon()
    {
    }

    @Override
    public String getPartName()
    {
        return NAME;
    }
}