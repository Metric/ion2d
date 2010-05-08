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

    protected static void checkInstance()
    {
        if(instance == null)
        {
            instance = new CCScheduler();
        }
    }

    public static void setTimeScale(float scale)
    {
        checkInstance();
        instance.timeScale = scale;
    }

    /**
     * Removes all currently scheduled timers
     */
    public static void unscheduleAllTimers()
    {
        checkInstance();

        instance.schedule.clear();
        instance.scheduleToAdd.clear();
        instance.scheduleToRemove.clear();
    }

    /**
     * Performs all the tick operations on the timers
     * @param float timeTick
     */
    public static void tick(float timeTick)
    {
        checkInstance();

        if(instance.timeScale != 1.0f)
        {
            timeTick *= instance.timeScale;
        }

        for(CCTimer removeTimer : instance.scheduleToRemove)
        {
            instance.schedule.remove(removeTimer);
        }

        instance.scheduleToRemove.clear();

        for(CCTimer addTimer : instance.scheduleToAdd)
        {
            instance.schedule.add(addTimer);
        }

        instance.scheduleToAdd.clear();

        for(CCTimer scheduledTimer : instance.schedule)
        {
            scheduledTimer.fire(timeTick);
        }
    }

    /**
     * Adds a timer to the scheduler
     * @param CCTimer timer
     */
    public static void scheduleTimer(CCTimer timer)
    {
        checkInstance();

        if(instance.scheduleToRemove.contains(timer))
        {
            instance.scheduleToRemove.remove(timer);
        }

        if(!instance.schedule.contains(timer) && !instance.scheduleToAdd.contains(timer))
        {
            instance.scheduleToAdd.add(timer);
        }
    }

    /**
     * Unschedules a timer from the scheduler
     * @param CCTimer timer
     */
    public static void unscheduleTimer(CCTimer timer)
    {
        checkInstance();

        if(instance.scheduleToAdd.contains(timer))
        {
            instance.scheduleToAdd.remove(timer);
            return;
        }

        if(instance.schedule.contains(timer))
        {
            instance.scheduleToRemove.add(timer);
        }
    }
}
