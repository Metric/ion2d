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
package cocos2d.actions;

import cocos2d.support.CCTypes.*;
import cocos2d.support.*;

public class CCIntervalAction extends CCFiniteAction
{
    private float elapsed;
    boolean firstTick;

    public CCIntervalAction()
    {
        this(0.05f);
    }

    public CCIntervalAction(float duration)
    {
        this.duration = duration;
        this.elapsed = 0.0f;

        if(this.duration == 0)
            this.duration = 0.05f;

        this.firstTick = true;
    }

    public CCIntervalAction(CCIntervalAction original)
    {
        if(original == null) throw new NullPointerException();
        
        this.duration = original.duration;
        this.elapsed = original.elapsed;
        this.firstTick = original.firstTick;
        this.selector = original.selector;
        this.tag = original.tag;
    }

    public CCIntervalAction clone()
    {
        return new CCIntervalAction(this);
    }

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

    public void startWithTarget(CCSelector selector)
    {
        super.startWithTarget(selector);
        this.elapsed = 0.0f;
        this.firstTick = true;
    }

    public void stop()
    {
        this.elapsed = this.duration;
        super.stop();
    }

    public float getElapsed()
    {
        return this.elapsed;
    }

    public boolean isDone()
    {
        return (this.elapsed >= this.duration);
    }

    public CCIntervalAction reverse() throws Exception
    {
        throw new Exception("Reverse Action Not Implemented");
    }
}
