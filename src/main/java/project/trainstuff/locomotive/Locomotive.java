package project.trainstuff.locomotive;

import project.streetvehicle.IMoveable;
import project.trainstuff.TrainPart;

public abstract class Locomotive extends TrainPart implements IMoveable
{
    //ABSTRACT
    public Double power;
    public String signature;

    public Locomotive()
    {
    }


}