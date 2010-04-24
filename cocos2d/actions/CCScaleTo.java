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
import cocos2d.*;

public class CCScaleTo extends CCIntervalAction
{
    protected float scaleX;
    protected float scaleY;
    protected float startScaleX;
    protected float startScaleY;
    protected float endScaleX;
    protected float endScaleY;
    protected float deltaX;
    protected float deltaY;

    public CCScaleTo()
    {
        this(0, 1.0f);
    }

    public CCScaleTo(float duration, float scale)
    {
        this.duration = duration;
        this.endScaleX = this.endScaleY = scale;
    }

    public CCScaleTo(float duration, float scaleX, float scaleY)
    {
        this.duration = duration;
        this.endScaleX = scaleX;
        this.endScaleY = scaleY;
    }

    public CCScaleTo(CCScaleTo original)
    {
        if(original == null) throw new NullPointerException();

        this.scaleX = original.scaleX;
        this.scaleY = original.scaleY;
        this.endScaleX = original.endScaleX;
        this.endScaleY = original.endScaleY;
        this.startScaleX = original.startScaleX;
        this.startScaleY = original.startScaleY;
        this.deltaX = original.deltaX;
        this.deltaY = original.deltaY;
        this.duration = original.duration;
        this.firstTick = original.firstTick;
        this.selector = original.selector;
        this.tag = original.tag;
    }

    public CCScaleTo clone()
    {
        return new CCScaleTo(this);
    }

    public void startWithTarget(CCSelector selector)
    {
        try
        {
            super.startWithTarget(selector);

            CCNode target = (CCNode)this.selector.target;
            this.startScaleX = target.getScaleX();
            this.startScaleY = target.getScaleY();
            this.deltaX = this.endScaleX - this.startScaleX;
            this.deltaY = this.endScaleY - this.startScaleY;
        } catch (Exception e)
        {
            System.out.println(e.getMessage());
        }
    }

    public void update(float time)
    {
        try
        {
            CCNode node = (CCNode)this.selector.target;
            node.setScaleX((this.startScaleX + this.deltaX * time));
            node.setScaleY((this.startScaleY + this.deltaY * time));
        } catch (Exception e)
        {
            System.out.println(e.getMessage());
        }
    }
}
