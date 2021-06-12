package project.constants;

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

    public static final String INFO_TEXT = """
            PL - Passenger Locomotive
            CL - Cargo Locomotive
            ML - Maneuver Locomotive
            UL - Universal Locomotive
                        
            PBW - Passenger Bad Wagon
            PRW - Passenger Restaurant Wagon
            PSW - Passenger Seat Wagon
            PSS - Passenger Sleep Wagon
            SUW - Special Use Wagon
            """;

    public static final String DEFAULT_CONFIG_FILE = """
            left_street_speed=2000
            left_street_num_of_cars=3
            middle_street_speed=2000
            middle_street_num_of_cars=3
            right_street_speed=200
            right_street_num_of_cars=5
            train_history=./res/train_history
            trainstation_map=./res/mapstuff/trainstation_map.txt
            train_stations=./res/mapstuff/train_stations.json
            train_dir=./res/trains
            railroads=./res/mapstuff/roads/railroads.json
            streetroads=./res/mapstuff/roads/streetroads.json
            """;

}
