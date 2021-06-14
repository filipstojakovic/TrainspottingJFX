package project.vehiclestuff.trainstuff.wagon.passengerwagon;

import project.vehiclestuff.trainstuff.wagon.PassengerWagon;

public class PassengerRestaurantWagon extends PassengerWagon
{
    public static final String NAME = "PRW";

    public String description;

    public PassengerRestaurantWagon()
    {
    }

    @Override
    public String getPartName()
    {
        return NAME;
    }

    public String getDescription()
    {
        return description;
    }

    public void setDescription(String description)
    {
        this.description = description;
    }
}