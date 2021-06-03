package project.map.Field;

public class RampField extends Field
{
    private boolean isClosed;

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