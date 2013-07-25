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

public class INKeyboardEvent extends INEvent
{
    public static String KEY_DOWN = "IN_KEY_DOWN";
    public static String KEY_UP = "IN_KEY_UP";
    public static String KEY_PRESSED = "IN_KEY_PRESSED";

    /**
     * The key that is being pressed or let up
     */
    public int key;

    /**
     * True if the ALT key is being pressed
     * At the same time the other key was pressed
     */
    public boolean alt;

    /**
     * True if the shift key is being pressed
     * At the same time the other key was pressed
     */
    public boolean shift;

    /**
     * True if the control key is being pressed
     * At the same time the other key was pressed
     */
    public boolean control;

    /**
     * Default Constructor
     */
    public INKeyboardEvent()
    {
        this(null,EVENT,-1,false,false,false,true);
    }

    /**
     * Secondary Constructor
     * @param Object target
     * @param String event
     * @param int key
     * @param boolean alt
     * @param boolean shift
     * @param boolean control
     * @param boolean bubbles
     */
    public INKeyboardEvent(Object target, String event, int key, boolean alt, boolean shift, boolean control, boolean bubbles)
    {
        this.target = target;
        this.event = event;
        this.key = key;
        this.alt = alt;
        this.shift = shift;
        this.control = control;
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
                INKeyboardEvent nEvent = (INKeyboardEvent) obj;

                if(!this.target.equals(nEvent.target) || !this.event.equals(nEvent.event)
                    || this.bubbles != nEvent.bubbles || this.key != nEvent.key || this.alt != nEvent.alt
                    || this.shift != nEvent.shift || this.control != nEvent.control)
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
