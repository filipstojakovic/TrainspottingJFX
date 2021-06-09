package project.vehiclestuff.trainstuff.wagon;

public class PassengerBadWagon extends PassengerWagon
{
    private final String NAME = "PBW";
    public Integer passengerSpace;

    public PassengerBadWagon()
    {
    }

    @Override
    public String getPartName()
    {
        return NAME;
    }

}