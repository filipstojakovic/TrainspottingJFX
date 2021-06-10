package project.spawners;

import project.jsonparsers.TrainJsonParser;
import project.vehiclestuff.trainstuff.Train;
import project.vehiclestuff.trainstuff.trainstation.TrainStation;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static java.nio.file.StandardWatchEventKinds.ENTRY_CREATE;

public class TrainSpawner extends Thread
{
    public static final int MINOR_DELAY = 200;
    private final File watchDirectoryFile;
    private final HashMap<String, TrainStation> trainStationMap;
    private final List<String> visitedFileNames;

    public TrainSpawner(String directoryPath, HashMap<String, TrainStation> trainStationMap)
    {
        visitedFileNames = new ArrayList<>();
        watchDirectoryFile = new File(directoryPath);
        this.trainStationMap = trainStationMap;
    }

    @Override
    public void run()
    {
        try
        {
            WatchService watcher = FileSystems.getDefault().newWatchService();
            Path dir = watchDirectoryFile.toPath();
            dir.register(watcher, ENTRY_CREATE);
            //System.out.println("Watch Service registered for dir: " + dir);

            while (true)
            {
                WatchKey key;
                try
                {
                    key = watcher.take();
                } catch (InterruptedException ex)
                {
                    return;
                }

                for (WatchEvent<?> event : key.pollEvents())
                {
                    WatchEvent.Kind<?> kind = event.kind();
                    WatchEvent<Path> ev = (WatchEvent<Path>) event;
                    Path fileName = ev.context();
                    System.out.println(kind.name() + ": " + fileName);
                    if (fileName.toString().trim().endsWith(".json")
                            && !visitedFileNames.contains(fileName.toString())
                            && kind.equals(ENTRY_CREATE))
                    {
                        Thread.sleep(MINOR_DELAY);
                        visitedFileNames.add(fileName.toString());
                        Path filePath = dir.resolve(fileName);
                        Train train = getTrainFromFile(filePath);
                        if (train != null)
                            train.start();
                    }
                }

                boolean valid = key.reset();
                if (!valid)
                {
                    break;
                }
            }

        } catch (IOException | InterruptedException ex)
        {
            System.err.println(ex);
        }
    }

    public void getAllTrainsFromDirectory() throws FileNotFoundException
    {
        File[] files = watchDirectoryFile.listFiles();
        //If this pathname does not denote a directory, then listFiles() returns null.
        if (files == null)
            throw new FileNotFoundException();

        new Thread(() ->
        {
            for (File file : files)
            {
                try
                {
                    if (file.isFile())
                    {
                        Thread.sleep(MINOR_DELAY);
                        Train train = getTrainFromFile(file.toPath());
                        train.start();

                    }
                } catch (InterruptedException e)
                {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    private Train getTrainFromFile(Path filePath)
    {
        Train train = TrainJsonParser.getTrainPartsFromJson(trainStationMap, filePath.toString());

        //TODO: validate train
        return train;
    }
}
