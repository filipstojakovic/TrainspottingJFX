package project.trainstuff;

import javafx.application.Platform;
import project.map.Field.Field;
import project.map.Field.RailField;
import project.map.Field.TrainStationField;
import project.map.Map;
import project.map.MapController;
import project.trainstuff.trainstation.TrainStation;

import java.util.List;
import java.util.Objects;
import java.util.Queue;

public class Train extends Thread
{
    private final String trainName;
    private Queue<TrainStation> destinationStationsOrder;
    private List<TrainPart> trainPartList;
    private boolean isElectric; // if there is a Locomotive with Electric Engine
    private static final String THUNDER = "⚡";

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
    public void run()
    {
        //todo: check if trainPartList.size > 0 ??
        final TrainStation beginingStation = destinationStationsOrder.poll();

        TrainStation firstStation;
        TrainStation secondStation = beginingStation;

        RailRoad currentRailRoad = null;

        while (!destinationStationsOrder.isEmpty())
        {
            /*
            //        while (startField.isOccupied())
            //        {
            //            try
            //            {
            //                Thread.sleep(500);
            //            } catch (InterruptedException e)
            //            {
            //                e.printStackTrace();
            //            }
            //        }
            */
            firstStation = secondStation;
            secondStation = destinationStationsOrder.poll();

            currentRailRoad = getRailRoadBetweenStations(firstStation, secondStation);
            currentRailRoad.addTrainOnRoad(this);

            Field startField = currentRailRoad.getStartingField();
            int currentX = startField.getxPosition();
            int currentY = startField.getyPosition();

            Field firstLineField = firstStation.getStartingFieldForDestination(secondStation.getStationName());
            int previousX = firstLineField.getxPosition();
            int previousY = firstLineField.getyPosition();

            Field nextField = startField;
            while (!(nextField instanceof TrainStationField))
            {
                //while(field.isOccupied())

                shiftBackTrainPosition(currentX, currentY);
                drawTrainOnMap();

                try
                {
                    Thread.sleep(100); // Train speed here
                } catch (InterruptedException e)
                {
                    e.printStackTrace();
                }

                nextField = Map.getNextField(currentX, currentY, previousX, previousY, RailField.class);
                previousX = currentX;
                previousY = currentY;
                currentX = nextField.getxPosition();
                currentY = nextField.getyPosition();
            }

            currentRailRoad.removeTrainFromRailRoad(this);
        }
        if (currentRailRoad != null)
            currentRailRoad.removeTrainFromRailRoad(this);
    }

    private RailRoad getRailRoadBetweenStations(final TrainStation firstStation,final TrainStation secondStation)
    {
        RailRoad currentRailRoad;
        currentRailRoad = firstStation.getTrainRailRoads().stream()
                .filter(trainLine1 -> firstStation.getStationName().equals(trainLine1.getStartStationName())
                        && secondStation.getStationName().equals(trainLine1.getEndStationName()))
                .findFirst()
                .orElse(null);
        return currentRailRoad;
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
            Platform.runLater(() -> MapController.getGridCell(part.getCurrentX(), part.getCurrentY()).setText(part.getPartName()));
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
