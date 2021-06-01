package project.map.Field;

public class RampField extends Field
{
    private Boolean isClosed;

    public RampField(int xPosition, int yPosition)
    {
        super(xPosition, yPosition);
    }

    public Boolean getClosed()
    {
        return isClosed;
    }

    public void setClosed(Boolean closed)
    {
        isClosed = closed;
    }
}