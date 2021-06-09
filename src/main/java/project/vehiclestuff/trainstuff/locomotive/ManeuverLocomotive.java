package project.vehiclestuff.trainstuff.locomotive;

import project.vehiclestuff.trainstuff.trainpartinterface.IManeuver;

public class ManeuverLocomotive extends Locomotive implements IManeuver
{
	private final String NAME = "ML";

	public ManeuverLocomotive() {
	}

	@Override
	public String getPartName()
	{
		return NAME;
	}
}