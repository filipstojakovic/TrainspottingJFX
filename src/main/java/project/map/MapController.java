package project.map;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import project.Util.LabelUtils;
import project.constants.ColorConstants;
import project.constants.FieldConstants;
import project.jsonparsers.RailRoadJsonParser;
import project.map.Field.*;
import project.streetstuff.StreetRoad;
import project.trainstuff.RailRoad;
import project.trainstuff.trainstation.TrainStation;
import project.spawners_and_watchers.StreetVehicleSpawner;
import project.spawners_and_watchers.TrainSpawner;

import java.io.FileNotFoundException;
import java.io.IOException;
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


        //streetVehicleSpawner.start();
    }

    @FXML
    void onStartBtnClicked(ActionEvent event)
    {
        TrainSpawner trainSpawner = new TrainSpawner("C:\\Users\\filip\\IdeaProjects\\TrainspottingJFX\\src\\main\\resources\\trains", trainStationMap);
        try
        {
            trainSpawner.getAllTrainsFromDirectory();
        } catch (FileNotFoundException e)
        {
            e.printStackTrace();
        }
        trainSpawner.start();

        StreetVehicleSpawner streetVehicleSpawner = null;
        try
        {
            streetVehicleSpawner = new StreetVehicleSpawner(streetRoads);
            streetVehicleSpawner.start();
        } catch (IOException exception)
        {
            exception.printStackTrace();
        }
    }


    private Stage dialogStage;
    private DialogController dialogController;

    @FXML
    void openDialogClicked(ActionEvent event)
    {
        try
        {
            if (dialogStage != null) //close last if its open
            {
                System.out.println("Closed becouse reopened!");
                dialogController.close();
                dialogStage.close();
            }

            FXMLLoader loader = new FXMLLoader(MapController.class.getClassLoader().getResource("./DialogView.fxml"));
            Parent root = (Parent) loader.load();

            dialogStage = new Stage();
            dialogController = loader.getController();
            dialogStage.setOnCloseRequest(x ->
            {
                System.out.println("Closed, on \"X\" clicked");
                dialogController.close();
            });
            dialogStage.setTitle("Movement information");
            dialogStage.setScene(new Scene(root));
            dialogStage.show();
            // Hide this current window (if this is what you want)
            //((Node)(event.getSource())).getScene().getWindow().hide();
        } catch (IOException e)
        {
            e.printStackTrace();
        }
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
