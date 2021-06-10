package project.vehiclestuff.trainstuff.wagon;

public class PassengerRestaurantWagon extends PassengerWagon
{
    private final String NAME = "PRW";

    public String description;

    public PassengerRestaurantWagon()
    {
    }

    @Override
    public String getPartName()
    {
        return NAME;
    }
}