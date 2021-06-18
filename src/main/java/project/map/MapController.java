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
import project.Util.GenericLogger;
import project.Util.LabelUtils;
import project.constants.ColorConstants;
import project.constants.Constants;
import project.constants.FieldConstants;
import project.exception.PropertyNotFoundException;
import project.map.Field.*;
import project.spawners.StreetVehicleSpawner;
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
import java.util.logging.Level;

import static project.map.Map.DIM;

public class MapController implements Initializable
{
    public MapModel mapModel;
    @FXML
    private Label infoLabel;
    @FXML
    public GridPane gridPane;
    public static GridPane mapGridPane;

    private HashMap<String, TrainStation> trainStationMap;
    private List<Street> streets;
    private List<RailRoad> railRoads;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle)
    {
        infoLabel.setText(Constants.INFO_TEXT);
        try
        {
            mapModel = new MapModel();
            initializeMap();
            railRoads = mapModel.initRailRoads();
            trainStationMap = mapModel.initializeTrainStationMap(railRoads);
            streets = mapModel.initializeStreets();

        } catch (Exception ex)
        {
            GenericLogger.createLog(this.getClass(), Level.SEVERE, "Unable to load nessery components", ex);
            System.exit(1);
        }
        try
        {
            if (mapModel != null)
                TrainSpawner.trainHistoryDirPath = mapModel.getTrainHistoryPathDir();

        } catch (PropertyNotFoundException ex)
        {
            GenericLogger.createAsyncLog(this.getClass(), Level.WARNING, "Unable to get TrainHistory folder path from popertie file", ex);
        }
    }

    private TrainSpawner trainSpawner;
    private StreetVehicleSpawner streetVehicleSpawner;

    @FXML
    void onStartBtnClicked(ActionEvent event)
    {
        try
        {
            if (trainSpawner != null)
                trainSpawner.close();
            trainSpawner = mapModel.initTrainSpawner(trainStationMap);
            trainSpawner.getAllTrainsFromDirectory();
            trainSpawner.start();
        } catch (URISyntaxException | FileNotFoundException | PropertyNotFoundException | IllegalThreadStateException ex)
        {
            GenericLogger.createAsyncLog(this.getClass(), ex);
            if (trainSpawner != null)
                trainSpawner.close();
        }

        try
        {
            if (streetVehicleSpawner != null)
                streetVehicleSpawner.close();
            streetVehicleSpawner = new StreetVehicleSpawner(streets);
            streetVehicleSpawner.start();
        } catch (IOException | URISyntaxException | PropertyNotFoundException ex)
        {
            GenericLogger.createAsyncLog(this.getClass(), ex);
        }
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
            movementDialogStage.setTitle("Trains history dialog");
            movementDialogStage.setScene(new Scene(root));
            movementDialogStage.show();
            // Hide this current window (if this is what you want)
            //((Node)(event.getSource())).getScene().getWindow().hide();
        } catch (IOException ex)
        {
            GenericLogger.createAsyncLog(this.getClass(), Level.SEVERE, "Unable to open DialogView.fxml", ex);
        }
    }

    public synchronized static Label getGridCell(int x, int y)
    {
        if (x < DIM && x >= 0 && y < DIM && y >= 0)
            return (Label) mapGridPane.getChildren().get(y * DIM + x);

        return null;
    }

    private void initializeMap() throws Exception
    {
        mapGridPane = gridPane;
        //gridPane.setBorder(new Border(new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, null, null)));
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
