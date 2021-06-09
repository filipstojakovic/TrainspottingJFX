package project.vehiclestuff.trainstuff;

import javafx.application.Platform;
import javafx.scene.control.Label;
import project.map.Field.Field;
import project.map.Field.RailField;
import project.map.Field.TrainStationField;
import project.map.Map;
import project.map.MapController;
import project.vehiclestuff.IMoveable;
import project.vehiclestuff.trainstuff.trainstation.TrainStation;

import java.util.List;
import java.util.Objects;
import java.util.Queue;

public class Train extends Thread implements IMoveable
{
    public static final int TRAIN_MOVE_SPEED = 100;
    private final String trainName;
    private Queue<TrainStation> destinationStationsOrder;
    private List<TrainPart> trainPartList;
    private boolean isElectric; // if there is a Locomotive with Electric Engine
    private static final String THUNDER = "⚡";

    private static final Object LOCK = new Object();

    public static int i = 0;

    public Train()
    {
        trainName = String.valueOf(i++);
        setDaemon(true);
    }

    public String getTrainName()
    {
        return trainName;
    }

    @Override
    public void writeCurrentPositionToFile(int x, int y, String description, String filePath)
    {
        String text = trainName + " " + description;
    }

    @Override
    public synchronized void run()
    {
        //todo: check if trainPartList.size > 0 ??
        final TrainStation beginingStation = destinationStationsOrder.poll();
        //todo: check if there is behinstation is null

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

                //Label label = MapController.getGridCell(currentX, currentY);
                //synchronized (LOCK)
                //{
                //    while (!"".equals(label.getText()))
                //        Thread.sleep(300);
                //}

                firstStation.removeParkedTrain(this);


                Field nextField = railRoadField;
                while (!(nextField instanceof TrainStationField))
                {
                    Label label = MapController.getGridCell(nextField.getxPosition(), nextField.getyPosition());
                    while (!"".equals(label.getText()))
                    {
                        Thread.sleep(300);
                    }
                    shiftBackTrainPosition(currentX, currentY);
                    drawTrainOnMap();
                    Thread.sleep(TRAIN_MOVE_SPEED); // Train speed here

                    nextField = Map.getNextField(currentX, currentY, previousX, previousY, RailField.class);
                    previousX = currentX;
                    previousY = currentY;
                    currentX = nextField.getxPosition();
                    currentY = nextField.getyPosition();
                }

                parkTrainInStation(); // TODO: method to park in TrainStation parkedTrainList

                currentRailRoad.removeTrainFromRailRoad(this);
            }

        } catch (Exception ex)
        {
            ex.printStackTrace();
        }

        if (currentRailRoad != null)
            currentRailRoad.removeTrainFromRailRoad(this);
    }

    private void parkTrainInStation() throws InterruptedException
    {
        for (TrainPart trainPart : trainPartList)
        {
            shiftBackTrainPosition(-1, -1);
            drawTrainOnMap();
            Thread.sleep(TRAIN_MOVE_SPEED); // Train speed here
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
        for (int i = trainPartList.size() - 1; i > 0; i--)
        {
            TrainPart trainPart = trainPartList.get(i);
            int partx = trainPart.getCurrentX();
            int party = trainPart.getCurrentY();
            Platform.runLater(() -> MapController.getGridCell(partx, party).setText(""));
            TrainPart trainPartBefore = trainPartList.get(i - 1);
            trainPart.setPosition(trainPartBefore.getCurrentX(), trainPartBefore.getCurrentY());
        }
        trainPartList.get(0).setPosition(currentX, currentY);
    }

    private void drawTrainOnMap()
    {
        for (var part : trainPartList)
        {
            Platform.runLater(() ->
                    MapController.getGridCell(part.getCurrentX(), part.getCurrentY())
                            .setText(part.getPartName()));
        }
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
