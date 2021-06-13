package project.vehiclestuff.trainstuff;

import javafx.application.Platform;
import javafx.scene.control.Label;
import project.Util.GenericLogger;
import project.Util.Utils;
import project.exception.NoDestinationStationsException;
import project.exception.TrainWithoutPartsException;
import project.map.Field.Field;
import project.map.Field.RailField;
import project.map.Field.RampField;
import project.map.Field.TrainStationField;
import project.map.Map;
import project.map.MapController;
import project.spawners.TrainSpawner;
import project.vehiclestuff.trainstuff.trainstation.TrainStation;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Queue;

public class Train extends Thread
{
    private final String trainName;
    private Queue<TrainStation> destinationStationsOrder;
    private List<TrainPart> trainPartList;
    private List<IMoveable> moveableParts;
    private int trainSpeed;
    private boolean isElectric = false; //true if there is a Locomotive with Electric Engine

    public Train(String trainName)
    {
        this.trainName = trainName;
    }

    public String getTrainName()
    {
        return trainName;
    }

    @Override
    public void run()
    {
        try
        {
            validateTrainAttributes();

        } catch (TrainWithoutPartsException | NoDestinationStationsException ex)
        {
            GenericLogger.asyncLog(this.getClass(), ex);
            return;
        }
        constructMoveableParts();

        final TrainHistory trainHistory = new TrainHistory();
        final TrainStation beginingStation = destinationStationsOrder.poll();
        beginingStation.addParkedTrain(this);

        TrainStation firstStation;
        TrainStation secondStation = beginingStation;

        RailRoad currentRailRoad = null;
        RailRoad oppositeRailRoad = null;
        try
        {
            while (!destinationStationsOrder.isEmpty())
            {
                long currentTime = System.currentTimeMillis();
                firstStation = secondStation;
                secondStation = destinationStationsOrder.poll();

                currentRailRoad = getRailRoadBetweenStations(firstStation, secondStation);
                oppositeRailRoad = getRailRoadBetweenStations(secondStation, firstStation);

                while (!oppositeRailRoad.isRailRoadEmpty())
                    Thread.sleep(trainSpeed);

                currentRailRoad.addTrainOnRoad(this);
                firstStation.removeParkedTrain(this); // remove from TrainStation parked train

                trainHistory.addStationParkedTime(firstStation.getStationName(), System.currentTimeMillis() - currentTime);

                Field railRoadField = currentRailRoad.getStartingField();
                int currentX = railRoadField.getxPosition();
                int currentY = railRoadField.getyPosition();

                Field stationField = firstStation.getStartingFieldForDestination(secondStation.getStationName());
                int previousX = stationField.getxPosition();
                int previousY = stationField.getyPosition();

                trainHistory.addStationDepartureTime(firstStation.getStationName(), System.currentTimeMillis());
                Field nextField = railRoadField;
                while (!(nextField instanceof TrainStationField))
                {
                    Label label = MapController.getGridCell(nextField.getxPosition(), nextField.getyPosition());
                    while (!"".equals(label.getText()))
                        Thread.sleep(trainSpeed);

                    trainHistory.addPositionHistory(currentX, currentY);
                    shiftBackTrainPosition(currentX, currentY);
                    drawTrainOnMap();
                    Thread.sleep(trainSpeed); // Train speed here

                    nextField = Map.getNextField(currentX, currentY, previousX, previousY, RailField.class);
                    previousX = currentX;
                    previousY = currentY;
                    currentX = nextField.getxPosition();
                    currentY = nextField.getyPosition();
                }

                parkTrainInStation();
                secondStation.addParkedTrain(this);
                currentRailRoad.removeTrainFromRailRoad(this);
            }

        } catch (InterruptedException ex)
        {
            GenericLogger.asyncLog(this.getClass(), ex);
        }

        if (currentRailRoad != null)
            currentRailRoad.removeTrainFromRailRoad(this);
        secondStation.removeParkedTrain(this);

        serializeTrainHistory(trainHistory);
    }

    private void constructMoveableParts()
    {
        moveableParts = new ArrayList<>();
        if (isElectric)
            moveableParts.add(new ElectricField());
        moveableParts.addAll(trainPartList);
        if (isElectric)
            moveableParts.add(new ElectricField());
    }

