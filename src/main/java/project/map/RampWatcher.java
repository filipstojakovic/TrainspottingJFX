package project.map;

import javafx.application.Platform;
import project.Util.LabelUtils;
import project.constants.ColorConstants;
import project.map.Field.RampField;
import project.vehiclestuff.trainstuff.RailRoad;

import java.util.List;

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
                RailRoad opositeRoad = railRoads.stream()
                        .filter(x ->
                        {
                            String startName = railRoad.getStartStationName();
                            String endName = railRoad.getEndStationName();
                            return startName.equals(x.getEndStationName()) && endName.equals(x.getStartStationName());
                        })
                        .findFirst().get();

                boolean shouldClose;
                synchronized (railRoad)
                {
                    synchronized (opositeRoad)
                    {
                        shouldClose = shouldCloseRampOnRailRoad(railRoad) || shouldCloseRampOnRailRoad(opositeRoad);
                        railRoad.getRamps().forEach(ramp ->
                        {
                            synchronized (ramp.RAMP_LOCK)
                            {
                                final boolean close = shouldClose;
                                Platform.runLater(() ->
                                {
                                    var lbl = MapController.getGridCell(ramp.getxPosition(), ramp.getyPosition());
                                    LabelUtils.setLableBackgroundAndBorderColor(lbl, close ? ColorConstants.RED : ColorConstants.BLACK);
                                });

                                ramp.setClosed(shouldClose);
                                ramp.RAMP_LOCK.notify();
                            }

                        });
                    }
                }
                try
                {
                    Thread.sleep(10);
                } catch (Exception ex)
                {
                    ex.printStackTrace();
                }

            }
        }
    }

    public static final int MINOR_DELAY = 300;

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
            if (!trainRoad.isRailRoadEmpty() && !"".equals(cellText))
            {
                shouldClose = true;
                break;
            }
        }
        return shouldClose;
    }

    public void setActive(boolean active)
    {
        isActive = active;
    }
}
