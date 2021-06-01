package project.trainstuff.locomotive;

import project.trainstuff.trainpartinterface.IUniversal;

public class UniversalLocomotive extends Locomotive implements IUniversal
{
	private final String NAME = "UL";

	public UniversalLocomotive() {
	}

	@Override
	public String getPartName()
	{
		return NAME;
	}
}