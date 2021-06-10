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
import project.spawners.TrainSpawner;
import project.vehiclestuff.streetstuff.Street;
import project.vehiclestuff.trainstuff.RailRoad;
import project.vehiclestuff.trainstuff.trainstation.TrainStation;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URISyntaxException;
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
    private List<Street> streets;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle)
    {
        try
        {
            mapModel = new MapModel();
            initializeMap();
            List<RailRoad> railRoads = mapModel.initRailRoads();
            new RampWatcher(railRoads).start();
            trainStationMap = mapModel.initializeTrainStationMap(railRoads);
            streets = mapModel.initializeStreets();

        } catch (Exception ex)
        {
            ex.printStackTrace();
            System.exit(1);
        }
    }

    @FXML
    void onStartBtnClicked(ActionEvent event)
    {
        try
        {
            TrainSpawner trainSpawner = mapModel.initTrainSpawner(trainStationMap);
            trainSpawner.getAllTrainsFromDirectory();
            trainSpawner.start();
        } catch (FileNotFoundException | URISyntaxException e)
        {
            e.printStackTrace();
        }

        //StreetVehicleSpawner streetVehicleSpawner = null;
        //try
        //{
        //    streetVehicleSpawner = new StreetVehicleSpawner(streets);
        //    streetVehicleSpawner.start();
        //} catch (IOException exception)
        //{
        //    exception.printStackTrace();
        //}
    }


    private Stage movementDialogStage;

    @FXML
    void openDialogClicked(ActionEvent event)
    {
        try
        {
            if (movementDialogStage != null) //close last if its open
            {
                System.out.println("Closed becouse reopened!");
                movementDialogStage.close();
            }

            FXMLLoader loader = new FXMLLoader(MapController.class.getClassLoader().getResource("./fxmls/DialogView.fxml"));
            Parent root = (Parent) loader.load();

            movementDialogStage = new Stage();
            movementDialogStage.setTitle("Movement information");
            movementDialogStage.setScene(new Scene(root));
            movementDialogStage.show();
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
                Label fieldLabl = LabelUtils.createLabel();
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
