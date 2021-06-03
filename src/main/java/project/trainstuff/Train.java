package project.trainstuff;

import javafx.application.Platform;
import project.map.Field.Field;
import project.map.Field.RailField;
import project.map.Field.TrainStationField;
import project.map.Map;
import project.map.MapController;
import project.trainstuff.trainstation.TrainStation;

import java.util.List;
import java.util.Queue;

public class Train extends Thread
{
    private Queue<TrainStation> destinationStationsOrder;
    private List<TrainPart> trainPartList;
    private boolean isElectric; // if there is a Locomotive with Electric Engine
    private final String THUNDER = "⚡";

    public Train()
    {
        setDaemon(true);
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
            if (currentRailRoad != null)
                currentRailRoad.setOccupied(false);
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

            TrainStation finalFirstStation = firstStation;
            TrainStation finalSecondStation = secondStation;
            currentRailRoad = firstStation.getTrainRailRoads().stream()
                    .filter(trainLine1 -> finalFirstStation.getStationName().equals(trainLine1.getStartStationName())
                            && finalSecondStation.getStationName().equals(trainLine1.getEndStationName()))
                    .findFirst()
                    .orElse(null);

            currentRailRoad.setOccupied(true);
            Field startField = currentRailRoad.getStartingField();
            int currentX = startField.getxPosition();
            int currentY = startField.getyPosition();

            Field firstLineField = firstStation.getStartingFieldForDestination(secondStation.getStationName());
            int previousX = firstLineField.getxPosition();
            int previousY = firstLineField.getyPosition();

            boolean flag = true;
            Field nextField = startField;
            while (flag)
            {
                //while(field.isOccupied())
                if (nextField instanceof TrainStationField)
                {
                    flag = false;
                    continue;
                }

                shiftBackTrainPosition(currentX, currentY);
                drawTrainOnMap();

                try
                {
                    Thread.sleep(300); // Train speed here
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
        }
        if (currentRailRoad != null)
            currentRailRoad.setOccupied(false);
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

}
