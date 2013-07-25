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
import ion2d.support.*;
import ion2d.support.INTypes.*;

import java.util.*;

public class INAnimate extends INIntervalAction
{
    protected INAnimation animation;
    protected INSpriteFrame originalFrame;
    protected boolean restoreOriginalFrame;

    /**
     * Constructor
     * @param INAnimation animation
     * @param boolean restoreOriginalFrame
     */
    public INAnimate(INAnimation animation, boolean restoreOriginalFrame)
    {
        super(animation.getFrames().size() * animation.getDelay());
        this.restoreOriginalFrame = restoreOriginalFrame;
        this.animation = animation;
        this.originalFrame = null;
    }

    /**
     * Secondary Constructor
     * @param float duration
     * @param INAnimation animation
     * @param boolean restoreOriginalFrame
     */
    public INAnimate(float duration, INAnimation animation, boolean restoreOriginalFrame)
    {
        super(duration);
        this.restoreOriginalFrame = restoreOriginalFrame;
        this.animation = animation;
        this.originalFrame = null;
    }

    /**
     * Copy Constructor
     * @param INAnimate original
     */
    public INAnimate(INAnimate original)
    {
        if(original == null) throw new NullPointerException();

        this.duration = original.duration;
        this.firstTick = original.firstTick;
        this.restoreOriginalFrame = original.restoreOriginalFrame;
        this.animation = original.animation.clone();
        this.selector = original.selector.clone();
        this.tag = original.tag;
        this.originalFrame = original.originalFrame.clone();
    }

    /**
     * Clone Override
     * @return INAnimate
     */
    public INAnimate clone()
    {
        return new INAnimate(this);
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
            INSprite sprite = (INSprite)this.selector.target;

            if(this.restoreOriginalFrame)
                this.originalFrame = sprite.getCurrentFrame();
            
        } catch (Exception e)
        {
            System.out.print(e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * stops the action
     */
    public void stop()
    {
        if(this.restoreOriginalFrame)
        {
            if(this.selector != null)
            {
                if(this.selector.target != null)
                {
                    INSprite sprite = (INSprite)this.selector.target;

                    try
                    {
                        if(this.originalFrame != null)
                        {
                            sprite.setDisplayFrame(this.originalFrame);
                        }
                    } catch (Exception e)
                    {
                        System.out.println(e.getMessage());
                        e.printStackTrace();
                    }
                }
            }
        }

        super.stop();
    }

    /**
     * Updates the action
     * @param float time
     */
    public void update(float time)
    {
        int idx = 0;
        ArrayList<INSpriteFrame> frames = this.animation.getFrames();
        int numberOfFrames = frames.size();
        float slice = 1.0f / numberOfFrames;

        if(time != 0)
        {
            idx = Math.round(time / slice);
        }

        if(idx >= numberOfFrames)
        {
            idx = numberOfFrames - 1;
        }

        if(this.selector != null)
        {
            INSprite sprite = (INSprite)this.selector.target;
            INSpriteFrame frame = frames.get(idx);
            if(!sprite.getCurrentFrame().equals(frame))
            {
                try
                {
                    sprite.setDisplayFrame(frame);
                } catch (Exception e)
                {
                    System.out.println(e.getMessage());
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * Reverse override
     * @return INAnimate
     */
    public INIntervalAction reverse()
    {
        ArrayList<INSpriteFrame> frames = this.animation.getFrames();
        ArrayList<INSpriteFrame> reverseFrames = new ArrayList<INSpriteFrame>(frames.size());

        for(int i = frames.size() - 1; i >= 0; i--)
        {
            reverseFrames.add(frames.get(i));
        }

        INAnimation newAnimation = new INAnimation(this.animation.getName(), this.animation.getDelay(), reverseFrames);

        return new INAnimate(this.duration, newAnimation, this.restoreOriginalFrame);
    }
}
