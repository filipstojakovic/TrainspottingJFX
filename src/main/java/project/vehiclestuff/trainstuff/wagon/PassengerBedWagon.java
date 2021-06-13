package project.vehiclestuff.trainstuff.wagon;

public class PassengerBedWagon extends PassengerWagon
{
    public static final String NAME = "PBW";
    public Integer passengerSpace;

    public PassengerBedWagon()
    {
    }

    @Override
    public String getPartName()
    {
        return NAME;
    }

}