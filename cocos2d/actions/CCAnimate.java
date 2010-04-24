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
import cocos2d.support.CCTypes.*;

public class CCAnimate extends CCIntervalAction
{
    protected CCAnimation animation;
    protected boolean restoreOriginalFrame;

    public CCAnimate(CCAnimation animation, boolean restoreOriginalFrame)
    {
        super(animation.frames.size() * animation.getDelay());
        this.restoreOriginalFrame = restoreOriginalFrame;
        this.animation = animation;
       
    }

    public CCAnimate(float duration, CCAnimation animation, boolean restoreOriginalFrame)
    {
        super(duration);
        this.restoreOriginalFrame = restoreOriginalFrame;
        this.animation = animation;
    }

    public CCAnimate(CCAnimate original)
    {
        if(original == null) throw new NullPointerException();

        this.duration = original.duration;
        this.firstTick = original.firstTick;
        this.restoreOriginalFrame = original.restoreOriginalFrame;
        this.animation = original.animation;
        this.selector = original.selector;
        this.tag = original.tag;
    }

    public CCAnimate clone()
    {
        return new CCAnimate(this);
    }

    public void startWithTarget(CCSelector selector)
    {
        try
        {
            super.startWithTarget(selector);
            CCSprite sprite = (CCSprite)this.selector.target;

            //Add restore original frame here
        } catch (Exception e)
        {
            System.out.print(e.getMessage());
        }
    }

    public void stop()
    {
        //Add restore original frame here
        super.stop();
    }

    public void update(float time)
    {

    }
}
