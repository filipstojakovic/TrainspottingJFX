package project.vehiclestuff.trainstuff;

import project.Util.Utils;

import java.io.Serial;
import java.io.Serializable;
import java.util.*;
import java.util.concurrent.TimeUnit;

public class TrainHistory implements Serializable
{
    @Serial
    private static final long serialVersionUID = 1L;
    private static final String ARROW = " -> ";

    class Position implements Serializable
    {
        int x;
        int y;

        public Position(int x, int y)
        {
            this.x = x;
            this.y = y;
        }
    }

    private List<Position> positionsHistory;
    private HashMap<String, Long> stationsDepartureTime;
    private HashMap<String, Long> stationsParkedTime;


    public TrainHistory()
    {
        positionsHistory = new ArrayList<>();
        stationsDepartureTime = new LinkedHashMap<>();
        stationsParkedTime = new LinkedHashMap<>();
    }

    public void addStationDepartureTime(String stationName, long timeInMillisec)
    {
        stationsDepartureTime.put(stationName, timeInMillisec);
    }

    public void addStationParkedTime(String stationName, long timeInMillisec)
    {
        stationsParkedTime.put(stationName, timeInMillisec);
    }

    public void addPositionHistory(int x, int y)
    {
        positionsHistory.add(new Position(x, y));
    }

    public List<Position> getPositionsHistory()
    {
        return positionsHistory;
    }

    @Override
    public String toString()
    {
        StringBuilder stringBuilder = new StringBuilder();

        stringBuilder.append(departureTimeString()).append("\n");
        stringBuilder.append(parkedTimeDurationString()).append("\n");
        stringBuilder.append(pathHistoryString());

        return stringBuilder.toString();
    }

    private String departureTimeString()
    {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Departure from station times:\n");
        for (var stationDepartureTime : stationsDepartureTime.entrySet())
        {
            stringBuilder.append("From station ").append(stationDepartureTime.getKey())
                    .append(" departed at: ").append(Utils.formatDate(stationDepartureTime.getValue()))
                    .append("\n");
        }
        return stringBuilder.toString();
    }

    private String parkedTimeDurationString()
    {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Stations parked time duration:\n");
        for (var stationParkedTime : stationsParkedTime.entrySet())
        {
            long seconds = TimeUnit.MILLISECONDS.toSeconds(stationParkedTime.getValue());
            stringBuilder.append("on Station ").append(stationParkedTime.getKey()).append(": ")
                    .append(seconds).append(" seconds\n");
        }
        return stringBuilder.toString();
    }

    private String pathHistoryString()
    {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Path history:\n");
        for (var position : positionsHistory)
        {
            stringBuilder.append("(").append(position.x).append(",").append(position.y).append(")").append(ARROW);
        }
        if (stringBuilder.length() > ARROW.length())
            stringBuilder.setLength(stringBuilder.length() - ARROW.length());
        return stringBuilder.toString();
    }

}

