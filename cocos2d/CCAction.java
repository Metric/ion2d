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

public class CCAction
{
    protected CCSelector selector;
    protected int tag;

    public CCAction()
    {
        this.selector = null;
        this.tag = -1;
    }

    public void startWithTarget(CCSelector selector)
    {
        this.selector = selector;
    }

    public CCAction(CCAction original)
    {
        if(original == null) throw new NullPointerException();
        
        this.selector = original.selector;
        this.tag = original.tag;
    }

    public CCAction clone()
    {
        return new CCAction(this);
    }

    /**
     * Stops the action, but should be done through the original node.stopAction(CCAction action);
     */
    public void stop()
    {
        this.selector = null;
    }

    /**
     * Called every frame with its delta time. Don't overrided.
     * @param time
     */
    public void step(float time)
    {

    }

    /**
     * Called once per frame, time value between 0 and 1
     * @param CCTime time
     */
    public void update(float time)
    {
        
    }

    public boolean isDone()
    {
        return true;
    }

    public int getTag()
    {
        return this.tag;
    }

    public CCSelector getTarget()
    {
        return this.selector;
    }
}
