package project.map;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import project.Util.LabelUtils;
import project.constants.ColorConstants;
import project.constants.FieldConstants;
import project.jsonparsers.RailRoadJsonParser;
import project.jsonparsers.TrainJsonParser;
import project.map.Field.*;
import project.streetstuff.StreetRoad;
import project.streetstuff.streetvehicle.Car;
import project.streetstuff.streetvehicle.Truck;
import project.trainstuff.RailRoad;
import project.trainstuff.Train;
import project.trainstuff.trainstation.TrainStation;

import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.ResourceBundle;

import static project.map.Map.DIM;

public class MapController implements Initializable
{
    public MapModel mapModel;
    @FXML
    public GridPane gridPane;
    public static GridPane mapPane;

    private HashMap<String, TrainStation> trainStationMap;
    private List<StreetRoad> streetRoads;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle)
    {
        mapModel = new project.map.MapModel();
        initializeMap();
        List<RailRoad> railRoads = RailRoadJsonParser.getRailRoadListFromJson("./src/main/resources/roads/railroads.json"); //TODO: sredi putanju

        new RampWatcher(railRoads).start();

        trainStationMap = mapModel.initializeRailRoads(railRoads);
        streetRoads = mapModel.initializeStreetRoads();

    }

    @FXML
    void onStartBtnClicked(ActionEvent event)
    {
        Train train = TrainJsonParser.getTrainPartsFromJson(trainStationMap, "nesto"); //TODO: sredi ovaj file path, vjerovatno sa file watcherom
        train.start();

        //((RampField) Map.getField(13, 6)).setClosed(true);

        int i = 0;
        for (StreetRoad streetRoad : streetRoads)
        {
            if (i % 2 == 0)
                new Car(streetRoad).start();
            else
                new Truck(streetRoad).start();
            i++;
        }
    }


    boolean closed = true;
    @FXML
    void onChangeRampStatusClicked(ActionEvent event)
    {
        ((Button)event.getSource()).setText((closed?"spustene":"podignute") + " rampe");
        trainStationMap.entrySet().forEach(x -> x.getValue().getTrainRailRoads().forEach(y -> y.getRamps().forEach(z -> z.setClosed(closed))));
        closed = !closed;
    }


    public static Label getGridCell(int x, int y)
    {
        if (x < DIM && x >= 0 && y < DIM && y >= 0)
            return (Label) mapPane.getChildren().get(y * DIM + x);
        return new Label();
    }

    private void initializeMap()
    {
        mapPane = gridPane;
        List<List<String>> mapValues = new ArrayList<>();
        try
        {
            mapValues = mapModel.getMap();

        } catch (Exception ex)
        {
            ex.printStackTrace();
        }
        Map.mapFields = new ArrayList<>();
        int i = 0;
        for (List<String> rowList : mapValues)
        {
            int j = 0;
            List<Field> rowFields = new ArrayList<>();
            for (String fieldValue : rowList)
            {
                Label fieldLabl = LabelUtils.getLabel();
                switch (fieldValue)
                {
                    case FieldConstants.RAMP:
                        LabelUtils.setLableBackgroundAndBorderColor(fieldLabl, ColorConstants.BLACK);
                        rowFields.add(new RampField(j, i));
                        break;
                    case FieldConstants.STREET:
                        LabelUtils.setLableBackgroundAndBorderColor(fieldLabl, ColorConstants.BLUE);
                        rowFields.add(new StreetField(j, i));
                        break;

                    case FieldConstants.RAILS:
                        LabelUtils.setLableBackgroundAndBorderColor(fieldLabl, ColorConstants.GRAY);
                        rowFields.add(new RailField(j, i));
                        break;
                    case FieldConstants.TRAINSTATION:
                        LabelUtils.setLabelBackGroundColor(fieldLabl, ColorConstants.GRAY);
                        rowFields.add(new TrainStationField(j, i));
                        break;
                    default:
                        //rowFields.add(new Field(j, i));
                        rowFields.add(null);
                }
                gridPane.add(fieldLabl, j, i);
                j++;
            }
            Map.mapFields.add(rowFields);
            i++;
        }
    }
}
