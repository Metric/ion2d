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

import cocos2d.support.*;
import cocos2d.support.CCTypes.*;
import cocos2d.*;
import java.awt.geom.*;

public class CCMoveTo extends CCIntervalAction
{
    protected Vertex2F endPoint;
    protected Vertex2F startPoint;
    protected Point2D delta;

    public CCMoveTo()
    {
        this(0,new Point2D.Float(0,0));
    }

    public CCMoveTo(float duration, Point2D position)
    {
        this.endPoint = new Vertex2F((float)position.getX(), (float)position.getY());
        this.duration = duration;
    }

    public CCMoveTo(CCMoveTo original)
    {
        if(original == null) throw new NullPointerException();

        this.startPoint = original.startPoint;
        this.delta = original.delta;
        this.endPoint = original.endPoint;
        this.duration = original.duration;
        this.firstTick = original.firstTick;
        this.selector = original.selector;
        this.tag = original.tag;
    }

    public CCMoveTo clone()
    {
        return new CCMoveTo(this);
    }

    public void startWithTarget(CCSelector selector)
    {
        try
        {
            super.startWithTarget(selector);

            CCNode target = (CCNode)this.selector.target;

            this.startPoint = target.getPosition();
            this.delta = CCMath.pointSubtract(this.endPoint.toPoint2D(), this.startPoint.toPoint2D());
        } catch (Exception e)
        {
            System.out.println(e.getMessage());
        }
    }

    public void update(float time)
    {
        try
        {
            CCNode node = (CCNode)this.selector.target;
            node.setPosition(new Vertex2F(this.startPoint.x + (float)this.delta.getX() * time, this.startPoint.y + (float)this.delta.getY() * time));
        } catch (Exception e)
        {
            System.out.println(e.getMessage());
        }
    }
}
