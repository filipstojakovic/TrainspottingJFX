package project.map.Field;

public class RailField extends Field
{
    private boolean hasElectricity;

    public RailField(int xPosition, int yPosition)
    {
        super(xPosition, yPosition);
    }

    public boolean isHasElectricity()
    {
        return hasElectricity;
    }

    public void setHasElectricity(boolean hasElectricity)
    {
        this.hasElectricity = hasElectricity;
    }
}