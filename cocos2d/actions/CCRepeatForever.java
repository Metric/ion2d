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

import cocos2d.*;
import cocos2d.support.*;

public class CCRepeatForever extends CCAction
{
    private CCIntervalAction other;

    public CCRepeatForever()
    {
        this.other = null;
    }

    public CCRepeatForever(CCIntervalAction action)
    {
        this.other = action;
    }

    public void startWithTarget(CCSelector selector)
    {
        super.startWithTarget(selector);

        if(this.other == null) return;

        this.other.startWithTarget(this.selector);
    }

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

    public boolean isDone()
    {
        return false;
    }

    public CCRepeatForever reverse()
    {
        CCRepeatForever reverseRepeat = new CCRepeatForever(this.other);
        reverseRepeat.startWithTarget(this.selector);

        return reverseRepeat;
    }
}
