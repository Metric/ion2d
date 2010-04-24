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

package cocos2d;

import cocos2d.support.CCTypes.*;
import java.util.Vector;

public class CCScheduler
{
    protected Vector<CCTimer> schedule;
    protected Vector<CCTimer> scheduleToAdd;
    protected Vector<CCTimer> scheduleToRemove;
    protected float timeScale;

    private static CCScheduler instance;

    private CCScheduler()
    {
        this.schedule = new Vector<CCTimer>();
        this.scheduleToAdd = new Vector<CCTimer>();
        this.scheduleToRemove = new Vector<CCTimer>();
        this.timeScale = 1.0f;
    }

    public static CCScheduler getInstance()
    {
        if(instance == null)
        {
            instance = new CCScheduler();
        }

        return instance;
    }

    public void setTimeScale(float scale) { this.timeScale = scale; }

    /**
     * Removes all currently scheduled timers
     */
    public void unscheduleAllTimers()
    {
        this.schedule.clear();
        this.scheduleToAdd.clear();
        this.scheduleToRemove.clear();
    }

    /**
     * Performs all the tick operations on the timers
     * @param timeTick
     */
    public void tick(float timeTick)
    {
        if(this.timeScale != 1.0f)
        {
            timeTick *= this.timeScale;
        }

        for(CCTimer removeTimer : this.scheduleToRemove)
        {
            this.schedule.remove(removeTimer);
        }

        this.scheduleToRemove.clear();

        for(CCTimer addTimer : this.scheduleToAdd)
        {
            this.schedule.add(addTimer);
        }

        this.scheduleToAdd.clear();

        for(CCTimer scheduledTimer : this.schedule)
        {
            scheduledTimer.fire(timeTick);
        }
    }

    /**
     * Adds a timer to the scheduler
     * @param CCTimer timer
     */
    public void scheduleTimer(CCTimer timer)
    {
        if(this.scheduleToRemove.contains(timer))
        {
            this.scheduleToRemove.remove(timer);
        }

        if(!this.schedule.contains(timer) && !this.scheduleToAdd.contains(timer))
        {
            this.scheduleToAdd.add(timer);
        }
    }

    /**
     * Unschedules a timer from the scheduler
     */
    public void unscheduleTimer(CCTimer timer)
    {
        if(this.scheduleToAdd.contains(timer))
        {
            this.scheduleToAdd.remove(timer);
            return;
        }

        if(this.schedule.contains(timer))
        {
            this.scheduleToRemove.add(timer);
        }
    }
}
