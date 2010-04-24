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
import cocos2d.*;

public class CCRotateTo extends CCIntervalAction
{
    private float startAngle;
    private float resultAngle;
    private float differenceAngle;

    public CCRotateTo()
    {
        this(0,0);
    }

    public CCRotateTo(float duration, float angle)
    {
        this.resultAngle = angle;
        this.duration = duration;
    }

    public CCRotateTo(CCRotateTo original)
    {
        if(original == null) throw new NullPointerException();

        this.resultAngle = original.resultAngle;
        this.differenceAngle = original.differenceAngle;
        this.startAngle = original.startAngle;
        this.duration = original.duration;
        this.firstTick = original.firstTick;
        this.selector = original.selector;
        this.tag = original.tag;
    }

    public CCRotateTo clone()
    {
        return new CCRotateTo(this);
    }

    public void startWithTarget(CCSelector selector)
    {
        try
        {
            super.startWithTarget(selector);

            CCNode target = (CCNode)this.selector.target;

            this.startAngle = target.getRotation();
            if(this.startAngle > 0)
            {
                this.startAngle = this.startAngle % 360.0f;
            }
            else
            {
                this.startAngle = this.startAngle % -360.0f;
            }

            this.differenceAngle = this.resultAngle - this.startAngle;

            if(this.differenceAngle > 180)
                this.differenceAngle -= 360;
            if(this.differenceAngle < -180)
                this.differenceAngle += 360;
            
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
            node.setRotation(this.startAngle + this.differenceAngle * time);
        } catch (Exception e)
        {
            System.out.println(e.getMessage());
        }
    }
}
