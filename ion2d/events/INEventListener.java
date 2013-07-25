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
package ion2d.events;

public class INEventListener
{
    /**
     * The target object to call
     * when dispatched
     */
    public Object target;

    /**
     * The event to listen for
     */
    public String event;

    /**
     * The name of the method to
     * Call when event is dispatched
     */
    public String methodName;

    /**
     * Default Constructor
     */
    public INEventListener()
    {
        this.target = null;
        this.event = null;
    }

    /**
     * Secondary constructor
     * @param Object target
     * @param String event
     */
    public INEventListener(Object target, String event, String methodName)
    {
        this.target = target;
        this.event = event;
        this.methodName = methodName;
    }

    /**
     * Equals Method override
     * @param Object obj
     * @return bool
     */
    public boolean equals ( Object obj )
    {
        if ( this == obj ) return true;

        if ((obj != null) && (getClass() == obj.getClass()))
        {
                INEventListener nEvent = (INEventListener) obj;

                if(!this.target.equals(nEvent.target) || !this.event.equals(nEvent.event))
                {
                    return false;
                }

                return true;
        }
        else
        {
                return false;
        }
    }
}
