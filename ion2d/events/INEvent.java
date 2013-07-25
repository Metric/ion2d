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

public class INEvent
{

    /**
     * The target who dispatched the event
     */
    public Object target;

    /**
     * If true the event bubbles up through all
     * Event listeners
     */
    public boolean bubbles;

    /**
     * The name of the event
     */
    public String event;

    public static String EVENT = "IN_EVENT";

    /**
     * Default Constructor
     */
    public INEvent()
    {
        this.target = null;
        this.event = EVENT;
        this.bubbles = false;
    }

    /**
     * Secondary Construct
     * @param String event
     * @param Object target
     * @param boolean bubbles
     */
    public INEvent(String event, Object target, boolean bubbles)
    {
        this.event = event;
        this.target = target;
        this.bubbles = bubbles;
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
                INEvent nEvent = (INEvent) obj;

                if(!this.target.equals(nEvent.target) || !this.event.equals(nEvent.event)
                    || this.bubbles != nEvent.bubbles)
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
