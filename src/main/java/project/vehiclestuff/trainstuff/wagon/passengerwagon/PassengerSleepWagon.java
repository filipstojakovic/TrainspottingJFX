package project.vehiclestuff.trainstuff.wagon.passengerwagon;

import project.vehiclestuff.trainstuff.wagon.PassengerWagon;

public class PassengerSleepWagon extends PassengerWagon
{
    public static final String NAME = "PSS";
    public Integer passengerSpace;

    public PassengerSleepWagon()
    {
    }

    @Override
    public String getPartName()
    {
        return NAME;
    }

    public Integer getPassengerSpace()
    {
        return passengerSpace;
    }

    public void setPassengerSpace(Integer passengerSpace)
    {
        this.passengerSpace = passengerSpace;
    }
}