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

import ion2d.support.INTypes.*;
import ion2d.support.*;

public class INIntervalAction extends INFiniteAction
{
    protected float elapsed;
    protected boolean firstTick;

    /**
     * Default constructor
     */
    public INIntervalAction()
    {
        this(0.05f);
    }

    /**
     * Secondary constructor
     * @param float duration
     */
    public INIntervalAction(float duration)
    {
        this.duration = duration;
        this.elapsed = 0.0f;

        if(this.duration == 0)
            this.duration = 0.05f;

        this.firstTick = true;
    }

    /**
     * Copy constructor
     * @param INIntervalAction original
     */
    public INIntervalAction(INIntervalAction original)
    {
        if(original == null) throw new NullPointerException();
        
        this.duration = original.duration;
        this.elapsed = original.elapsed;
        this.firstTick = original.firstTick;
        this.selector = original.selector.clone();
        this.tag = original.tag;
    }

    /**
     * Clone override
     * @return INIntervalAction
     */
    public INIntervalAction clone()
    {
        return new INIntervalAction(this);
    }

    /**
     * The actual method the makes the action work
     * @param float time
     */
    public void step(float time)
    {
        if(this.firstTick)
        {
            this.firstTick = false;
            this.elapsed = 0.0f;
        }
        else
        {
            this.elapsed += time;
        }

        float updateTime = (float)Math.min(1.0f, this.elapsed/this.duration);

        this.update(updateTime);
    }

    /**
     * Starts the action with the specified target
     * @param INSelector selector
     */
    public void startWithTarget(INSelector selector)
    {
        super.startWithTarget(selector);
        this.elapsed = 0.0f;
        this.firstTick = true;
    }

    /**
     * Stops the action
     */
    public void stop()
    {
        this.elapsed = this.duration;
        super.stop();
    }

    /**
     * Gets the elapsed time of the action
     * @return float
     */
    public float getElapsed()
    {
        return this.elapsed;
    }

    /**
     * Status of whether the action is done or not
     * @return boolean
     */
    public boolean isDone()
    {
        return (this.elapsed >= this.duration);
    }

    /**
     * Please override me. INIntervalAction does not support this at the
     * Base class
     * @return INIntervalAction
     * @throws Exception
     */
    public INIntervalAction reverse() throws Exception
    {
        throw new Exception("Reverse Action Not Implemented for the base class of CCIntervalAction");
    }
}
