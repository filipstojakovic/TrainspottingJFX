package project.Util;

import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.text.Font;
import project.constants.ColorConstants;

public abstract class LabelUtils
{
    public static final double TEXT_FIELD_WIDTH = 25;
    public static final double TEXT_FIELD_HEIGHT = 25;

    public static Label getLabel(String text, String color)
    {
        Label label = new Label(text);
        label.setMinWidth(TEXT_FIELD_WIDTH);
        label.setMinHeight(TEXT_FIELD_HEIGHT);
        label.setAlignment(Pos.CENTER);
        label.setFont(new Font(10));
        setLableBackgroundAndBorderColor(label, ColorConstants.WHITE);
        label.setStyle("-fx-border-color: black;");

        return label;
    }

    public static Label getLabel()
    {
        return getLabel("");
    }

    public static Label getLabel(String color)
    {
        return getLabel("", color);
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
