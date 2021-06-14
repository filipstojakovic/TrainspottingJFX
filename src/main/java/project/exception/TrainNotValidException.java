package project.exception;

public class TrainNotValidException extends Exception
{
    public static final String MSG = "Train does not have valid parts construction";

    public TrainNotValidException()
    {
        super(MSG);
    }

    public TrainNotValidException(String msg)
    {
        super(MSG + ": " + msg);
    }
}
