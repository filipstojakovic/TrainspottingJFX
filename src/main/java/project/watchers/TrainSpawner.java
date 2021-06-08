package project.watchers;

import project.jsonparsers.TrainJsonParser;
import project.trainstuff.Train;
import project.trainstuff.trainstation.TrainStation;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.*;
import java.util.HashMap;

import static java.nio.file.StandardWatchEventKinds.ENTRY_CREATE;

public class TrainSpawner extends Thread
{
    private final Path watchDirectoryPath;
    private final HashMap<String, TrainStation> trainStationMap;

    public TrainSpawner(String directoryPath, HashMap<String, TrainStation> trainStationMap)
    {
        watchDirectoryPath = (new File(directoryPath)).toPath();
        this.trainStationMap = trainStationMap;
    }

    @Override
    public void run()
    {
        try
        {
            WatchService watcher = FileSystems.getDefault().newWatchService();
            File directory = watchDirectoryPath.toFile();
            Path dir = directory.toPath();
            dir.register(watcher, ENTRY_CREATE);
            System.out.println("Watch Service registered for dir: " + directory.getAbsolutePath());

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
                    if (fileName.toString().trim().endsWith(".json") && kind.equals(ENTRY_CREATE))
                    {
                        Path filePath = dir.resolve(fileName);
                        Train train = getTrainFromFile(filePath);
                        train.start();
                        break;
                    }
                }

                boolean valid = key.reset();
                if (!valid)
                {
                    break;
                }
            }

        } catch (IOException ex)
        {
            System.err.println(ex);
        }
    }

    public void getAllTrainsFromDirectory() throws FileNotFoundException
    {
        File[] files = watchDirectoryPath.toFile().listFiles();
        //If this pathname does not denote a directory, then listFiles() returns null.
        if(files==null)
            throw new FileNotFoundException();

        new Thread(()->
        {
            for (File file : files)
            {
                if (file.isFile())
                {
                    Train train = getTrainFromFile(file.toPath());
                    train.start();
                    try
                    {
                        Thread.sleep(10);
                    } catch (InterruptedException e)
                    {
                        e.printStackTrace();
                    }
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
