package project.spawners;

import project.vehiclestuff.streetstuff.StreetRoad;
import project.vehiclestuff.streetstuff.streetvehicle.Car;
import project.vehiclestuff.streetstuff.streetvehicle.StreetVehicle;
import project.vehiclestuff.streetstuff.streetvehicle.Truck;

import java.io.*;
import java.nio.file.*;
import java.util.HashMap;
import java.util.List;
import java.util.Properties;
import java.util.Random;

import static java.nio.file.StandardWatchEventKinds.ENTRY_MODIFY;

public class StreetVehicleSpawner extends Thread
{
    public static final String SPAWN_SPEED_PROP = "speed";
    public static final String MAX_VEHICLE_NUM_PROP = "max_vehicles_num";
    public Random random;

    public List<StreetRoad> streetRoads;
    public HashMap<StreetRoad, Integer> numOfVehicleOnStreet;

    private int maxNumOfVehicles;
    private final int SPAWN_SPEED = 2000; //TODO: check this

    private static int num = 0;
    Properties properties;

    public StreetVehicleSpawner(List<StreetRoad> streetRoads) throws IOException
    {
        setDaemon(true);
        this.streetRoads = streetRoads;
        numOfVehicleOnStreet = new HashMap<>();
        for (StreetRoad streetRoad : streetRoads)
            numOfVehicleOnStreet.put(streetRoad, 0);
        random = new Random();
        properties = new Properties();
        File file = new File("C:\\Users\\filip\\IdeaProjects\\TrainspottingJFX\\src\\main\\resources\\configuration.config");
        readStreetVehiclePropertyFile(file);
    }

    @Override
    public void run()
    {
        startWatcher();
        try
        {
            while (true)
            {
                Thread.sleep(SPAWN_SPEED);
                StreetRoad streetRoad = getRandomStreetRoad();
                if (streetRoad != null)
                {
                    StreetVehicle streetVehicle = createVehicle(streetRoad);
                    streetVehicle.start();
                }
            }
        } catch (Exception ex)
        {
            ex.printStackTrace();
        }


    }

    private void startWatcher()
    {
        new Thread(() ->
        {
            try
            {
                WatchService watcher = FileSystems.getDefault().newWatchService();
                File directory = new File("C:\\Users\\filip\\IdeaProjects\\TrainspottingJFX\\src\\main\\resources\\");
                Path dir = directory.toPath();
                dir.register(watcher, ENTRY_MODIFY);
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
                        if (fileName.toString().trim().equals("configuration.config") && kind.equals(ENTRY_MODIFY))
                        {
                            File file = dir.resolve(fileName).toFile();
                            readStreetVehiclePropertyFile(file);
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
        }).start();
    }


    private synchronized void readStreetVehiclePropertyFile(File file) throws IOException
    {
        try (InputStream inputStream = new FileInputStream(file))
        {
            properties.load(inputStream);
            for (StreetRoad streetRoad : streetRoads)
            {
                int spawnSpeed = Integer.parseInt(properties.getProperty(streetRoad.getName() + SPAWN_SPEED_PROP));
                streetRoad.setSpeed(spawnSpeed);

            }
            int maxVehicles = Integer.parseInt(properties.getProperty(MAX_VEHICLE_NUM_PROP));
            if (maxVehicles > maxNumOfVehicles)
                maxNumOfVehicles = maxVehicles;
        }
    }

    private StreetRoad getRandomStreetRoad()
    {
        boolean flag = true;
        StreetRoad streetRoad = null;
        while (flag)
        {
            flag = checkIfStreetsNotFull();
            if (!flag)
            {
                streetRoad = null;
                break;
            }
            int randomPosition = random.nextInt(streetRoads.size());
            streetRoad = streetRoads.get(randomPosition);
            if (numOfVehicleOnStreet.get(streetRoad) < maxNumOfVehicles)
            {
                int num = numOfVehicleOnStreet.get(streetRoad);
                num++;
                numOfVehicleOnStreet.put(streetRoad, num);
                flag = false;
            }
        }
        return streetRoad;
    }

    private boolean checkIfStreetsNotFull()
    {
        boolean flag = false;
        for (var streetHashMap : numOfVehicleOnStreet.entrySet())
        {
            if (streetHashMap.getValue() < maxNumOfVehicles)
            {
                flag = true;
                break;
            }
        }
        return flag;
    }

    private StreetVehicle createVehicle(StreetRoad streetRoad)
    {
        StreetVehicle streetVehicle = (num % 4 == 0) ? new Truck(streetRoad) : new Car(streetRoad);
        num++;
        return streetVehicle;
    }
}
