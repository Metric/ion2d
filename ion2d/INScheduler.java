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

package ion2d;

import ion2d.support.INTypes.*;
import java.util.ArrayList;

public class INScheduler
{
    protected ArrayList<INTimer> schedule;
    protected ArrayList<INTimer> scheduleToAdd;
    protected ArrayList<INTimer> scheduleToRemove;
    protected float timeScale;

    private static INScheduler instance;

    private INScheduler()
    {
        this.schedule = new ArrayList<INTimer>();
        this.scheduleToAdd = new ArrayList<INTimer>();
        this.scheduleToRemove = new ArrayList<INTimer>();
        this.timeScale = 1.0f;
    }

    /**
     * Checks to see if the singleton has been created
     * If it hasn't then it creates it
     */
    protected static void checkInstance()
    {
        if(instance == null)
        {
            instance = new INScheduler();
        }
    }

    /**
     * Sets the time scale to use for the scheduler
     * @param float scale
     */
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

        int i = 0;
        int length = instance.scheduleToRemove.size();
        for(i = 0; i < length; i++)
        {
            instance.schedule.remove(instance.scheduleToRemove.get(i));
        }

        instance.scheduleToRemove.clear();

        length = instance.scheduleToAdd.size();
        for(i = 0; i < length; i++)
        {
            instance.schedule.add(instance.scheduleToAdd.get(i));
        }

        instance.scheduleToAdd.clear();

        length = instance.schedule.size();
        for(i = 0; i < length; i++)
        {
            instance.schedule.get(i).fire(timeTick);
        }
    }

    /**
     * Adds a timer to the scheduler
     * @param INTimer timer
     */
    public static void scheduleTimer(INTimer timer)
    {
        checkInstance();

        instance.scheduleToAdd.add(timer);
    }

    /**
     * Unschedules a timer from the scheduler
     * @param INTimer timer
     */
    public static void unscheduleTimer(INTimer timer)
    {
        checkInstance();

        instance.scheduleToRemove.add(timer);
    }

    /**
     * Finalize override
     */
    public void finalize()
    {
        unscheduleAllTimers();
    }
}
