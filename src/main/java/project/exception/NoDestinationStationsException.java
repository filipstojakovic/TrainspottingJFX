package project.exception;

public class NoDestinationStationsException extends Exception
{
    public static final String MSG = "Train has no destination stations";

    public NoDestinationStationsException()
    {
        super(MSG);
    }

    public NoDestinationStationsException(String msg)
    {
        super(msg);
    }
}