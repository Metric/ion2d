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

import java.util.Vector;
import cocos2d.support.*;

public class CCSequence extends CCIntervalAction
{
    private Vector<CCFiniteAction> sequence;
    private int last;
    private int current;

    public CCSequence()
    {
        this.sequence = new Vector<CCFiniteAction>();
        this.last = -1;
        this.current = 0;
    }

    public CCSequence(CCFiniteAction action1, CCFiniteAction ... multipleActions)
    {
        this.sequence = new Vector<CCFiniteAction>(multipleActions.length + 1);
        this.last = -1;
        this.current = 0;

        float totalDuration = action1.getDuration();

        this.sequence.add(action1);

        for(int i = 0; i < multipleActions.length; i++)
        {
            if(multipleActions[i] != null)
            {
                totalDuration += multipleActions[i].getDuration();
                this.sequence.add(multipleActions[i]);
            }
        }

        this.duration = totalDuration;
    }

    public CCSequence(CCSequence original)
    {
        if(original == null) throw new NullPointerException();

        this.duration = original.duration;
        this.firstTick = original.firstTick;
        this.last = original.last;
        this.selector = original.selector;
        this.sequence = (Vector<CCFiniteAction>)original.sequence.clone();
        this.tag = original.tag;
    }

    public CCSequence clone()
    {
        return new CCSequence(this);
    }

    public void startWithTarget(CCSelector selector)
    {
        this.selector = selector;
        this.last = -1;
        this.current = 0;
    }

    public void stop()
    {
        for(CCFiniteAction action : this.sequence)
        {
            action.stop();
        }

        super.stop();
    }

    public void update(float time)
    {
        CCFiniteAction action = null;

        if(this.last != this.current)
        {
            this.last = this.current;
            action = this.sequence.get(this.last);
            action.startWithTarget(this.selector);
        }
        else
        {
            action = this.sequence.get(this.last);
        }

        float split = action.duration / this.duration;
        float newTime = 0.0f;
            
        if(time >= split)
        {
            if(split == 1)
            {
                newTime = 1;
            }
            else
            {
                newTime = (time - split) / (1 - split);
            }
        }
        else
        {
            if(split != 0)
            {
                newTime = time / split;
            }
            else
            {
                newTime = 1;
            }
        }

        action.update(newTime);

        if(newTime == 1)
            action.stop();

        if(action.isDone())
        {
            this.current++;

            if(this.current > this.sequence.size())
                this.current = this.sequence.size();
        }
    }

    public CCIntervalAction reverse()
    {
        try
        {
            CCFiniteAction[] buffer = new CCFiniteAction[this.sequence.size()];

            for(int i = 0; i < this.sequence.size(); i++)
            {
                if(i != 0)
                    buffer[i] = this.sequence.get(i).reverse();
            }

            CCFiniteAction first = this.sequence.get(0).reverse();

            if(first == null) return null;

            return new CCSequence(first, buffer);

        } catch (Exception e)
        {
            System.out.println(e.getMessage());
            return null;
        }
    }
}
