package project.map;

import project.map.Field.RampField;
import project.vehiclestuff.trainstuff.RailRoad;

import java.util.List;

public class RampWatcher extends Thread
{
    private final List<RailRoad> railRoads;

    private boolean isActive;

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
                        .filter(x -> railRoad.getStartStationName().equals(x.getEndStationName()))
                        .findFirst().get();

                boolean shouldClose = shouldCloseRampOnRailRoad(railRoad) || shouldCloseRampOnRailRoad(opositeRoad);
                railRoad.getRamps().forEach(x -> x.setClosed(shouldClose));
            }
        }
    }

    public static final int MINOR_DELAY = 1;

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
