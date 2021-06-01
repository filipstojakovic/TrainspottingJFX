package project.trainstuff.trainstation;

import project.map.Field.Field;
import project.trainstuff.Train;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class TrainStation extends Thread
{
    private String stationName;
    private List<TrainLine> trainLines;
    private List<Train> parkedTrains;

    private HashMap<String, Field> destinationFields;

    public TrainStation()
    {
        setDaemon(true);
        parkedTrains = new ArrayList<>();
        destinationFields = new HashMap<>();
    }

    public TrainStation(String stationName, TrainLine trainLine)
    {
        this();
        setName("THREAD => trainStation: " + stationName);
        this.stationName = stationName;
        addTrainLine(trainLine);
    }

    public TrainStation(String stationName, List<TrainLine> trainLines)
    {
        this();
        setName("THREAD => trainStation: " + stationName);
        this.stationName = stationName;
        this.trainLines = trainLines;
    }

    @Override
    public void run()
    {
        boolean flag = true;
        while (flag)
        {
            for (var train : parkedTrains)
            {
                //if(stationName.equals(train.getDestination()))

            }
            try
            {
                Thread.sleep(2000);
            } catch (InterruptedException e)
            {
                e.printStackTrace();
            }
        }
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

    public void addTrainLine(TrainLine trainLine)
    {
        if (trainLines == null)
            trainLines = new ArrayList<>();
        trainLines.add(trainLine);
    }

    public String getStationName()
    {
        return stationName;
    }

    public void setStationName(String stationName)
    {
        setName("THREAD => trainStation: " + stationName);
        this.stationName = stationName;
    }

    public List<TrainLine> getTrainLines()
    {
        return trainLines;
    }

    public void setTrainLines(List<TrainLine> trainLines)
    {
        this.trainLines = trainLines;
    }

    public List<Train> getParkedTrains()
    {
        return parkedTrains;
    }

    public void setParkedTrains(List<Train> parkedTrains)
    {
        this.parkedTrains = parkedTrains;
    }

}
