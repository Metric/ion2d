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

public class CCJumpTo extends CCJumpBy
{
     public CCJumpTo()
    {
        this(0,new Point2D.Float(0,0), 1.0f, 5);
    }

    public CCJumpTo(float duration, Point2D position, float height, int jumps)
    {
        this.jumps = jumps;
        this.height = height;
        this.delta = position;
        this.duration = duration;
    }

    public CCJumpTo(CCJumpTo original)
    {
        if(original == null) throw new NullPointerException();

        this.height = original.height;
        this.startPoint = original.startPoint;
        this.delta = original.delta;
        this.jumps = original.jumps;
        this.duration = original.duration;
        this.firstTick = original.firstTick;
        this.selector = original.selector;
        this.tag = original.tag;
    }

    public CCJumpTo clone()
    {
        return new CCJumpTo(this);
    }

    public void startWithTarget(CCSelector selector)
    {
        try
        {
            super.startWithTarget(selector);
            this.delta = new Point2D.Float((float)this.delta.getX() - this.startPoint.x, (float)this.delta.getY() - this.startPoint.y);
        } catch (Exception e)
        {
            System.out.println(e.getMessage());
        }
    }
}
