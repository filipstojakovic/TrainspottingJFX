package project.Util;

import project.exception.TrainNotValidException;
import project.exception.UnreachableStationException;
import project.vehiclestuff.trainstuff.Train;
import project.vehiclestuff.trainstuff.TrainPart;
import project.vehiclestuff.trainstuff.locomotive.Locomotive;
import project.vehiclestuff.trainstuff.trainpartinterface.ICargo;
import project.vehiclestuff.trainstuff.trainpartinterface.IManeuver;
import project.vehiclestuff.trainstuff.trainpartinterface.IPassenger;
import project.vehiclestuff.trainstuff.trainpartinterface.IUniversal;
import project.vehiclestuff.trainstuff.trainstation.TrainStation;
import project.vehiclestuff.trainstuff.wagon.Wagon;

import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.stream.Collectors;

public class TrainValidator
{
    public static boolean isTrainValid(Train train) throws TrainNotValidException
    {
        final var trainParts = train.getTrainPartList();

        var locomotiveParts = trainParts.stream()
                .filter(part -> part instanceof Locomotive)
                .collect(Collectors.toList());

        if (locomotiveParts.isEmpty())
            throw new TrainNotValidException("Train has no locomotive");

        boolean hasUniversalLocomotive = locomotiveParts.stream() // is there at least one UniversalLocomotive
                .anyMatch(part -> part instanceof IUniversal);

        final var wagonParts = trainParts.stream()
                .filter(part -> part instanceof Wagon)
                .collect(Collectors.toList());

        return (areLocomotivesValid(locomotiveParts, IPassenger.class) && areWagonsValid(wagonParts, hasUniversalLocomotive, IPassenger.class))
                || (areLocomotivesValid(locomotiveParts, ICargo.class) && areWagonsValid(wagonParts, hasUniversalLocomotive, ICargo.class))
                || (areLocomotivesValid(locomotiveParts, IManeuver.class) && areWagonsValid(wagonParts, hasUniversalLocomotive, IManeuver.class));
    }

    public static boolean isTrainDestinationStationReachable(Train train) throws TrainNotValidException, UnreachableStationException
    {
        if (train.getDestinationStationsOrder() == null || train.getDestinationStationsOrder().isEmpty())
            throw new TrainNotValidException("No destination station");

        final Queue<TrainStation> stationsQueue = new LinkedList<>(train.getDestinationStationsOrder()); //deep copy
        TrainStation firstStation = stationsQueue.poll();
        TrainStation secondStation;
        while (!stationsQueue.isEmpty())
        {
            secondStation = stationsQueue.poll();
            if (TrainStation.getRailRoadBetweenStations(firstStation, secondStation) == null)
                throw new UnreachableStationException("No railroad between " + firstStation.getStationName()
                        + " and " + secondStation.getStationName());

            firstStation = secondStation;
        }
        return true;
    }

    private static boolean areLocomotivesValid(List<TrainPart> locomotiveParts, Class<?> className)
    {
        var validParts = locomotiveParts.stream()
                .filter(part -> (className.isInstance(part) || part instanceof IUniversal))
                .collect(Collectors.toList());

        return locomotiveParts.size() == validParts.size();
    }

    private static boolean areWagonsValid(List<TrainPart> wagonParts, boolean hasUniversalLocomotive, Class<?> className)
    {
        var validWagons = wagonParts.stream()
                .filter(wagon -> (className.isInstance(wagon)
                        || (hasUniversalLocomotive && (wagon instanceof IUniversal))))
                .collect(Collectors.toList());

        return wagonParts.size() == validWagons.size();
    }

}
