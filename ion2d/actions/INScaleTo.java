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
package ion2d.actions;

import ion2d.support.*;
import ion2d.support.INTypes.*;
import ion2d.*;

public class INScaleTo extends INIntervalAction
{
    protected float scaleX;
    protected float scaleY;
    protected float startScaleX;
    protected float startScaleY;
    protected float endScaleX;
    protected float endScaleY;
    protected float deltaX;
    protected float deltaY;

    /**
     * Default Constructor
     */
    public INScaleTo()
    {
        this(0, 1.0f);
    }

    /**
     * Secondary Constructor
     * @param float duration
     * @param float scale
     */
    public INScaleTo(float duration, float scale)
    {
        this.duration = duration;
        this.endScaleX = this.endScaleY = scale;
    }

    /**
     * Third Constructor
     * @param float duration
     * @param float scaleX
     * @param float scaleY
     */
    public INScaleTo(float duration, float scaleX, float scaleY)
    {
        this.duration = duration;
        this.endScaleX = scaleX;
        this.endScaleY = scaleY;
    }

    /**
     * Copy Constructor
     * @param INScaleTo original
     */
    public INScaleTo(INScaleTo original)
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
        this.selector = original.selector.clone();
        this.tag = original.tag;
    }

    /**
     * Clone Override
     * @return INScaleTo
     */
    public INScaleTo clone()
    {
        return new INScaleTo(this);
    }

    /**
     * Starts the action with the specified target
     * @param INSelector selector
     */
    public void startWithTarget(INSelector selector)
    {
        try
        {
            super.startWithTarget(selector);

            INNode target = (INNode)this.selector.target;
            this.startScaleX = target.getScaleX();
            this.startScaleY = target.getScaleY();
            this.deltaX = this.endScaleX - this.startScaleX;
            this.deltaY = this.endScaleY - this.startScaleY;
        } catch (Exception e)
        {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Updates the action
     * @param float time
     */
    public void update(float time)
    {
        try
        {
            INNode node = (INNode)this.selector.target;
            node.setScaleX((this.startScaleX + this.deltaX * time));
            node.setScaleY((this.startScaleY + this.deltaY * time));
        } catch (Exception e)
        {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
    }
}
