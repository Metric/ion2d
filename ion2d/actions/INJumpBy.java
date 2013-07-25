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

public class INJumpBy extends INIntervalAction
{
    protected int jumps;
    protected float height;
    protected Point2D delta;
    protected Vertex2F startPoint;

    /**
     * Default constructor
     */
    public INJumpBy()
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
    public INJumpBy(float duration, Point2D position, float height, int jumps)
    {
        this.jumps = jumps;
        this.height = height;
        this.delta = position;
        this.duration = duration;
    }

    /**
     * Copy constructor
     * @param INJumpBy original
     */
    public INJumpBy(INJumpBy original)
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
     * @return INJumpBy
     */
    public INJumpBy clone()
    {
        return new INJumpBy(this);
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
            float fraction = (time * this.jumps) % 1.0f;
            float y = height * 4 * fraction * (1 - fraction);
            y += (float)this.delta.getY() * time;
            float x = (float)this.delta.getX() * time;
            node.setPosition(new Vertex2F(this.startPoint.x + x, this.startPoint.y + y));
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
        return new INJumpBy(this.duration, new Point2D.Float((float)-this.delta.getX(), (float)-this.delta.getY()), this.height, this.jumps);
    }
}
