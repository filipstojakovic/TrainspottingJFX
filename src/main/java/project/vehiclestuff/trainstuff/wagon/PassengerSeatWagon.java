package project.vehiclestuff.trainstuff.wagon;

public class PassengerSeatWagon extends PassengerWagon
{
    public static final String NAME = "PSW";
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