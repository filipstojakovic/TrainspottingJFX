package project.vehiclestuff.trainstuff.wagon;

public class PassengerRestoranWagon extends PassengerWagon
{
    private final String NAME = "PRW";

    public String description;

    public PassengerRestoranWagon()
    {
    }

    @Override
    public String getPartName()
    {
        return NAME;
    }
}