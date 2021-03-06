package project.vehiclestuff.trainstuff;

import javafx.application.Platform;
import javafx.scene.control.Label;
import project.Util.GenericLogger;
import project.Util.Utils;
import project.exception.TrainNotValidException;
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
            if (trainPartList.isEmpty())
                throw new TrainNotValidException("Train has no parts");
            constructMoveableParts();

            final TrainHistory trainHistory = new TrainHistory();
            final TrainStation beginingStation = destinationStationsOrder.poll();
            beginingStation.addParkedTrain(this);

            TrainStation firstStation = null;
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

                    currentRailRoad = TrainStation.getRailRoadBetweenStations(firstStation, secondStation);
                    oppositeRailRoad = TrainStation.getRailRoadBetweenStations(secondStation, firstStation);

                    while (!oppositeRailRoad.isRailRoadEmpty())
                        Thread.sleep(trainSpeed);

                    firstStation.removeParkedTrain(this); // remove from TrainStation parked train
                    firstStation.addTrainOnRailRoad(this, currentRailRoad);

                    trainHistory.addStationParkedTime(firstStation.getStationName(), System.currentTimeMillis() - currentTime);
                    trainHistory.addStationDepartureTime(firstStation.getStationName(), System.currentTimeMillis());

                    //train move
                    Field stationDepartureField = firstStation.getStartingFieldForDestination(secondStation.getStationName());
                    startTrainMovement(trainHistory, stationDepartureField, currentRailRoad, firstStation);
                    //train !move

                    firstStation.removeTrainOffRailRoad(this, currentRailRoad);
                    secondStation.addParkedTrain(this);
                }

            } catch (InterruptedException ex)
            {
                GenericLogger.createAsyncLog(this.getClass(), ex);
            }
            if (firstStation != null)
                firstStation.removeTrainOffRailRoad(this, currentRailRoad);
            secondStation.removeParkedTrain(this);
            serializeTrainHistory(trainHistory);

        } catch (TrainNotValidException ex)
        {
            GenericLogger.createAsyncLog(this.getClass(), ex);
        }


    }

    private void startTrainMovement(TrainHistory trainHistory, Field stationDepartureField
            , RailRoad currentRailRoad, TrainStation departureStation) throws InterruptedException
    {
        Field firstRailRoadField = currentRailRoad.getStartingField();
        int currentX = firstRailRoadField.getxPosition();
        int currentY = firstRailRoadField.getyPosition();

        int previousX = stationDepartureField.getxPosition();
        int previousY = stationDepartureField.getyPosition();

        Field nextField = firstRailRoadField;

        boolean railRoadHasRamp = currentRailRoad.getRamps().size() > 0;
        int numOfRamps = currentRailRoad.getRamps().size();
        int fieldsAfterRamp = 0;
        while (!(nextField instanceof TrainStationField))
        {
            Label label = MapController.getGridCell(nextField.getxPosition(), nextField.getyPosition());
            while (!"".equals(label.getText()))
                Thread.sleep(trainSpeed);


            trainHistory.addPositionHistory(currentX, currentY);
            shiftBackTrainPosition(currentX, currentY);
            drawTrainOnMap();
            if (railRoadHasRamp && nextField instanceof RampField)
                numOfRamps--;
            if (railRoadHasRamp && numOfRamps == 0)
            {
                fieldsAfterRamp++;
                if (fieldsAfterRamp - currentRailRoad.getRamps().size() == moveableParts.size())
                {
                    departureStation.openRampIfLastTrainOnRoad(this, currentRailRoad);
                }
            }
            Thread.sleep(trainSpeed); // Train speed here

            nextField = Map.getNextField(currentX, currentY, previousX, previousY, RailField.class);
            previousX = currentX;
            previousY = currentY;
            currentX = nextField.getxPosition();
            currentY = nextField.getyPosition();

            if (nextField instanceof TrainStationField)
                parkTrainInStation(railRoadHasRamp, numOfRamps, fieldsAfterRamp, currentRailRoad, departureStation);
        }
    }

    private void parkTrainInStation(boolean railRoadHasRamp, int numOfRamps, int fieldsAfterRamp, RailRoad currentRailRoad, TrainStation departureStation) throws InterruptedException
    {
        for (var ignored : moveableParts)
        {
            shiftBackTrainPosition(-1, -1);
            drawTrainOnMap();
            if (railRoadHasRamp && numOfRamps == 0)
            {
                fieldsAfterRamp++;
                if (fieldsAfterRamp - currentRailRoad.getRamps().size() == moveableParts.size())
                {
                    departureStation.openRampIfLastTrainOnRoad(this, currentRailRoad);
                }
            }
            Thread.sleep(trainSpeed);
        }
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
            GenericLogger.createAsyncLog(this.getClass(), ex);
        }
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

    public List<TrainPart> getTrainPartList()
    {
        return trainPartList;
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
        return trainSpeed == train.trainSpeed
                && isElectric == train.isElectric
                && trainName.equals(train.trainName)
                && Objects.equals(destinationStationsOrder, train.destinationStationsOrder)
                && trainPartList.equals(train.trainPartList) && moveableParts.equals(train.moveableParts);
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(trainName, destinationStationsOrder, trainPartList, moveableParts, trainSpeed, isElectric);
    }
}
