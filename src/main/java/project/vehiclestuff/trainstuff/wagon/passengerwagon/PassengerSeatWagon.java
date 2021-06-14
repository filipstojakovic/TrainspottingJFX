package project.vehiclestuff.trainstuff.wagon.passengerwagon;

import project.vehiclestuff.trainstuff.wagon.PassengerWagon;

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

    public Integer getPassengerSpace()
    {
        return passengerSpace;
    }

    public void setPassengerSpace(Integer passengerSpace)
    {
        this.passengerSpace = passengerSpace;
    }
}