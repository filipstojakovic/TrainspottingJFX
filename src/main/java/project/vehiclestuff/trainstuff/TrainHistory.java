package project.vehiclestuff.trainstuff;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class TrainHistory implements Serializable
{
    class Position
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

    public TrainHistory()
    {
        positionsHistory = new ArrayList<>();
    }

    public void addPositionHistory(int x, int y)
    {
        positionsHistory.add(new Position(x, y));
    }

    public List<Position> getPositionsHistory()
    {
        return positionsHistory;
    }
}

