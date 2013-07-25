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
import java.awt.geom.*;

public class INJumpTo extends INJumpBy
{
    /**
     * Default constructor
     */
    public INJumpTo()
    {
        this(0,new Point2D.Float(0,0), 1.0f, 5);
    }

    /**
     * Secondary Constructor
     * @param float duration
     * @param Point2D position
     * @param float height
     * @param int jumps
     */
    public INJumpTo(float duration, Point2D position, float height, int jumps)
    {
        this.jumps = jumps;
        this.height = height;
        this.delta = position;
        this.duration = duration;
    }

    /**
     * Copy Constructor
     * @param INJumpTo original
     */
    public INJumpTo(INJumpTo original)
    {
        if(original == null) throw new NullPointerException();

        this.height = original.height;
        this.startPoint = original.startPoint.clone();
        this.delta = original.delta;
        this.jumps = original.jumps;
        this.duration = original.duration;
        this.firstTick = original.firstTick;
        this.selector = original.selector.clone();
        this.tag = original.tag;
    }

    /**
     * Clone override
     * @return INJumpTo
     */
    public INJumpTo clone()
    {
        return new INJumpTo(this);
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
            this.delta = new Point2D.Float((float)this.delta.getX() - this.startPoint.x, (float)this.delta.getY() - this.startPoint.y);
        } catch (Exception e)
        {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
    }
}
