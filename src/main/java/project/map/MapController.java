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
import project.map.Field.*;
import project.movementdialog.MovementDialogController;
import project.spawners.StreetVehicleSpawner;
import project.spawners.TrainSpawner;
import project.vehiclestuff.streetstuff.StreetRoad;
import project.vehiclestuff.trainstuff.RailRoad;
import project.vehiclestuff.trainstuff.trainstation.TrainStation;

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
        mapModel = new MapModel();
        try
        {
            initializeMap();
            List<RailRoad> railRoads = mapModel.initRailRoads();
            new RampWatcher(railRoads).start();
            trainStationMap = mapModel.initializeTrainStationMap(railRoads);
            streetRoads = mapModel.initializeStreetRoads();
        } catch (Exception ex)
        {
            ex.printStackTrace();
            System.exit(1);
        }
    }

    @FXML
    void onStartBtnClicked(ActionEvent event)
    {
        TrainSpawner trainSpawner = mapModel.initTrainSpawner(trainStationMap);
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
    private MovementDialogController movementDialogController;

    @FXML
    void openDialogClicked(ActionEvent event)
    {
        try
        {
            if (dialogStage != null) //close last if its open
            {
                System.out.println("Closed becouse reopened!");
                movementDialogController.close();
                dialogStage.close();
            }

            FXMLLoader loader = new FXMLLoader(MapController.class.getClassLoader().getResource("./fxmls/DialogView.fxml"));
            Parent root = (Parent) loader.load();

            dialogStage = new Stage();
            movementDialogController = loader.getController();
            dialogStage.setOnCloseRequest(x ->
            {
                System.out.println("Closed, on \"X\" clicked");
                movementDialogController.close();
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

    public synchronized static Label getGridCell(int x, int y)
    {
        if (x < DIM && x >= 0 && y < DIM && y >= 0)
            return (Label) mapPane.getChildren().get(y * DIM + x);

        return new Label();     //TODO: change to null
    }

    private void initializeMap() throws Exception
    {
        mapPane = gridPane;
        List<List<String>> mapValues = mapModel.getMap();

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
