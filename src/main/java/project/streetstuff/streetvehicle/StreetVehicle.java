package project.streetstuff.streetvehicle;

import javafx.application.Platform;
import project.map.Field.Field;
import project.map.Field.RampField;
import project.map.MapController;
import project.streetstuff.StreetRoad;

public abstract class StreetVehicle extends Thread implements IMoveable
{
    protected StreetRoad streetRoad;

    public StreetVehicle()
    {
        setDaemon(true);
    }

    public StreetVehicle(StreetRoad streetRoad)
    {
        this();
        this.streetRoad = streetRoad;
    }

    public abstract String getVehicleName();


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

                if(!"".equals(cellText))
                {
                    drawVehicle(secondX,secondY);
                    while (!"".equals(MapController.getGridCell(firstX, firstY).getText()))
                        Thread.sleep(500);
                    removeVehicle(secondX,secondY);
                }


                if (field instanceof RampField rampField)// && rampField.isClosed())
                {
                    if (rampField.isClosed())
                    {
                        drawVehicle(secondX, secondY);
                        while (rampField.isClosed())
                        {
                            Thread.sleep(500);
                        }
                        removeVehicle(secondX, secondY);
                    }
                }

                drawVehicle(firstX, firstY);
                Thread.sleep(530);
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
        Platform.runLater(() -> MapController.getGridCell(finalX, finalY).setText(getVehicleName()));
    }

    private void removeVehicle(final int finalX, final int finalY)
    {
        Platform.runLater(() -> MapController.getGridCell(finalX, finalY).setText(""));
    }
}