package project.exception;

public class UnreachableStationException extends Exception
{
    public static final String MSG = "Unreachable station exception";

    public UnreachableStationException()
    {
        super(MSG);
    }

    public UnreachableStationException(String msg)
    {
        super(msg);
    }
}
