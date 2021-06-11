package project.exception;

public class TrainWithoutPartsException extends Exception
{
    public static final String MSG = "Train has now TrainParts";

    public TrainWithoutPartsException()
    {
        super(MSG);
    }

    public TrainWithoutPartsException(String msg)
    {
        super(msg);
    }
}
