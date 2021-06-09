package project.vehiclestuff.streetstuff.streetvehicle;

import javafx.application.Platform;
import project.map.Field.Field;
import project.map.Field.RampField;
import project.map.MapController;
import project.vehiclestuff.IMoveable;
import project.vehiclestuff.streetstuff.StreetRoad;

public abstract class StreetVehicle extends Thread implements IMoveable
{
    protected StreetRoad streetRoad;

    private final int SPEED; // TODO: make it final
    private String mark;
    private String model;
    private int year;

    //public StreetVehicle()
    //{
    //    setDaemon(true);
    //}


    @Override
    public void writeCurrentPositionToFile(int x, int y, String description, String filePath)
    {
        String vehicleName = getStreetVehicleName();

    }

    public StreetVehicle(StreetRoad streetRoad)
    {
        setDaemon(true);
        this.streetRoad = streetRoad;
        SPEED = streetRoad.getSpeed();
    }

    public abstract String getStreetVehicleName();


    @Override
    public void run()
    {
        var streetRoadFields = streetRoad.getStreetFields();
        int firstX, firstY;
        int secondX = -1, secondY = -1;
        try
        {
            for (Field field : streetRoadFields)
            {
                //todo: check if car (slower car) is in front or if there is guzva
                firstX = field.getxPosition();
                firstY = field.getyPosition();

                String cellText = MapController.getGridCell(firstX, firstY).getText();

                if (!"".equals(cellText))
                {
                    drawVehicle(secondX, secondY);
                    while (!"".equals(MapController.getGridCell(firstX, firstY).getText()))
                        Thread.sleep(SPEED);
                    removeVehicle(secondX, secondY);
                }


                if (field instanceof RampField rampField)// && rampField.isClosed())
                {
                    if (rampField.isClosed())
                    {
                        drawVehicle(secondX, secondY);
                        while (rampField.isClosed())
                        {
                            Thread.sleep(SPEED);
                        }
                        removeVehicle(secondX, secondY);
                    }
                }

                drawVehicle(firstX, firstY);
                Thread.sleep(SPEED);
                removeVehicle(firstX, firstY);
                secondX = firstX;
                secondY = firstY;
            }
        } catch (InterruptedException e)
        {
            e.printStackTrace();
        }
    }


    private void drawVehicle(final int finalX, final int finalY)
    {
        Platform.runLater(() -> MapController.getGridCell(finalX, finalY).setText(getStreetVehicleName()));
    }

    private void removeVehicle(final int finalX, final int finalY)
    {
        Platform.runLater(() -> MapController.getGridCell(finalX, finalY).setText(""));
    }
}