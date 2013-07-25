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

public class INAction
{
    protected INSelector selector;
    protected int tag;

    /**
     * Default constructor
     */
    public INAction()
    {
        this.selector = null;
        this.tag = -1;
    }

    /**
     * Secondary constructor for allowing a
     * Action to be created with a specific target
     * @param INSelector selector
     */
    public INAction(INSelector selector)
    {
        this.selector = selector;
    }

    /**
     * Third constructor for allowing a
     * Action to be created with a specific target and tag
     * @param INSelector selector
     * @param int tag
     */
    public INAction(INSelector selector, int tag)
    {
        this.selector = selector;
        this.tag = tag;
    }

    public void startWithTarget(INSelector selector)
    {
        this.selector = selector;
    }

    /**
     * Copy constructor
     * @param INAction original
     */
    public INAction(INAction original)
    {
        if(original == null) throw new NullPointerException();
        
        this.selector = original.selector;
        this.tag = original.tag;
    }

    /**
     * Clone override
     * @return INAction
     */
    public INAction clone()
    {
        return new INAction(this);
    }

    /**
     * Stops the action, but should be done through the original node.stopAction(INAction action);
     */
    public void stop()
    {
        this.selector = null;
    }

    /**
     * Called every frame with its delta time. Don't overrided.
     * @param float time
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

    /**
     * Tells whether the action is complete or not
     * @return boolean
     */
    public boolean isDone()
    {
        return true;
    }

    /**
     * Gets the tag associated with the action
     * @return int
     */
    public int getTag()
    {
        return this.tag;
    }

    /**
     * Gets the target of the action
     * @return INSelector
     */
    public INSelector getTarget()
    {
        return this.selector;
    }
}
