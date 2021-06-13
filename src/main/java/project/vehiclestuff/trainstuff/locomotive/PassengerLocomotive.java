package project.vehiclestuff.trainstuff.locomotive;

import project.vehiclestuff.trainstuff.trainpartinterface.IPassenger;

public class PassengerLocomotive extends Locomotive implements IPassenger
{
	public static final String NAME = "PL";

	public PassengerLocomotive() {
	}

	@Override
	public String getPartName()
	{
		return NAME;
	}
}