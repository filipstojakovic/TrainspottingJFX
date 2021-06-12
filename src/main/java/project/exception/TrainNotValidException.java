package project.exception;

public class TrainNotValidException extends Exception
{
    public static final String MSG = "Train has not valid construction";

    public TrainNotValidException()
    {
        super(MSG);
    }

    public TrainNotValidException(String msg)
    {
        super(msg);
    }
}
