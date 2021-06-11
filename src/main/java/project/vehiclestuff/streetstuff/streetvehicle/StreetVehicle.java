package project.vehiclestuff.streetstuff.streetvehicle;

import javafx.application.Platform;
import project.map.Field.Field;
import project.map.Field.RampField;
import project.map.MapController;
import project.vehiclestuff.streetstuff.StreetRoad;

public abstract class StreetVehicle extends Thread
{
    protected StreetRoad streetRoad;

    private final int SPEED;
    private String mark;
    private String model;
    private int year;

    public StreetVehicle(StreetRoad streetRoad, int speed)
    {
        setDaemon(true);
        this.streetRoad = streetRoad;
        SPEED = speed;
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
                firstX = field.getxPosition();
                firstY = field.getyPosition();

                var label = MapController.getGridCell(firstX, firstY);
                if (label == null)
                    continue;

                String cellText = label.getText();
                if (!"".equals(cellText))
                {
                    drawVehicle(secondX, secondY);
                    while (!"".equals(MapController.getGridCell(firstX, firstY).getText()))
                        Thread.sleep(SPEED);
                    removeVehicle(secondX, secondY);
                }


                if (field instanceof RampField rampField)
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
        Platform.runLater(() ->
        {
            var label = MapController.getGridCell(finalX, finalY);
            if (label != null)
                label.setText(getStreetVehicleName());
        });
    }

    private void removeVehicle(final int finalX, final int finalY)
    {
        Platform.runLater(() ->
        {
            var label = MapController.getGridCell(finalX, finalY);
            if (label != null)
                label.setText("");
        });
    }
}