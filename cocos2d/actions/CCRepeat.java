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

import cocos2d.support.*;

public class CCRepeat extends CCIntervalAction
{
    private CCFiniteAction other;
    private int times;
    private int total;

    public CCRepeat()
    {
        this.other = null;
        this.times = 0;
        this.total = 0;
    }

    public CCRepeat(CCFiniteAction action, int times)
    {
        this.duration = action.getDuration() * times;
        this.times = times;
        this.other = action;
    }

    public CCRepeat(CCRepeat original)
    {
        if(original == null) throw new NullPointerException();

        this.duration = original.duration;
        this.other = original.other;
        this.firstTick = original.firstTick;
        this.selector = original.selector;
        this.tag = original.tag;
        this.times = original.times;
        this.total = original.total;
    }

    public CCRepeat clone()
    {
        return new CCRepeat(this);
    }

    public void startWithTarget(CCSelector selector)
    {
        this.total = 0;
        super.startWithTarget(selector);
        this.other.startWithTarget(selector);
    }

    public void stop()
    {
        this.other.stop();
        super.stop();
    }

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

    public boolean isDone()
    {
        return (this.total == this.times);
    }

    public CCIntervalAction reverse()
    {
        try
        {
            return new CCRepeat(this.other.reverse(), this.times);
        } catch (Exception e)
        {
            System.out.println(e.getMessage());
            return null;
        }
    }
}
