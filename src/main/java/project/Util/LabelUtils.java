package project.Util;

import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import project.constants.ColorConstants;

public abstract class LabelUtils
{
    //Note: never use label.setStyle!!!!
    public static final double TEXT_FIELD_WIDTH = 24;
    public static final double TEXT_FIELD_HEIGHT = 24;
    public static final int FONT_SIZE = 10;

    public static Label createLabel(String text, String color)
    {
        Label label = new Label(text);
        label.setAlignment(Pos.CENTER);
        setLableBackgroundAndBorderColor(label, color);
        label.setMinWidth(TEXT_FIELD_WIDTH);
        label.setMinHeight(TEXT_FIELD_HEIGHT);
        label.setFont(Font.font(null, FontWeight.BOLD, FONT_SIZE));

        return label;
    }

    public static Label createLabel()
    {
        return createLabel(ColorConstants.WHITE);
    }

    public static Label createLabel(String color)
    {
        return createLabel("", color);
    }

    public static void setLabelBackGroundColor(Label label, String color)
    {
        label.setBackground(new Background(new BackgroundFill(Paint.valueOf(color), null, null)));
        label.setBorder(null);
    }

    public static void setLableBackgroundAndBorderColor(Label label, String color)
    {
        label.setBorder(new Border(new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, null, null)));
        label.setBackground(new Background(new BackgroundFill(Paint.valueOf(color), null, null)));
    }
}
