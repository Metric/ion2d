/**
 * ion2d
 *
 * Copyright (C) 2010 Vantage Technic
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the 'ion2d' license.
 *
 * You will find a copy of this license within the ion2d
 * distribution inside the "LICENSE" file.
 */
package ion2d.actions;

import ion2d.*;

public class INSpeed extends INAction
{
    protected INIntervalAction other;
    protected float speed;

    /**
     * Default Constructor
     */
    public INSpeed()
    {
        this.other = null;
    }

    /**
     * Secondary Constructor
     * @param INIntervalAction action
     * @param float rate
     */
    public INSpeed(INIntervalAction action, float rate)
    {
        this.other = action;
        this.speed = rate;
    }

    /**
     * Copy Constructor
     * @param INSpeed original
     */
    public INSpeed(INSpeed original)
    {
        if(original == null) throw new NullPointerException();

        this.other = original.other.clone();
        this.speed = original.speed;
        this.selector = original.selector.clone();
        this.tag = original.tag;
    }

    /**
     * Clone Override
     * @return INSpeed
     */
    public INSpeed clone()
    {
        return new INSpeed(this);
    }

    /**
     * Performs the action step
     * @param float time
     */
    public void step(float time)
    {
        if(this.other == null) return;

        this.other.step(time * this.speed);
    }

    /**
     * Returns whether the action is done or not
     * @return boolean
     */
    public boolean isDone()
    {
        if(this.other == null) return true;

        return this.other.isDone();
    }

    /**
     * Reverses the action
     * @return INAction
     * @throws Exception
     */
    public INAction reverse() throws Exception
    {
        if(this.other == null) return null;

        return new INSpeed(this.other.reverse(), this.speed);
    }
}
