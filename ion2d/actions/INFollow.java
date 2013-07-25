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

import ion2d.*;
import ion2d.support.INTypes.*;
import ion2d.support.*;
import java.awt.*;

public class INFollow extends INAction
{
    protected INNode target;
    protected boolean boundarySet;
    protected Rectangle worldBoundary;

    /**
     * Default constructor
     */
    public INFollow()
    {
        this.target = null;
        this.worldBoundary = null;
        this.boundarySet = false;
    }

    /**
     * Secondary constructor
     * @param INNode follow
     */
    public INFollow(INNode follow)
    {
        if(target == null) throw new NullPointerException();
        this.target = follow;
        this.boundarySet = false;
    }

    /**
     * Third Constructor
     * @param INNode follow
     * @param Rectangle worldBoundary
     */
    public INFollow(INNode follow, Rectangle worldBoundary)
    {
        if(target == null) throw new NullPointerException();
        this.target = follow;
        this.boundarySet = true;
        this.worldBoundary = worldBoundary;
    }

    /**
     * Copy Constructor
     * @param INFollow original
     */
    public INFollow(INFollow original)
    {
        if(original == null) throw new NullPointerException();
        this.target = original.target;
        this.boundarySet = original.boundarySet;
        this.worldBoundary = (Rectangle)original.worldBoundary.clone();
        this.selector = original.selector.clone();
        this.tag = original.tag;
    }

    /**
     * Sets the world rectangle boundary for following
     * @param Rectangle rect
     */
    public void setFollowBoundry(Rectangle rect) { this.worldBoundary = rect; this.boundarySet = true; }

    /**
     * Clone override
     * @return INFollow
     */
    public INFollow clone()
    {
        return new INFollow(this);
    }

    /**
     * This method advances the actual action based on the time given
     * @param float time
     */
    public void step(float time)
    {
        if(this.selector == null || this.target == null) return;

        if(this.selector.target instanceof INNode)
        {
            INNode tempTarget = (INNode)this.selector.target;
            Vertex2F tempPoint = new Vertex2F();

            if(tempTarget.getPosition().x + tempTarget.getContentSize().width > this.target.getPosition().x )
            {
                tempPoint.x = tempTarget.getPosition().x - 1.0f;
            }
            else if(tempTarget.getPosition().x - tempTarget.getContentSize().width < this.target.getPosition().x)
            {
                tempPoint.x = tempTarget.getPosition().x + 1.0f;
            }

            if(tempTarget.getPosition().y + tempTarget.getContentSize().height > this.target.getPosition().y)
            {
                tempPoint.y = tempTarget.getPosition().y - 1.0f;
            }
            else if(tempTarget.getPosition().y - tempTarget.getContentSize().height < this.target.getPosition().y)
            {
                tempPoint.y = tempTarget.getPosition().y + 1.0f;
            }

            //Check to see if the movement is within the specified world boundaries
            if(this.boundarySet)
            {
                tempPoint.x = INMath.clamp(tempPoint.x, (float)this.worldBoundary.getMinX(), (float)this.worldBoundary.getWidth());
                tempPoint.y = INMath.clamp(tempPoint.y, (float)this.worldBoundary.getMinY(), (float)this.worldBoundary.getHeight());
            }

            tempTarget.setPosition(tempPoint);
        }
    }

    /**
     * The current status of action and if it is done or not
     * @return boolean
     */
    public boolean isDone()
    {
        if(this.target == null) return true;

        return !this.target.isRunning();
    }

    /**
     * Stops the action
     */
    public void stop()
    {
        super.stop();
    }
}
