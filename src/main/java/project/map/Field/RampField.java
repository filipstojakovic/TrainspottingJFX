package project.map.Field;

public class RampField extends Field
{
    public final Object RAMP_LOCK = new Object();
    private boolean isClosed;

    public boolean isHasElectricity()
    {
        return hasElectricity;
    }

    public void setHasElectricity(boolean hasElectricity)
    {
        this.hasElectricity = hasElectricity;
    }

    private boolean hasElectricity;

    public RampField(int xPosition, int yPosition)
    {
        super(xPosition, yPosition);
    }

    public synchronized boolean isClosed()
    {
        return isClosed;
    }

    public synchronized void setClosed(boolean closed)
    {
        isClosed = closed;
    }
}