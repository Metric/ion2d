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
import ion2d.support.*;

public class INRepeatForever extends INAction
{
    protected INIntervalAction other;

    /**
     * Default constructor
     */
    public INRepeatForever()
    {
        this.other = null;
    }

    /**
     * Secondary constructor
     * @param INIntervalAction action
     */
    public INRepeatForever(INIntervalAction action)
    {
        this.other = action;
    }

    /**
     * Copy Constructor
     * @param INRepeatForever original
     */
    public INRepeatForever(INRepeatForever original)
    {
        if(original == null) throw new NullPointerException();

        this.other = original.other.clone();
        this.selector = original.selector.clone();
        this.tag = original.tag;
    }

    /**
     * Clone Override
     * @return INRepeatForever
     */
    public INRepeatForever clone()
    {
        return new INRepeatForever(this);
    }


    /**
     * Starts the action with the specified target
     * @param INSelector selector
     */
    public void startWithTarget(INSelector selector)
    {
        super.startWithTarget(selector);

        if(this.other == null) return;

        this.other.startWithTarget(this.selector);
    }

    /**
     * Performs the action step
     * @param float time
     */
    public void step(float time)
    {
        if(this.other == null) return;
        
        this.other.step(time);
        if(this.other.isDone())
        {
            float difference = time + this.other.getDuration() - this.other.getElapsed();
            this.other.startWithTarget(this.selector);
            
            this.other.step(difference);
        }
    }

    /**
     * Returns whether the action is complete or not
     * @return boolean
     */
    public boolean isDone()
    {
        return false;
    }

    /**
     * Reverses the action
     * @return INAction
     */
    public INAction reverse() throws Exception
    {
        INRepeatForever reverseRepeat = new INRepeatForever(this.other.reverse());
        reverseRepeat.startWithTarget(this.selector);

        return reverseRepeat;
    }
}
