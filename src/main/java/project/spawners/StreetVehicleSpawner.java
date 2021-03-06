package project.spawners;

import project.Util.GenericLogger;
import project.Util.Utils;
import project.constants.Constants;
import project.exception.PropertyNotFoundException;
import project.vehiclestuff.streetstuff.Street;
import project.vehiclestuff.streetstuff.StreetRoad;
import project.vehiclestuff.streetstuff.streetvehicle.Car;
import project.vehiclestuff.streetstuff.streetvehicle.StreetVehicle;
import project.vehiclestuff.streetstuff.streetvehicle.Truck;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.*;
import java.util.HashMap;
import java.util.List;
import java.util.Properties;
import java.util.Random;
import java.util.stream.Collectors;

import static java.nio.file.StandardWatchEventKinds.ENTRY_MODIFY;

public class StreetVehicleSpawner extends Thread
{
    public static final int MINOR_DELAY = 200; // for watcher bug
    private static final int SPAWN_SPEED = 2000;
    private static final String MOVE_SPEED_PROP = "_speed";
    private static final String VEHICLE_NUM_PROP = "_num_of_cars";
    private final Random random;

    private final List<Street> streets;
    private final HashMap<String, Integer> streetNumOfVehiclesMap; // streetname : vehicle_counter

    Properties properties;
    private boolean isActive;
    private boolean isWatcherActive;

    public StreetVehicleSpawner(List<Street> streets) throws IOException, URISyntaxException, PropertyNotFoundException
    {
        setDaemon(true);
        this.streets = streets;
        streetNumOfVehiclesMap = new HashMap<>();
        for (var street : streets)
            streetNumOfVehiclesMap.put(street.getStreetName(), 0);

        random = new Random();
        properties = Utils.loadPropertie(Constants.CONFIGURATION_FILE);
        readStreetVehiclePropertyFile();
    }

    private void readStreetVehiclePropertyFile() throws PropertyNotFoundException, ClassCastException
    {
        for (var street : streets)
        {
            synchronized (this)
            {
                String streetSpeedProp = properties.getProperty(street.getStreetName() + MOVE_SPEED_PROP);
                if (streetSpeedProp == null)
                    throw new PropertyNotFoundException(street.getStreetName() + MOVE_SPEED_PROP);
                int streetSpeed = Integer.parseInt(streetSpeedProp); // can throw ClassCastException
                street.setSpeedLimit(streetSpeed);

                String maxNumOfVehicleProp = properties.getProperty(street.getStreetName() + VEHICLE_NUM_PROP);
                if (maxNumOfVehicleProp == null)
                    throw new PropertyNotFoundException(street.getStreetName() + VEHICLE_NUM_PROP);
                int propertieMaxNumOfVehicles = Integer.parseInt(maxNumOfVehicleProp);
                int currentMaxNumOfVehicles = street.getMaxNumOfVehicles();
                if (propertieMaxNumOfVehicles > currentMaxNumOfVehicles)
                {
                    street.setMaxNumOfVehicles(propertieMaxNumOfVehicles); // set max num of vehicles on street
                    notify();
                }
            }
        }
    }

    @Override
    public void run()
    {
        startWatcher();
        try
        {
            isActive = true;
            while (isActive)
            {
                List<Street> streetsThatAreNotFull = getNotFullStreets();
                if (streetsThatAreNotFull == null || streetsThatAreNotFull.isEmpty())
                {
                    synchronized (this)
                    {
                        System.out.println("All streets are full");
                        wait();
                        System.out.println("Adding more street vehicles");
                    }
                    continue;
                }
                Street streetThatIsNotFull = streetsThatAreNotFull.get(random.nextInt(streetsThatAreNotFull.size()));
                StreetVehicle streetVehicle = createVehicle(streetThatIsNotFull);
                synchronized (this)
                {
                    int numOfVehicles = streetNumOfVehiclesMap.get(streetThatIsNotFull.getStreetName());
                    numOfVehicles++;
                    streetNumOfVehiclesMap.put(streetThatIsNotFull.getStreetName(), numOfVehicles);
                }
                streetVehicle.start();
                Thread.sleep(SPAWN_SPEED);
            }

            System.out.println("Closing StreetVehicleSpawner thread");
        } catch (InterruptedException ex)
        {
            GenericLogger.createAsyncLog(this.getClass(), ex);
        }
    }

    private List<Street> getNotFullStreets()
    {
        return streets.stream()
                .filter(street -> streetNumOfVehiclesMap.get(street.getStreetName()) < street.getMaxNumOfVehicles())
                .collect(Collectors.toList());
    }

    //watching folder "./res" and checking config.properties for modification
    private void startWatcher()
    {
        new Thread(() ->
        {
            try
            {
                WatchService watcher = FileSystems.getDefault().newWatchService();
                File configPropFile = new File(Constants.CONFIGURATION_FILE);
                if (!configPropFile.exists())
                    throw new FileNotFoundException(Constants.CONFIGURATION_FILE + " file not found");
                File directory = configPropFile.getParentFile();
                Path dir = directory.toPath();
                dir.register(watcher, ENTRY_MODIFY);
                //System.out.println("Watch Service registered for dir: " + directory.getAbsolutePath());
                isWatcherActive = true;
                while (isWatcherActive)
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
                        if (!isWatcherActive)
                            break;
                        WatchEvent.Kind<?> kind = event.kind();
                        WatchEvent<Path> ev = (WatchEvent<Path>) event;
                        Path fileName = ev.context();
                        if ("config.properties".equals(fileName.toString().trim())
                                && kind.equals(ENTRY_MODIFY))
                        {
                            try
                            {
                                Thread.sleep(MINOR_DELAY); // becouse of bugs
                            } catch (InterruptedException ex)
                            {
                                GenericLogger.createAsyncLog(this.getClass(), ex);
                            }
                            System.out.println(kind.name() + ": " + "config.properties");
                            properties = Utils.loadPropertie(Constants.CONFIGURATION_FILE);
                            readStreetVehiclePropertyFile();
                            break;
                        }
                    }

                    boolean valid = key.reset();
                    if (!valid)
                        break;
                }
                System.out.println("Closing " + directory.getName() + " file watcher");
            } catch (IOException | URISyntaxException | PropertyNotFoundException ex)
            {
                GenericLogger.createAsyncLog(this.getClass(), ex);
            }
        }).start();
    }

    private static long num = 0;
    private StreetVehicle createVehicle(Street streetThatIsNotFull)
    {
        int speedLimit = streetThatIsNotFull.getSpeedLimit();
        StreetRoad streetRoad = streetThatIsNotFull.getRoads().get(random.nextInt(streetThatIsNotFull.getRoads().size()));
        StreetVehicle streetVehicle = (num % 4 == 0) ?
                new Truck(streetRoad, speedLimit) : new Car(streetRoad, speedLimit);
        num++;
        return streetVehicle;
    }

    public void close()
    {
        synchronized (this)
        {
            notify();
        }
        isActive = false;
        isWatcherActive = false;
    }
}
