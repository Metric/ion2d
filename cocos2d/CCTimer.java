/**
 * cocos2d for Java
 *
 * Copyright (C) 2010 Vantage Technic
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the 'cocos2d for Java' license.
 *
 * You will find a copy of this license within the cocos2d for Java
 * distribution inside the "LICENSE" file.
 */

package cocos2d;

import cocos2d.support.*;
import cocos2d.support.CCTypes.*;

public class CCTimer
{
    protected CCSelector selector;
    protected float interval;
    protected float elapsed;

    public CCTimer()
    {
        this(null, 0);
    }

    public CCTimer(CCSelector selector)
    {
        this(selector, CCTypes.FLT_EPSILON);
    }

    public CCTimer(CCSelector selector, float interval)
    {
        if(selector == null) throw new NullPointerException();

        this.selector = selector;
        this.interval = interval;
        this.elapsed = -1.0f;
    }

    public CCTimer(CCTimer original)
    {
        if(original == null) throw new NullPointerException();

        this.selector = original.selector;
        this.interval = original.interval;
        this.elapsed = original.elapsed;
    }

    public CCTimer clone()
    {
        return new CCTimer(this);
    }

    /**
     * Call this to updated the elapsed time
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
            }
            this.elapsed = 0.0f;
        }
    }
}
