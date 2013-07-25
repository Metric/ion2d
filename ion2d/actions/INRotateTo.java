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
import ion2d.*;

public class INRotateTo extends INIntervalAction
{
    protected float startAngle;
    protected float resultAngle;
    protected float differenceAngle;

    /**
     * Default constructor
     */
    public INRotateTo()
    {
        this(0,0);
    }

    /**
     * Secondary Constructor
     * @param float duration
     * @param float angle
     */
    public INRotateTo(float duration, float angle)
    {
        this.resultAngle = angle;
        this.duration = duration;
    }

    /**
     * Copy Constructor
     * @param INRotateTo original
     */
    public INRotateTo(INRotateTo original)
    {
        if(original == null) throw new NullPointerException();

        this.resultAngle = original.resultAngle;
        this.differenceAngle = original.differenceAngle;
        this.startAngle = original.startAngle;
        this.duration = original.duration;
        this.firstTick = original.firstTick;
        this.selector = original.selector.clone();
        this.tag = original.tag;
    }

    /**
     * Clone Override
     * @return INRotateTo
     */
    public INRotateTo clone()
    {
        return new INRotateTo(this);
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
            node.setRotation(this.startAngle + this.differenceAngle * time);
        } catch (Exception e)
        {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
    }
}
