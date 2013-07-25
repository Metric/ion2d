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

import ion2d.support.*;

public class INRepeat extends INIntervalAction
{
    protected INFiniteAction other;
    protected int times;
    protected int total;

    /**
     * Default constructor
     */
    public INRepeat()
    {
        this.other = null;
        this.times = 0;
        this.total = 0;
    }

    /**
     * Secondary Constructor
     * @param INFiniteAction action
     * @param int times
     */
    public INRepeat(INFiniteAction action, int times)
    {
        this.duration = action.getDuration() * times;
        this.times = times;
        this.other = action;
    }

    /**
     * Copy Constructor
     * @param INRepeat original
     */
    public INRepeat(INRepeat original)
    {
        if(original == null) throw new NullPointerException();

        this.duration = original.duration;
        this.other = (INFiniteAction)original.other.clone();
        this.firstTick = original.firstTick;
        this.selector = original.selector.clone();
        this.tag = original.tag;
        this.times = original.times;
        this.total = original.total;
    }

    /**
     * Clone Override
     * @return INRepeat
     */
    public INRepeat clone()
    {
        return new INRepeat(this);
    }

    /**
     * Starts the action with the specified target
     * @param INSelector selector
     */
    public void startWithTarget(INSelector selector)
    {
        this.total = 0;
        super.startWithTarget(selector);
        this.other.startWithTarget(selector);
    }

    /**
     * Stops the action
     */
    public void stop()
    {
        this.other.stop();
        super.stop();
    }

    /**
     * Updates the action
     * @param float time
     */
    public void update(float time)
    {
        float timeOffset = time * this.times;

        if(timeOffset > this.total + 1 )
        {
            this.other.update(1.0f);
            this.total++;
            this.other.stop();
            this.other.startWithTarget(this.selector);

            if(this.total == this.times)
            {
                this.other.update(0);
            }
            else
            {
                this.other.update(timeOffset - this.total);
            }
        }
        else
        {
            float recentOffset = timeOffset % 1.0f;

            if(time == 1.0f)
            {
                recentOffset = 1.0f;
                this.total++;
            }

            this.other.update(Math.min(recentOffset, 1));
        }
    }

    /**
     * Returns whether the action is complete or not
     * @return boolean
     */
    public boolean isDone()
    {
        return (this.total == this.times);
    }

    /**
     * Reverses the action
     * @return INIntervalAction
     */
    public INIntervalAction reverse()
    {
        try
        {
            return new INRepeat(this.other.reverse(), this.times);
        } catch (Exception e)
        {
            System.out.println(e.getMessage());
            e.printStackTrace();
            return null;
        }
    }
}
