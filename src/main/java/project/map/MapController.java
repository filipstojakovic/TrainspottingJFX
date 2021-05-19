package project.map;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import project.FieldEnum;
import project.Util.LabelUtils;
import project.constants.ColorConstants;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class MapController implements Initializable
{
    private MapModel mapModel;
    @FXML
    private GridPane gridPane;

    public static final int DIM = 30;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle)
    {
        mapModel = new project.map.MapModel();
        List<List<String>> mapValues = new ArrayList<>();
        try
        {
            mapValues = mapModel.getMap();

        } catch (Exception ex)
        {
            ex.printStackTrace();
        }

        int i = 0;
        for (List<String> rowList : mapValues)
        {
            int j = 0;
            for (String fieldValue : rowList)
            {
                Label fieldLabl = LabelUtils.getLabel("");
                switch (fieldValue)
                {
                    case FieldEnum.RAMP:
                        LabelUtils.setLableBackgroundAndBorderColor(fieldLabl, ColorConstants.BLACK);
                        break;
                    case FieldEnum.STREET:
                        LabelUtils.setLableBackgroundAndBorderColor(fieldLabl, ColorConstants.BLUE);
                        break;

                    case FieldEnum.REILS:
                        LabelUtils.setLableBackgroundAndBorderColor(fieldLabl, ColorConstants.GRAY);
                        break;
                    case FieldEnum.TRAINSTATION:
                        LabelUtils.setLabelBackGroundColor(fieldLabl, ColorConstants.GRAY);
                }
                gridPane.add(fieldLabl, j, i);
                j++;
            }
            i++;
        }
    }


}
