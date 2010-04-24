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

public class CCScaleBy extends CCScaleTo
{
    public CCScaleBy()
    {
        this(0, 1.0f);
    }

    public CCScaleBy(float duration, float scale)
    {
        this.duration = duration;
        this.endScaleX = this.endScaleY = scale;
    }

    public CCScaleBy(float duration, float scaleX, float scaleY)
    {
        this.duration = duration;
        this.endScaleX = scaleX;
        this.endScaleY = scaleY;
    }

    public CCScaleBy(CCScaleBy original)
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

    public CCScaleBy clone()
    {
        return new CCScaleBy(this);
    }

    public void startWithTarget(CCSelector selector)
    {
        try
        {
            super.startWithTarget(selector);

            this.deltaX = this.startScaleX * this.endScaleX - this.startScaleX;
            this.deltaY = this.startScaleY * this.endScaleY - this.startScaleY;
        } catch (Exception e)
        {
            System.out.println(e.getMessage());
        }
    }

    public CCIntervalAction reverse() throws Exception
    {
        return new CCScaleBy(this.duration, 1/this.endScaleX, 1/this.endScaleY);
    }
}
