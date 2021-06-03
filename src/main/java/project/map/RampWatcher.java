package project.map;

import project.map.Field.RampField;
import project.trainstuff.RailRoad;

import java.util.List;

public class RampWatcher extends Thread
{
    private final List<RailRoad> railRoads;

    public RampWatcher(List<RailRoad> railRoads)
    {
        this.railRoads = railRoads;
    }

    @Override
    public void run()
    {
        while(true)
        {
            for (RailRoad railRoad : railRoads)
            {
                RailRoad opositeRoad = railRoads.stream().filter(x -> railRoad.getStartStationName().equals(x.getEndStationName())).findFirst().get();

                if (shouldCloseRampOnRailRoad(railRoad) || shouldCloseRampOnRailRoad(opositeRoad))
                {
                    railRoad.getRamps().forEach(x -> x.setClosed(true));

                } else
                {
                    railRoad.getRamps().forEach(x -> x.setClosed(false));
                }
            }

            try
            {
                Thread.sleep(50);
            } catch (InterruptedException e)
            {
                e.printStackTrace();
            }
        }

    }

    private boolean shouldCloseRampOnRailRoad(RailRoad trainRoad)
    {
        if(!trainRoad.isOccupied())
            return false;
        var railFields = trainRoad.getRoadFields();
        int i = 2;
        boolean shouldClose = false;
        for (var field : railFields)
        {
            if (field instanceof RampField)
            {
                i--;
            }
            String cellText = MapController.getGridCell(field.getxPosition(), field.getyPosition()).getText();
            if (!"".equals(cellText))
            {
                shouldClose = true;
                break;
            }
            if (i == 0)
                break;
        }

        return shouldClose;
    }
}
