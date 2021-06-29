package project.spawners;

import org.json.simple.parser.ParseException;
import project.Util.GenericLogger;
import project.Util.TrainValidator;
import project.Util.Utils;
import project.exception.TrainNotValidException;
import project.exception.UnreachableStationException;
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
import java.util.logging.Level;

import static java.nio.file.StandardWatchEventKinds.ENTRY_CREATE;

public class TrainSpawner extends Thread
{
    public static final int MINOR_DELAY = 200; // for watcher bug
    private final File watchDirectoryFile;
    public static String trainHistoryDirPath;
    private final HashMap<String, TrainStation> trainStationMap;
    private final List<String> visitedFileNames;
    private boolean isActive;

    public TrainSpawner(String directoryPath, HashMap<String, TrainStation> trainStationMap)
    {
        setDaemon(true);
        visitedFileNames = new ArrayList<>();
        Utils.createFolderIfNotExists(directoryPath);
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

            isActive = true;
            while (isActive)
            {
                WatchKey key;
                try
                {
                    key = watcher.take();
                } catch (InterruptedException ex)
                {
                    GenericLogger.createAsyncLog(this.getClass(), ex);
                    return;
                }

                for (WatchEvent<?> event : key.pollEvents())
                {
                    if (!isActive)
                        break;
                    WatchEvent.Kind<?> kind = event.kind();
                    WatchEvent<Path> ev = (WatchEvent<Path>) event;
                    Path fileName = ev.context();
                    if (kind.equals(ENTRY_CREATE))
                        System.out.println(kind.name() + ": " + fileName);
                    if (fileName.toString().trim().endsWith(".json")
                            && kind.equals(ENTRY_CREATE)
                            && !visitedFileNames.contains(fileName.toString()))
                    {
                        try
                        {
                            Thread.sleep(MINOR_DELAY);
                            visitedFileNames.add(fileName.toString());
                            Path filePath = dir.resolve(fileName);
                            Train train = getTrainFromFile(filePath);
                            train.start();

                        } catch (InterruptedException | TrainNotValidException | ParseException | UnreachableStationException ex)
                        {
                            GenericLogger.createAsyncLog(this.getClass(), ex);
                        }
                    }
                }

                boolean valid = key.reset();
                if (!valid)
                    break;
            }
            System.out.println("Closing " + watchDirectoryFile.getName() + " folder watcher");

        } catch (IOException ex)
        {
            GenericLogger.createAsyncLog(this.getClass(), ex);
        }
    }

    public void getAllTrainsFromDirectory() throws FileNotFoundException
    {
        Utils.createFolderIfNotExists(watchDirectoryFile.getAbsolutePath());
        File[] files = watchDirectoryFile.listFiles();
        //If this pathname does not denote a directory, then listFiles() returns null.
        if (files == null)
            throw new FileNotFoundException();

        new Thread(() ->
        {
            for (File file : files)
            {
                if (file.isFile())
                {
                    try
                    {
                        Thread.sleep(MINOR_DELAY);
                        Train train = getTrainFromFile(file.toPath());
                        train.start();
                    } catch (Exception ex)
                    {
                        GenericLogger.createAsyncLog(this.getClass(), Level.WARNING, "Problem detectedin file named: " + file.getName(), ex);
                    }
                }
            }
        }).start();
    }

    private Train getTrainFromFile(Path filePath) throws TrainNotValidException, IOException, ParseException, UnreachableStationException
    {
        Train train = TrainJsonParser.getTrainPartsFromJson(trainStationMap, filePath.toString());

        if (!TrainValidator.isTrainValid(train))
            throw new TrainNotValidException(train.getTrainName());

        if (!TrainValidator.isTrainDestinationStationReachable(train))
            throw new UnreachableStationException();

        return train;
    }


    public void close()
    {
        isActive = false;
    }
}
