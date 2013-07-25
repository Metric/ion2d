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

package ion2d;

import ion2d.support.*;
import ion2d.support.INTypes.*;

public class INTimer
{
    protected INSelector selector;
    protected float interval;
    protected float elapsed;

    /**
     * Default constructor
     */
    public INTimer()
    {
        this(null, 0);
    }

    /**
     * Secondary Constructor
     * @param INSelector selector
     */
    public INTimer(INSelector selector)
    {
        this(selector, INTypes.FLT_EPSILON);
    }

    /**
     * Third Constructor
     * @param INSelector selector
     * @param float interval
     */
    public INTimer(INSelector selector, float interval)
    {
        if(selector == null) throw new NullPointerException();

        this.selector = selector;
        this.interval = interval;
        this.elapsed = -1.0f;
    }

    /**
     * Copy constructor
     * @param INTimer original
     */
    public INTimer(INTimer original)
    {
        if(original == null) throw new NullPointerException();

        this.selector = original.selector;
        this.interval = original.interval;
        this.elapsed = original.elapsed;
    }

    /**
     * Clone Override
     * @return INTimer
     */
    public INTimer clone()
    {
        return new INTimer(this);
    }

    /**
     * Call this to update the elapsed time
     * @param float time
     */
    public void fire(float time)
    {
        if(this.elapsed == -1)
        {
            this.elapsed = 0;
        }
        else
        {
            this.elapsed += time;
        }

        if(this.elapsed >= this.interval)
        {
            try
            {
                this.selector.selector.invoke(this.selector.target, this.elapsed);
            } catch (Exception e)
            {
                System.out.println(e.getMessage());
                e.printStackTrace();
            }
            this.elapsed = 0.0f;
        }
    }
}
