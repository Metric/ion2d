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

public class INMouseEvent extends INEvent
{
    public static String MOUSE_DOWN = "IN_MOUSE_DOWN";
    public static String MOUSE_UP = "IN_MOUSE_UP";
    public static String MOUSE_PRESSED = "IN_MOUSE_PRESSED";
    public static String MOUSE_MOVE = "IN_MOUSE_MOVE";

    /**
     * The previous mouse X position
     */
    public int x1;

    /**
     * The previous mouse Y position
     */
    public int y1;

    /**
     * The current mouse X position
     */
    public int x2;

    /**
     * The current mouse Y position
     */
    public int y2;

    /**
     * The button that is being pressed on the mouse
     */
    public int button;

    /**
     * True is the alt key is down while moving or pressing
     * Another mouse button
     */
    public boolean alt;

    /**
     * True if the shift key is down while moving or pressing
     * Another mouse button
     */
    public boolean shift;

    /**
     * True if the control key is down while moving or pressing
     * Another mouse button
     */
    public boolean control;

    /**
     * Default constructor
     */
    public INMouseEvent()
    {
        this(null,EVENT,0,0,0,0,-1,false, false, false, true);
    }

    /**
     * Secondary constructor
     * @param Object target
     * @param String event
     * @param int x1
     * @param int y1
     * @param int x2
     * @param int y2
     * @param int button
     * @param boolean alt
     * @param boolean shift
     * @param boolean control
     * @param boolean bubbles
     */
    public INMouseEvent(Object target, String event, int x1, int y1, int x2, int y2, int button, boolean alt, boolean shift, boolean control,boolean bubbles)
    {
        this.target = target;
        this.event = event;
        this.x1 = x1;
        this.y1 = y1;
        this.x2 = x2;
        this.y2 = y2;
        this.button = button;
        this.bubbles = bubbles;
        this.alt = alt;
        this.shift = shift;
        this.control = control;
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
                INMouseEvent nEvent = (INMouseEvent) obj;

                if(!this.target.equals(nEvent.target) || !this.event.equals(nEvent.event)
                    || this.bubbles != nEvent.bubbles || this.x1 != nEvent.x1 || this.x2 != nEvent.x2
                    || this.y1 != nEvent.y1 || this.y2 != nEvent.y2 || this.button != nEvent.button
                    || this.alt != nEvent.alt || this.shift != nEvent.shift || this.control != nEvent.control)
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
