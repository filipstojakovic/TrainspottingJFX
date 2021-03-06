package project.vehiclestuff.trainstuff.trainstation;

import javafx.application.Platform;
import project.Util.LabelUtils;
import project.constants.ColorConstants;
import project.map.Field.Field;
import project.map.MapController;
import project.vehiclestuff.trainstuff.RailRoad;
import project.vehiclestuff.trainstuff.Train;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

public class TrainStation
{
    private String stationName;
    private List<RailRoad> trainRailRoads;

    private HashMap<String, Train> parkedTrains;
    private HashMap<String, Field> destinationFields;

    public TrainStation()
    {
        parkedTrains = new HashMap<>();
        destinationFields = new HashMap<>();
    }

    public Field getStartingFieldForDestination(String stationName)
    {
        return destinationFields.get(stationName);
    }

    public void addDestinationField(String destinationStationName, Field destinationField)
    {
        if (destinationField == null)
            destinationFields = new HashMap<>();
        destinationFields.put(destinationStationName, destinationField);
    }

    public void addTrainLine(RailRoad trainLine)
    {
        if (trainRailRoads == null)
            trainRailRoads = new ArrayList<>();
        trainRailRoads.add(trainLine);
    }

    public static RailRoad getRailRoadBetweenStations(final TrainStation firstStation, final TrainStation secondStation)
    {
        return firstStation.getTrainRailRoads().stream()
                .filter(trainLine1 -> firstStation.getStationName().equals(trainLine1.getStartStationName())
                        && secondStation.getStationName().equals(trainLine1.getEndStationName()))
                .findFirst()
                .orElse(null);
    }

    public void addTrainOnRailRoad(Train train, RailRoad railRoad)
    {
        railRoad.addTrainOnRoad(train);
        setRampStatus(railRoad, true);
    }

    public void removeTrainOffRailRoad(Train train, RailRoad railRoad)
    {
        if (railRoad != null)
            railRoad.removeTrainFromRailRoad(train);
    }

    public void openRampIfLastTrainOnRoad(Train train, RailRoad railRoad)
    {
        if (railRoad.getLastTrainOnRoad() == null || train.equals(railRoad.getLastTrainOnRoad()))
        {
            setRampStatus(railRoad, false);
        }
    }

    public void setRampStatus(RailRoad railRoad, boolean isClosed)
    {
        railRoad.getRamps().forEach(ramp ->
        {
            synchronized (ramp.RAMP_LOCK)
            {
                if (ramp.isClosed() == isClosed)
                    return;
                ramp.setIsClosed(isClosed);
                Platform.runLater(() ->
                {
                    var lbl = MapController.getGridCell(ramp.getxPosition(), ramp.getyPosition());
                    if (lbl != null)
                        LabelUtils.setLableBackgroundAndBorderColor(lbl, isClosed ? ColorConstants.RED : ColorConstants.BLACK);
                });
                ramp.RAMP_LOCK.notify();
            }
        });
    }

    public void addParkedTrain(Train train)
    {
        parkedTrains.put(train.getTrainName(), train);
    }

    public void removeParkedTrain(Train train)
    {
        parkedTrains.remove(train.getTrainName());
    }

    public String getStationName()
    {
        return stationName;
    }

    public void setStationName(String stationName)
    {
        this.stationName = stationName;
    }

    public List<RailRoad> getTrainRailRoads()
    {
        return trainRailRoads;
    }

    public void setTrainRailRoads(List<RailRoad> trainRailRoads)
    {
        this.trainRailRoads = trainRailRoads;
    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TrainStation that = (TrainStation) o;
        return stationName.equals(that.stationName) && trainRailRoads.equals(that.trainRailRoads) && parkedTrains.equals(that.parkedTrains) && destinationFields.equals(that.destinationFields);
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(stationName, trainRailRoads, parkedTrains, destinationFields);
    }
}
