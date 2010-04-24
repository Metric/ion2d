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

public class CCJumpBy extends CCIntervalAction
{
    protected int jumps;
    protected float height;
    protected Point2D delta;
    protected Vertex2F startPoint;

    public CCJumpBy()
    {
        this(0,new Point2D.Float(0,0), 1.0f, 5);
    }

    public CCJumpBy(float duration, Point2D position, float height, int jumps)
    {
        this.jumps = jumps;
        this.height = height;
        this.delta = position;
        this.duration = duration;
    }

    public CCJumpBy(CCJumpBy original)
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

    public CCJumpBy clone()
    {
        return new CCJumpBy(this);
    }

    public void startWithTarget(CCSelector selector)
    {
        try
        {
            super.startWithTarget(selector);

            CCNode target = (CCNode)this.selector.target;

            this.startPoint = target.getPosition();
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
            float fraction = (time * this.jumps) % 1.0f;
            float y = height * 4 * fraction * (1 - fraction);
            y += (float)this.delta.getY() * time;
            float x = (float)this.delta.getX() * time;
            node.setPosition(new Vertex2F(this.startPoint.x + x, this.startPoint.y + y));
        } catch (Exception e)
        {
            System.out.println(e.getMessage());
        }
    }

    public CCIntervalAction reverse() throws Exception
    {
        return new CCJumpBy(this.duration, new Point2D.Float((float)-this.delta.getX(), (float)-this.delta.getY()), this.height, this.jumps);
    }
}
