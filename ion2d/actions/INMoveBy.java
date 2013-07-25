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

public class INMoveBy extends INMoveTo
{
    /**
     * Default Constructor
     */
    public INMoveBy()
    {
        this(0,new Point2D.Float(0,0));
    }

    /**
     * Secondary Constructor
     * @param float duration
     * @param Point2D position
     */
    public INMoveBy(float duration, Point2D position)
    {
        this.delta = position;
        this.duration = duration;
    }

    /**
     * Copy Constructor
     * @param INMoveBy original
     */
    public INMoveBy(INMoveBy original)
    {
        if(original == null) throw new NullPointerException();

        this.startPoint = original.startPoint.clone();
        this.delta = original.delta;
        this.endPoint = original.endPoint.clone();
        this.duration = original.duration;
        this.firstTick = original.firstTick;
        this.selector = original.selector.clone();
        this.tag = original.tag;
    }

    /**
     * Clone Override
     * @return INMoveBy
     */
    public INMoveBy clone()
    {
        return new INMoveBy(this);
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
        } catch (Exception e)
        {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Reverses the action
     * @return INIntervalAction
     * @throws Exception
     */
    public INIntervalAction reverse() throws Exception
    {
        return new INMoveBy(this.duration, new Point2D.Float((float)-this.delta.getX(), (float)-this.delta.getY()));
    }
}
