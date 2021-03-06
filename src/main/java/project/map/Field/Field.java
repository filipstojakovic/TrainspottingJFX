package project.map.Field;

import project.vehiclestuff.trainstuff.IMoveable;

import java.util.Objects;

public abstract class Field
{
    protected final int xPosition;
    protected final int yPosition;

    protected IMoveable moveableObject;

    public Field(int xPosition, int yPosition)
    {
        this.xPosition = xPosition;
        this.yPosition = yPosition;
    }

    public int getxPosition()
    {
        return xPosition;
    }

    public int getyPosition()
    {
        return yPosition;
    }

    public IMoveable getMoveableObject()
    {
        return moveableObject;
    }

    public void setMoveableObject(IMoveable moveableObject)
    {
        this.moveableObject = moveableObject;
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