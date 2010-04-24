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
import java.awt.geom.*;

public class CCMoveBy extends CCMoveTo
{
    public CCMoveBy()
    {
        this(0,new Point2D.Float(0,0));
    }

    public CCMoveBy(float duration, Point2D position)
    {
        this.delta = position;
        this.duration = duration;
    }

    public CCMoveBy(CCMoveBy original)
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

    public CCMoveBy clone()
    {
        return new CCMoveBy(this);
    }

    public void startWithTarget(CCSelector selector)
    {
        try
        {
            super.startWithTarget(selector);
        } catch (Exception e)
        {
            System.out.println(e.getMessage());
        }
    }

    public CCIntervalAction reverse() throws Exception
    {
        return new CCMoveBy(this.duration, new Point2D.Float((float)-this.delta.getX(), (float)-this.delta.getY()));
    }
}
