package project.map;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import project.Util.LabelUtils;
import project.constants.ColorConstants;
import project.constants.FieldConstants;
import project.map.Field.*;
import project.trainstuff.trainstation.TrainStation;
import project.trainstuff.Train;
import project.jsonparsers.TrainJsonParser;

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


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle)
    {
        mapModel = new project.map.MapModel();
        initializeMap();
        trainStationMap = mapModel.initializeTrainLines();
    }

    @FXML
    void onStartBtnClicked(ActionEvent event)
    {
        for (var trainStration : trainStationMap.entrySet())
        {
            trainStration.getValue().start();
        }
        Train train = TrainJsonParser.getTrainPartsFromJson(trainStationMap, "nesto"); //TODO: sredi ovaj file path, vjerovatno sa file watcherom
        train.start();
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
