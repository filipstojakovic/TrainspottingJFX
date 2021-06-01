package project.trainstuff.locomotive;

import project.trainstuff.trainpartinterface.IManeuver;

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