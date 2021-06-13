package project.exception;

public class PropertyNotFoundException extends Exception
{
    public static final String MSG = "Property was not found";

    public PropertyNotFoundException()
    {
        super(MSG);
    }

    public PropertyNotFoundException(String msg)
    {
        super(MSG + ": (" + msg + ")");
    }
}
