package project.constants;

public abstract class Constants
{
    public static final String CONFIGURATION_FILE = "config.properties";

    //Properties KEYs
    public static final String SAVED_TRAINS_DIR_PROP = "saved_trains";
    public static final String TRAINSTATION_JSON_PROP = "train_stations";
    public static final String TRAINSTATION_MAP_PROP = "trainstation_map";
    public static final String TRAINS_DIR_PROP = "train_dir";

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


}
