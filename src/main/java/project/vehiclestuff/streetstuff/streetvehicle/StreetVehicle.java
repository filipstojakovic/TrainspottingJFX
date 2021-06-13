package project.vehiclestuff.streetstuff.streetvehicle;

import javafx.application.Platform;
import project.Util.GenericLogger;
import project.Util.Utils;
import project.map.Field.Field;
import project.map.Field.RampField;
import project.map.MapController;
import project.vehiclestuff.streetstuff.StreetRoad;

public abstract class StreetVehicle extends Thread
{
    public static final int SLOWEST_SPEED = 2000;
    protected final StreetRoad streetRoad;

    private final int SPEED;
    private String mark;
    private String model;
    private int year;

    public StreetVehicle(StreetRoad streetRoad, int speed)
    {
        this.streetRoad = streetRoad;
        SPEED = Utils.getRandomNumBetween(speed, SLOWEST_SPEED);
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
                    synchronized (rampField.RAMP_LOCK)
                    {
                        if (rampField.isClosed())
                        {
                            drawVehicle(secondX, secondY);
                            while (rampField.isClosed())
                            {
                                rampField.RAMP_LOCK.wait();
                            }
                            removeVehicle(secondX, secondY);
                        }
                    }
                }

                drawVehicle(firstX, firstY);
                Thread.sleep(SPEED);
                removeVehicle(firstX, firstY);
                secondX = firstX;
                secondY = firstY;
            }
        } catch (InterruptedException ex)
        {
            GenericLogger.asyncLog(this.getClass(), ex);
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