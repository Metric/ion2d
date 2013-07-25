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
import java.awt.geom.*;

public class INMoveTo extends INIntervalAction
{
    protected Vertex2F endPoint;
    protected Vertex2F startPoint;
    protected Point2D delta;

    /**
     * Default constructor
     */
    public INMoveTo()
    {
        this(0,new Point2D.Float(0,0));
    }

    /**
     * Secondary Constructor
     * @param float duration
     * @param Point2D position
     */
    public INMoveTo(float duration, Point2D position)
    {
        this.endPoint = new Vertex2F((float)position.getX(), (float)position.getY());
        this.duration = duration;
    }

    /**
     * Copy Constructor
     * @param INMoveTo original
     */
    public INMoveTo(INMoveTo original)
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
     * Clone override
     * @return INMoveTo
     */
    public INMoveTo clone()
    {
        return new INMoveTo(this);
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

            this.startPoint = target.getPosition();
            this.delta = INMath.pointSubtract(this.endPoint.toPoint2D(), this.startPoint.toPoint2D());
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
            node.setPosition(new Vertex2F(this.startPoint.x + (float)this.delta.getX() * time, this.startPoint.y + (float)this.delta.getY() * time));
        } catch (Exception e)
        {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
    }
}
