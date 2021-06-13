package project.vehiclestuff.trainstuff;

import java.util.Objects;

public abstract class TrainPart implements IMoveable
{
    //ABSTRACT
    protected int currentX = -1;
    protected int currentY = -1;

    public void setPosition(int x, int y)
    {
        currentX = x;
        currentY = y;
    }

    public int getCurrentX()
    {
        return currentX;
    }

    public void setCurrentX(int currentX)
    {
        this.currentX = currentX;
    }

    public int getCurrentY()
    {
        return currentY;
    }

    public void setCurrentY(int currentY)
    {
        this.currentY = currentY;
    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TrainPart trainPart = (TrainPart) o;
        return currentX == trainPart.currentX && currentY == trainPart.currentY;
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(currentX, currentY);
    }
}
