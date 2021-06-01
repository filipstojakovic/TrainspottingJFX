package project.map;

import project.map.Field.Field;
import project.map.Field.RampField;
import project.map.Field.TrainStationField;

import java.util.List;

public abstract class Map
{
    public static final int DIM = 30;
    public static List<List<Field>> mapFields;

    public static Field getField(int x, int y)
    {
        if (x < DIM && x >= 0 && y >= 0 && y < DIM)
            return mapFields.get(y).get(x);
        return null;
    }

    public static Field getUpField(int x, int y)
    {
        return getField(x, y - 1);
    }

    public static Field getLeftField(int x, int y)
    {
        return getField(x - 1, y);
    }

    public static Field getRightField(int x, int y)
    {
        return getField(x + 1, y);
    }

    public static Field getBottomField(int x, int y)
    {
        return getField(x, y + 1);
    }

    //find next field starting from current x and y position that is of type fieldType, that isn't previous field
    public static Field getNextField(int currentX, int currentY, int previousX, int previousY, Class<? extends Field> fieldType)
    {
        return getNextField(getField(currentX, currentY), getField(previousX, previousY), fieldType);
    }

    public static Field getNextField(Field currentField, Field prevousField, Class<? extends Field> fieldType)
    {
        if (currentField == null)
            return null;
        int x = currentField.getxPosition();
        int y = currentField.getyPosition();
        Field field;
        field = getUpField(x, y);
        if (isValidField(field, prevousField, fieldType))
            return field;
        field = getLeftField(x, y);
        if (isValidField(field, prevousField, fieldType))
            return field;
        field = getRightField(x, y);
        if (isValidField(field, prevousField, fieldType))
            return field;
        field = getBottomField(x, y);
        if (isValidField(field, prevousField, fieldType))
            return field;

        return null;
    }

    public static boolean isValidField(Field field, Field previousField, Class<? extends Field> fieldType)
    {
        return (field != null && !field.equals(previousField) &&
                (field.getClass() == fieldType || field.getClass() == RampField.class || field.getClass() == TrainStationField.class));
    }
}