    private void serializeTrainHistory(TrainHistory trainHistory)
    {
        Utils.createFolderIfNotExists(TrainSpawner.trainHistoryDirPath);
        String path = TrainSpawner.trainHistoryDirPath + File.separator + trainName;
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(path)))
        {
            oos.writeObject(trainHistory);
        } catch (IOException ex)
        {
            GenericLogger.asyncLog(this.getClass(), ex);
        }
    }

    private void validateTrainAttributes() throws TrainWithoutPartsException, NoDestinationStationsException
    {
        if (trainPartList == null || trainPartList.isEmpty())
            throw new TrainWithoutPartsException();

        if (destinationStationsOrder == null || destinationStationsOrder.isEmpty())
            throw new NoDestinationStationsException();

        //var firstStation = destinationStationsOrder.poll();

    }

    private void parkTrainInStation() throws InterruptedException
    {
        for (var trainPart : moveableParts)
        {
            shiftBackTrainPosition(-1, -1);
            drawTrainOnMap();
            Thread.sleep(trainSpeed); // Train speed here
        }
    }

    private RailRoad getRailRoadBetweenStations(final TrainStation firstStation, final TrainStation secondStation)
    {
        return firstStation.getTrainRailRoads().stream()
                .filter(trainLine1 -> firstStation.getStationName().equals(trainLine1.getStartStationName())
                        && secondStation.getStationName().equals(trainLine1.getEndStationName()))
                .findFirst()
                .orElse(null);
    }

    private void shiftBackTrainPosition(int currentX, int currentY)
    {
        deleteLastTrainPartText();
        for (int i = moveableParts.size() - 1; i > 0; i--)
        {
            var trainPart = moveableParts.get(i);
            var trainPartBefore = moveableParts.get(i - 1);
            trainPart.setPosition(trainPartBefore.getCurrentX(), trainPartBefore.getCurrentY());
        }
        moveableParts.get(0).setPosition(currentX, currentY);
    }

    private void deleteLastTrainPartText()
    {
        var lastField = moveableParts.get(moveableParts.size() - 1);
        final int xPosition = lastField.getCurrentX();
        final int yPosition = lastField.getCurrentY();
        if (isElectric)
            setFieldElectricity(xPosition, yPosition, false);
        Platform.runLater(() ->
        {
            var label = MapController.getGridCell(xPosition, yPosition);
            if (label != null)
                label.setText("");
        });
    }

    private void drawTrainOnMap()
    {
        Platform.runLater(() ->
        {
            boolean isFirstPart = true;
            for (var trainPart : moveableParts)
            {
                final int xPosition = trainPart.getCurrentX();
                final int yPosition = trainPart.getCurrentY();
                isFirstPart = drawTrainPartOnMap(isFirstPart, trainPart, xPosition, yPosition);
                if (isElectric)
                    setFieldElectricity(xPosition, yPosition, true);
            }
        });
    }

    private void setFieldElectricity(int x, int y, boolean hasElectricity)
    {
        Field field = Map.getField(x, y);
        if (field instanceof RailField railField)
        {
            railField.setHasElectricity(hasElectricity);

        } else if (field instanceof RampField rampField)
        {
            rampField.setHasElectricity(hasElectricity);
        }
    }

    private boolean drawTrainPartOnMap(boolean isFirstPart, IMoveable trainPart, int xPosition, int yPosition)
    {
        var label = MapController.getGridCell(xPosition, yPosition);
        if (label != null)
        {
            String text = trainPart.getPartName();
            if (isFirstPart && trainPart instanceof TrainPart)
            {
                text += trainPartList.size();
                isFirstPart = false;
            }
            label.setText(text);
        }
        return isFirstPart;
    }

    public Queue<TrainStation> getDestinationStationsOrder()
    {
        return destinationStationsOrder;
    }

    public void setDestinationStationsOrder(Queue<TrainStation> destinationStationsOrder)
    {
        this.destinationStationsOrder = destinationStationsOrder;
    }

    public boolean isElectric()
    {
        return isElectric;
    }

    public void setElectric(boolean electric)
    {
        isElectric = electric;
    }

    public void setTrainPartList(List<TrainPart> trainPartList)
    {
        this.trainPartList = trainPartList;
    }

    public void setTrainSpeed(int trainSpeed)
    {
        this.trainSpeed = trainSpeed;
    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Train train = (Train) o;
        return isElectric == train.isElectric
                && trainPartList.equals(train.trainPartList);
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(destinationStationsOrder, trainPartList, isElectric);
    }
}
