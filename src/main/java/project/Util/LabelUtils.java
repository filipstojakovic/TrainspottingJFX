package project.Util;

import javafx.geometry.Pos;
import javafx.scene.control.Label;
import project.constants.ColorConstants;

public class LabelUtils
{
    public static final double TEXT_FIELD_WIDTH = 25.0;
    public static final double TEXT_FIELD_HEIGHT = 25.0;

    public static Label getLabel(String color)
    {
        return getLabel("", color);
    }

    public static Label getLabel(String text, String color)
    {
        Label label = new Label(text);
        label.setPrefWidth(TEXT_FIELD_WIDTH);
        label.setPrefHeight(TEXT_FIELD_HEIGHT);
        label.setAlignment(Pos.CENTER);
        setLableBackgroundAndBorderColor(label, ColorConstants.WHITE);
        label.setStyle("-fx-border-color: black;");

        return label;
    }

    public static void setLabelBackGroundColor(Label label, String color)
    {
        label.setStyle("-fx-background-color: " + color);
    }

    public static void setLableBackgroundAndBorderColor(Label label, String color)
    {
        label.setStyle("-fx-background-color: " + color + ";"
                + "-fx-border-color: black;"
        );
    }
}
