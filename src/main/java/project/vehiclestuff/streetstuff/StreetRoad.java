package project.vehiclestuff.streetstuff;

import project.map.Field.Field;

import java.util.ArrayList;
import java.util.List;

public class StreetRoad
{
    private String name;

    private final List<Field> streetFields;

    public StreetRoad()
    {
        streetFields = new ArrayList<>();
    }

    public void addStreetField(Field field)
    {
        streetFields.add(field);
    }

    public List<Field> getStreetFields()
    {
        return streetFields;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public String getName()
    {
        return name;
    }
}
