package project.map;

import javafx.application.Platform;
import project.Util.GenericLogger;
import project.Util.LabelUtils;
import project.constants.ColorConstants;
import project.map.Field.RampField;
import project.vehiclestuff.streetstuff.streetvehicle.Car;
import project.vehiclestuff.streetstuff.streetvehicle.Truck;
import project.vehiclestuff.trainstuff.RailRoad;

import java.util.List;
import java.util.NoSuchElementException;

public class RampWatcher extends Thread
{
    private final List<RailRoad> railRoads;

    private boolean isActive;

    public static final Object RAMP_LOCK = new Object();

    public RampWatcher(List<RailRoad> railRoads)
    {
        isActive = true;
        this.railRoads = railRoads;
    }

    @Override
    public void run()
    {
        while (isActive)
        {
            for (RailRoad railRoad : railRoads)
            {
                RailRoad opositeRoad = null;
                try
                {
                    opositeRoad = railRoads.stream()
                            .filter(x ->
                            {
                                String startName = railRoad.getStartStationName();
                                String endName = railRoad.getEndStationName();
                                return startName.equals(x.getEndStationName()) && endName.equals(x.getStartStationName());
                            })
                            .findFirst().orElseThrow();
                } catch (NoSuchElementException ex)
                {
                    GenericLogger.createAsyncLog(this.getClass(), ex);
                    continue;
                }

                boolean shouldClose;
                shouldClose = shouldCloseRampOnRailRoad(railRoad) || shouldCloseRampOnRailRoad(opositeRoad);
                railRoad.getRamps().forEach(ramp ->
                {
                    synchronized (ramp.RAMP_LOCK)
                    {
                        final boolean close = shouldClose;

                        ramp.setClosed(shouldClose);
                        Platform.runLater(() ->
                        {
                            var lbl = MapController.getGridCell(ramp.getxPosition(), ramp.getyPosition());
                            if (lbl != null)
                                LabelUtils.setLableBackgroundAndBorderColor(lbl, close ? ColorConstants.RED : ColorConstants.BLACK);
                        });
                        ramp.RAMP_LOCK.notify();
                    }
                });
            }
            try
            {
                Thread.sleep(MINOR_DELAY);
            } catch (InterruptedException ex)
            {
                GenericLogger.createAsyncLog(this.getClass(), ex);
            }

        }
    }

    public static final int MINOR_DELAY = 100;

    private boolean shouldCloseRampOnRailRoad(RailRoad trainRoad)
    {
        if (trainRoad.isRailRoadEmpty())
            return false;

        var railFields = trainRoad.getRoadFields();
        boolean shouldClose = false;

        int numOfRamps = trainRoad.getRamps().size();

        for (int i = 0; i < railFields.size() && numOfRamps > 0; i++)
        {
            var field = railFields.get(i);
            if (field instanceof RampField)
                numOfRamps--;

            String cellText = MapController.getGridCell(field.getxPosition(), field.getyPosition()).getText();
            if (!trainRoad.isRailRoadEmpty() && !"".equals(cellText) && !Car.NAME.equals(cellText) && !Truck.NAME.equals(cellText))
            {
                shouldClose = true;
                break;
            }
        }
        return shouldClose;
    }

    public void close()
    {
        isActive = false;
    }
}
