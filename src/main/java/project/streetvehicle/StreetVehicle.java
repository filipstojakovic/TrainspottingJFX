package project.streetvehicle;

public abstract class StreetVehicle extends Thread implements IMoveable
{
    protected int xPosition;
    protected int yPosition;

    public StreetVehicle()
    {
        setDaemon(true);
    }

    protected abstract void setColor();

    @Override
    public void run()
    {


       /*
       while(next field is on map)
       {
           while((next field is ramp && ramp is closed) || nextField has Vehicle)
                sleep(500)
            move();
       }
        */
    }
}