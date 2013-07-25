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

public class INScaleBy extends INScaleTo
{
    /**
     * Default Constructor
     */
    public INScaleBy()
    {
        this(0, 1.0f);
    }

    /**
     * Secondary Constructor
     * @param float duration
     * @param float scale
     */
    public INScaleBy(float duration, float scale)
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
    public INScaleBy(float duration, float scaleX, float scaleY)
    {
        this.duration = duration;
        this.endScaleX = scaleX;
        this.endScaleY = scaleY;
    }

    /**
     * Copy Constructor
     * @param INScaleBy original
     */
    public INScaleBy(INScaleBy original)
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
     * @return INScaleBy
     */
    public INScaleBy clone()
    {
        return new INScaleBy(this);
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

            this.deltaX = this.startScaleX * this.endScaleX - this.startScaleX;
            this.deltaY = this.startScaleY * this.endScaleY - this.startScaleY;
        } catch (Exception e)
        {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Revers the action
     * @return INIntervalAction
     * @throws Exception
     */
    public INIntervalAction reverse() throws Exception
    {
        return new INScaleBy(this.duration, 1/this.endScaleX, 1/this.endScaleY);
    }
}
