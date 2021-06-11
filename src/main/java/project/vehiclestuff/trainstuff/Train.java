package project.vehiclestuff.trainstuff;

import javafx.application.Platform;
import javafx.scene.control.Label;
import project.exception.NoDestinationStationsException;
import project.exception.TrainWithoutPartsException;
import project.map.Field.Field;
import project.map.Field.RailField;
import project.map.Field.TrainStationField;
import project.map.Map;
import project.map.MapController;
import project.vehiclestuff.trainstuff.trainstation.TrainStation;

import java.util.List;
import java.util.Objects;
import java.util.Queue;

public class Train extends Thread
{
    private static final String THUNDER = "⚡";

    private final String trainName;
    private Queue<TrainStation> destinationStationsOrder;
    private List<TrainPart> trainPartList;
    private int trainSpeed;
    private boolean isElectric; // if there is a Locomotive with Electric Engine
    private TrainHistory trainHistory;

    public static int i = 0;

    public Train()
    {
        trainName = String.valueOf(i++);
        setDaemon(true);
        trainHistory = new TrainHistory(); //TODO: add my stuff
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
            ex.printStackTrace();
            return;
        }
        //TODO: check parking in station

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
                firstStation = secondStation;
                secondStation = destinationStationsOrder.poll();

                currentRailRoad = getRailRoadBetweenStations(firstStation, secondStation);
                oppositeRailRoad = getRailRoadBetweenStations(secondStation, firstStation);

                while (!oppositeRailRoad.isRailRoadEmpty())
                    Thread.sleep(500);


                currentRailRoad.addTrainOnRoad(this);

                Field railRoadField = currentRailRoad.getStartingField();
                int currentX = railRoadField.getxPosition();
                int currentY = railRoadField.getyPosition();

                Field stationField = firstStation.getStartingFieldForDestination(secondStation.getStationName());
                int previousX = stationField.getxPosition();
                int previousY = stationField.getyPosition();

                firstStation.removeParkedTrain(this); // remove from TrainStation parked train

                Field nextField = railRoadField;
                while (!(nextField instanceof TrainStationField))
                {
                    Label label = MapController.getGridCell(nextField.getxPosition(), nextField.getyPosition());
                    while (!"".equals(label.getText()))
                        Thread.sleep(trainSpeed);

                    shiftBackTrainPosition(currentX, currentY);
                    drawTrainOnMap();
                    Thread.sleep(trainSpeed); // Train speed here

                    nextField = Map.getNextField(currentX, currentY, previousX, previousY, RailField.class);
                    previousX = currentX;
                    previousY = currentY;
                    currentX = nextField.getxPosition();
                    currentY = nextField.getyPosition();
                }

                parkTrainInStation(); // TODO: method to park in TrainStation parkedTrainList
                secondStation.addParkedTrain(this);
                currentRailRoad.removeTrainFromRailRoad(this);
            }

        } catch (Exception ex)
        {
            ex.printStackTrace();
        }

        if (currentRailRoad != null)
            currentRailRoad.removeTrainFromRailRoad(this);
    }

    private void validateTrainAttributes() throws TrainWithoutPartsException, NoDestinationStationsException
    {
        if (trainPartList == null || trainPartList.isEmpty())
            throw new TrainWithoutPartsException();

        if (destinationStationsOrder == null || destinationStationsOrder.isEmpty())
            throw new NoDestinationStationsException();
    }

    private void parkTrainInStation() throws InterruptedException
    {
        for (TrainPart trainPart : trainPartList)
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
        for (int i = trainPartList.size() - 1; i > 0; i--)
        {
            TrainPart trainPart = trainPartList.get(i);
            TrainPart trainPartBefore = trainPartList.get(i - 1);
            trainPart.setPosition(trainPartBefore.getCurrentX(), trainPartBefore.getCurrentY());
        }
        trainPartList.get(0).setPosition(currentX, currentY);
    }

    private void deleteLastTrainPartText()
    {
        var lastField = trainPartList.get(trainPartList.size() - 1);
        int xPosition = lastField.getCurrentX();
        int yPosition = lastField.getCurrentY();
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
            for (var trainPart : trainPartList)
            {
                var label = MapController.getGridCell(trainPart.getCurrentX(), trainPart.getCurrentY());
                if (label != null)
                    label.setText(trainPart.getPartName());
            }
        });
    }

    public Queue<TrainStation> getDestinationStationsOrder()
    {
        return destinationStationsOrder;
    }

    public void setDestinationStationsOrder(Queue<TrainStation> destinationStationsOrder)
    {
        this.destinationStationsOrder = destinationStationsOrder;
    }

    public List<TrainPart> getTrainPartList()
    {
        return trainPartList;
    }

    public void setTrainPartList(List<TrainPart> trainPartList)
    {
        this.trainPartList = trainPartList;
    }

    public int getTrainSpeed()
    {
        return trainSpeed;
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
