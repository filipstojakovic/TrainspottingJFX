package project.trainstuff.wagon;

import project.streetvehicle.IMoveable;
import project.trainstuff.TrainPart;

public abstract class Wagon extends TrainPart implements IMoveable
{
    //ABSTRACT
    protected String signature;
    protected double length;

    public Wagon()
    {
    }


}