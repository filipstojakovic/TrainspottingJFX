package project.vehiclestuff.trainstuff.locomotive;

import project.vehiclestuff.trainstuff.trainpartinterface.IManeuver;

public class ManeuverLocomotive extends Locomotive implements IManeuver
{
	public static final String NAME = "ML";

	public ManeuverLocomotive() {
	}

	@Override
	public String getPartName()
	{
		return NAME;
	}
}