package project.trainstuff.locomotive;

import project.trainstuff.trainpartinterface.IPassenger;

public class PassengerLocomotive extends Locomotive implements IPassenger
{
	private final String NAME = "PL";

	public PassengerLocomotive() {
	}

	@Override
	public String getPartName()
	{
		return NAME;
	}
}