package project.trainstuff;

import javafx.application.Platform;
import project.map.Field.Field;
import project.map.Field.RailField;
import project.map.Field.TrainStationField;
import project.map.Map;
import project.map.MapController;
import project.trainstuff.trainstation.TrainLine;
import project.trainstuff.trainstation.TrainStation;

import java.util.List;
import java.util.Queue;

public class Train extends Thread
{
    private Queue<TrainStation> destinationStationsOrder;
    private List<TrainPart> trainPartList;
    private boolean isElectric; // if there is a Locomotive with Electric Engine

    public Train()
    {
        setDaemon(true);
    }

    @Override
    public void run()
    {
        final TrainStation beginingStation = destinationStationsOrder.poll();

        TrainStation firstStation;
        TrainStation secondStation = beginingStation;

        while (!destinationStationsOrder.isEmpty())
        {
            /*
        try
        {
            Field startField = Map.getField(xStart, yStart);
            //if (!(startField instanceof RailField || startField instanceof TrainStationField))
            //    System.err.println("NE MOZE SE OVDA VOZITI"); // throw exception
            //
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
            TrainLine trainLine = firstStation.getTrainLines().stream()
                    .filter(trainLine1 -> finalFirstStation.getStationName().equals(trainLine1.getStartStationName())
                            && finalSecondStation.getStationName().equals(trainLine1.getEndStationName()))
                    .findFirst()
                    .orElse(null);

            Field firstField = firstStation.getStartingFieldForDestination(secondStation.getStationName());
            Field startField = trainLine.getStartingField();
            int tmpx = startField.getxPosition();
            int tmpy = startField.getyPosition();
            int previousx = firstField.getxPosition();
            int previousy = firstField.getyPosition();

            boolean flag = true;
            while (flag)
            {
                Field field = Map.getNextField(tmpx, tmpy, previousx, previousy, RailField.class);
                //while(field.isOccupied())

                if (field instanceof TrainStationField)
                    flag = false;

                previousx = tmpx;
                previousy = tmpy;
                tmpx = field.getxPosition();
                tmpy = field.getyPosition();

                for (int i = trainPartList.size() - 1; i > 0; i--)
                {
                    TrainPart trainPart = trainPartList.get(i);
                    int partx = trainPart.getCurrentX();
                    int party = trainPart.getCurrentY();
                    Platform.runLater(() -> MapController.getGridCell(partx, party).setText(""));
                    //todo remove isOccupied
                    TrainPart trainPartBefore = trainPartList.get(i - 1);
                    trainPart.setPosition(trainPartBefore.getCurrentX(), trainPartBefore.getCurrentY());
                }
                trainPartList.get(0).setPosition(field.getxPosition(), field.getyPosition());

                for (var part : trainPartList)
                {
                    //todo put isOccupied=true, maybe its enough just to check !"".equalst(field.label.text)
                    Platform.runLater(() -> MapController.getGridCell(part.getCurrentX(), part.getCurrentY()).setText(part.getPartName()));
                }

                try
                {
                    Thread.sleep(100);
                } catch (InterruptedException e)
                {
                    e.printStackTrace();
                }
            }

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
