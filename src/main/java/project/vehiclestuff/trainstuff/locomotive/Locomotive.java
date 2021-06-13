package project.vehiclestuff.trainstuff.locomotive;

import project.vehiclestuff.trainstuff.TrainPart;

public abstract class Locomotive extends TrainPart
{
    //ABSTRACT
    protected Double power;
    protected String signature;
    protected EngineEnum engine;

    public Locomotive()
    {
    }

    public Double getPower()
    {
        return power;
    }

    public void setPower(Double power)
    {
        this.power = power;
    }

    public String getSignature()
    {
        return signature;
    }

    public void setSignature(String signature)
    {
        this.signature = signature;
    }

    public EngineEnum getEngine()
    {
        return engine;
    }

    public void setEngine(EngineEnum engine)
    {
        this.engine = engine;
    }
}