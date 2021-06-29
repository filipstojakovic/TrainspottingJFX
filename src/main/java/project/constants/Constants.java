package project.constants;

import project.vehiclestuff.trainstuff.ElectricField;
import project.vehiclestuff.trainstuff.locomotive.ManeuverLocomotive;
import project.vehiclestuff.trainstuff.locomotive.PassengerLocomotive;
import project.vehiclestuff.trainstuff.locomotive.UniversalLocomotive;
import project.vehiclestuff.trainstuff.wagon.*;
import project.vehiclestuff.trainstuff.wagon.passengerwagon.PassengerBedWagon;
import project.vehiclestuff.trainstuff.wagon.passengerwagon.PassengerRestaurantWagon;
import project.vehiclestuff.trainstuff.wagon.passengerwagon.PassengerSeatWagon;
import project.vehiclestuff.trainstuff.wagon.passengerwagon.PassengerSleepWagon;

import java.io.File;

public abstract class Constants
{
    public static final String CONFIGURATION_DIR = "." + File.separator + "res";
    public static final String CONFIGURATION_FILE = CONFIGURATION_DIR + File.separator + "config.properties";

    //Properties KEYs
    public static final String TRAIN_HISTORY_DIR_PROP = "train_history";
    public static final String TRAINSTATION_JSON_PROP = "train_stations";
    public static final String TRAINSTATION_MAP_PROP = "trainstation_map";
    public static final String TRAINS_DIR_PROP = "train_dir";
    public static final String RAILROADS_PROP = "railroads";
    public static final String STREETROADS_PROP = "streetroads";

    public static final int MAP_DIM = 30;

    public static final String SPLITTER = " - ";
    public static final String INFO_TEXT =
            PassengerLocomotive.NAME + SPLITTER + "Passenger Locomotive\n" +
                    ManeuverLocomotive.NAME + SPLITTER + "Maneuver Locomotive\n" +
                    UniversalLocomotive.NAME + SPLITTER + "Universal Locomotive\n" +
                    '\n' +
                    PassengerBedWagon.NAME + SPLITTER + "Passenger Bed Wagon\n" +
                    PassengerRestaurantWagon.NAME + SPLITTER + "Passenger Restaurant Wagon\n" +
                    PassengerSeatWagon.NAME + SPLITTER + "Passenger Seat Wagon\n" +
                    PassengerSleepWagon.NAME + SPLITTER + "Passenger Sleep Wagon\n" +
                    CargoWagon.NAME + SPLITTER + "Cargo Wagon\n" +
                    SpecialUseWagon.NAME + SPLITTER + "Special Use Wagon\n" +
                    '\n' +
                    ElectricField.THUNDER + SPLITTER + "Field has electricity";

    public static final String DEFAULT_CONFIG_FILE = """
            left_street_speed=2000
            left_street_num_of_cars=5
            middle_street_speed=2000
            middle_street_num_of_cars=5
            right_street_speed=2000
            right_street_num_of_cars=5
            train_history=./res/train_history
            trainstation_map=./res/mapstuff/trainstation_map.txt
            train_stations=./res/mapstuff/train_stations.json
            train_dir=./res/trains
            railroads=./res/mapstuff/roads/railroads.json
            streetroads=./res/mapstuff/roads/streetroads.json
            """;

}
