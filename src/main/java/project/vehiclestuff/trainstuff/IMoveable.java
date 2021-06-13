package project.vehiclestuff.trainstuff;

public interface IMoveable
{
    void setPosition(int x, int y);

    String getPartName();

    int getCurrentX();

    void setCurrentX(int currentX);

    int getCurrentY();

    void setCurrentY(int currentY);
}
