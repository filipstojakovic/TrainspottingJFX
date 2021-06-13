package project.vehiclestuff.trainstuff.locomotive;

import project.vehiclestuff.trainstuff.trainpartinterface.IUniversal;

public class UniversalLocomotive extends Locomotive implements IUniversal
{
	public static final String NAME = "UL";

	public UniversalLocomotive() {
	}

	@Override
	public String getPartName()
	{
		return NAME;
	}
}