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

import java.util.Vector;
import ion2d.support.*;

public class INSequence extends INIntervalAction
{
    protected Vector<INFiniteAction> sequence;
    protected int last;
    protected int current;

    /**
     * Default constructor
     */
    public INSequence()
    {
        this.sequence = new Vector<INFiniteAction>();
        this.last = -1;
        this.current = 0;
    }

    /**
     * Secondary Constructor
     * @param INFiniteAction action1
     * @param INFiniteAction ... multipleActions
     */
    public INSequence(INFiniteAction action1, INFiniteAction ... multipleActions)
    {
        this.sequence = new Vector<INFiniteAction>(multipleActions.length + 1);
        this.last = -1;
        this.current = 0;

        float totalDuration = action1.getDuration();

        this.sequence.add(action1);

        if(multipleActions != null)
        {
            for(int i = 0; i < multipleActions.length; i++)
            {
                if(multipleActions[i] != null)
                {
                    totalDuration += multipleActions[i].getDuration();
                    this.sequence.add(multipleActions[i]);
                }
            }
        }

        this.duration = totalDuration;
    }

    /**
     * Copy Constructor
     * @param INSequence original
     */
    public INSequence(INSequence original)
    {
        if(original == null) throw new NullPointerException();

        this.duration = original.duration;
        this.firstTick = original.firstTick;
        this.last = original.last;
        this.selector = original.selector.clone();
        this.sequence = (Vector<INFiniteAction>)original.sequence.clone();
        this.tag = original.tag;
    }

    /**
     * Clone Override
     * @return INSequence
     */
    public INSequence clone()
    {
        return new INSequence(this);
    }

    /**
     * Starts the action with the specified target
     * @param INSelector selector
     */
    public void startWithTarget(INSelector selector)
    {
        this.selector = selector;
        this.last = -1;
        this.current = 0;
    }

    /**
     * Stops the action
     */
    public void stop()
    {
        for(INFiniteAction action : this.sequence)
        {
            action.stop();
        }

        super.stop();
    }

    /**
     * Updates the action
     * @param float time
     */
    public void update(float time)
    {
        INFiniteAction action = null;

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

    /**
     * Reverses the action
     * @return INIntervalAction
     */
    public INIntervalAction reverse()
    {
        try
        {
            Vector<INFiniteAction> buffer = new Vector<INFiniteAction>(this.sequence.size());


            for(int i = this.sequence.size() - 1; i >= 0; i--)
            {
                    buffer.add(this.sequence.get(i).reverse());
            }

            INFiniteAction first = buffer.get(0);

            buffer.remove(0);

            if(first == null) return null;

            return new INSequence(first, (INFiniteAction[])buffer.toArray());

        } catch (Exception e)
        {
            System.out.println(e.getMessage());
            e.printStackTrace();
            return null;
        }
    }
}
