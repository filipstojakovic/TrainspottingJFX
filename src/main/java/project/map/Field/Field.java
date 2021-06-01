package project.map.Field;

import project.streetvehicle.IMoveable;

import java.io.Serializable;
import java.util.Objects;

public abstract class Field implements Serializable
{
    protected final int xPosition;
    protected final int yPosition;
    protected boolean isOccupied;
    protected IMoveable objectOnField;

    public Field(int xPosition, int yPosition)
    {
        isOccupied = false;
        this.xPosition = xPosition;
        this.yPosition = yPosition;
    }

    public synchronized void addObjectOnField(IMoveable moveableObject)
    {
        isOccupied = true;
        objectOnField = moveableObject;
    }

    public synchronized void removeObjectFromField()
    {
        isOccupied = false;
        objectOnField = null;
    }

    public int getxPosition()
    {
        return xPosition;
    }

    public int getyPosition()
    {
        return yPosition;
    }

    public boolean isOccupied()
    {
        return isOccupied;
    }

    public void setOccupied(boolean occupied)
    {
        isOccupied = occupied;
    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Field field = (Field) o;
        return xPosition == field.xPosition && yPosition == field.yPosition;
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(xPosition, yPosition);
    }
}