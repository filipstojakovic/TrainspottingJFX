package project.trainstuff;

public abstract class TrainPart
{
    //ABSTRACT
    protected int currentX = -1;
    protected int currentY = -1;

    public void setPosition(int x, int y)
    {
        currentX = x;
        currentY = y;
    }

    public abstract String getPartName();

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
}
