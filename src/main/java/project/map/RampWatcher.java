package project.map;

import project.map.Field.RampField;
import project.vehiclestuff.trainstuff.RailRoad;

import java.util.List;

public class RampWatcher extends Thread
{
    public static final int MINOR_DELAY = 10;
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
                try
                {
                    RailRoad opositeRoad = railRoads.stream().filter(x -> railRoad.getStartStationName().equals(x.getEndStationName())).findFirst().get();

                    if (shouldCloseRampOnRailRoad(railRoad) || shouldCloseRampOnRailRoad(opositeRoad))
                    {
                        railRoad.getRamps().forEach(x ->
                        {
                            //TODO: delete me
                            //Label rampLable = MapController.getGridCell(x.getxPosition(),x.getyPosition());
                            //Platform.runLater(()->LabelUtils.setLableBackgroundAndBorderColor(rampLable, ColorConstants.RED));
                            x.setClosed(true);
                        });

                    } else
                    {
                        railRoad.getRamps().forEach(x ->
                        {
                            //Label rampLable = MapController.getGridCell(x.getxPosition(),x.getyPosition());
                            //Platform.runLater(()->LabelUtils.setLableBackgroundAndBorderColor(rampLable, ColorConstants.BLACK));
                            x.setClosed(false);
                        });
                    }

                    Thread.sleep(MINOR_DELAY);
                } catch (InterruptedException e)
                {
                    e.printStackTrace();
                }
            }
        }
    }

    private boolean shouldCloseRampOnRailRoad(RailRoad trainRoad)
    {
        if (trainRoad.isRailRoadEmpty())
            return false;

        var railFields = trainRoad.getRoadFields();
        int numOfRamps = 2;
        boolean shouldClose = false;
        for (var field : railFields)
        {
            if (field instanceof RampField)
                numOfRamps--;

            String cellText = MapController.getGridCell(field.getxPosition(), field.getyPosition()).getText();
            if (!trainRoad.isRailRoadEmpty() && !"".equals(cellText))
            {
                shouldClose = true;
                break;
            }

            if (numOfRamps == 0)
                break;
        }

        return shouldClose;
    }

    public void setActive(boolean active)
    {
        isActive = active;
    }
}
