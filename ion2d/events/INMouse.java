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

import org.lwjgl.input.Mouse;
import org.lwjgl.input.Cursor;
import ion2d.INCursor;
import org.lwjgl.input.Keyboard;

public class INMouse {
    private static INMouse instance;

    protected Cursor mouseCursor;

    protected int[] buttons;
    protected int lastX;
    protected int lastY;

    /**
     * Constants for buttons
     */
    public static final int LEFT_BUTTON = 0;
    public static final int RIGHT_BUTTON = 1;
    public static final int MIDDLE_BUTTON = 2;
    public static final int NOBUTTON = -1;

    private INMouse()
    {
        this.mouseCursor = null;
        this.buttons = new int[3];
        this.lastX = 0;
        this.lastY = 0;
    }

    /**
     * Checks to see if the instance is created yet
     * If not we create it
     */
    protected static final void checkInstance()
    {
        if(instance == null)
        {
            instance = new INMouse();
        }
    }

    /**
     * Toggles whether the mouse cursor is grabbed
     * Basically, it grabs the mouse making it invisible
     * Unless you set a INCursor to use
     */
    public static final void grabCursor()
    {
        if(Mouse.isGrabbed() == false)
        {
            Mouse.setGrabbed(true);
        }
        else
        {
            Mouse.setGrabbed(false);
        }
    }
    
    /**
     * Returns whether the cursor is currently grabbed or not
     * @return boolean
     */
    public static final boolean isCursorGrabbed()
    {
        return Mouse.isGrabbed();
    }

    /**
     * Sets the cursor to display for the mouse
     * @param INCursor cursor
     */
    public static final void setCursor(INCursor cursor)
    {
        checkInstance();

        if(instance.mouseCursor != null)
            removeCursor();

        instance.mouseCursor = cursor.getCursor();

        try
        {
            Mouse.setNativeCursor(instance.mouseCursor);
        } catch (Exception e)
        {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Performs checking of incoming mouse polling
     * Also sends out to each listener the incoming mouse button / position
     */
    public static final void update()
    {
        checkInstance();

        Mouse.poll();

        while(Mouse.next())
        {
            int button = Mouse.getEventButton();

              instance.checkButton(button, Mouse.getX(), Mouse.getY());
              instance.checkMovement(button, Mouse.getX(), Mouse.getY());
        }
    }

    /**
     * Removes the custom cursor and restores the default cursor
     */
    public static final void removeCursor()
    {
        checkInstance();

        if(instance.mouseCursor != null)
            instance.mouseCursor.destroy();

        instance.mouseCursor = null;

        try
        {
            Mouse.setNativeCursor(null);
        } catch (Exception e)
        {
            System.out.println(e);
            e.printStackTrace();
        }

        System.gc();
    }

        /**
     * Checks the current status of the button being pressed.
     * Sends onMouseDown and onMouseMove calls if they are triggered
     * Also sends onMouseUp if the button is no longer being held
     * @param int button
     * @param int x
     * @param int y
     */
    public final void checkButton(int button, int x, int y)
    {
        boolean isAlt = (Keyboard.isKeyDown(INKey.LALT) || Keyboard.isKeyDown(INKey.RALT));
        boolean isShift = (Keyboard.isKeyDown(INKey.LSHIFT) || Keyboard.isKeyDown(INKey.LSHIFT));
        boolean isControl = (Keyboard.isKeyDown(INKey.LCONTROL) || Keyboard.isKeyDown(INKey.RCONTROL));

        if(button == INMouse.LEFT_BUTTON || button == INMouse.RIGHT_BUTTON || button == INMouse.MIDDLE_BUTTON)
        {
            if(this.buttons[button] == 0)
            {
                INEventManager.dispatch(new INMouseEvent(null, INMouseEvent.MOUSE_DOWN, this.lastX, this.lastY, x, y, button, isAlt, isShift, isControl, true));
            }
            else if(this.buttons[button] == 1)
            {
                INEventManager.dispatch(new INMouseEvent(null, INMouseEvent.MOUSE_PRESSED, this.lastX, this.lastY, x, y, button, isAlt, isShift, isControl, true));
            }

            if(Mouse.isButtonDown(button) == false)
            {
                INEventManager.dispatch(new INMouseEvent(null, INMouseEvent.MOUSE_UP, this.lastX, this.lastY, x, y, button, isAlt, isShift, isControl, true));
                this.buttons[button] = 0;
            }
        }
    }

    /**
     * Checks to see if the mouse has moved
     * If so the then the onMouseMove function is called on the listener
     * @param int x
     * @param int y
     */
    public final void checkMovement(int button, int x, int y)
    {
        boolean isAlt = (Keyboard.isKeyDown(INKey.LALT) || Keyboard.isKeyDown(INKey.RALT));
        boolean isShift = (Keyboard.isKeyDown(INKey.LSHIFT) || Keyboard.isKeyDown(INKey.LSHIFT));
        boolean isControl = (Keyboard.isKeyDown(INKey.LCONTROL) || Keyboard.isKeyDown(INKey.RCONTROL));


        if(this.lastX != x || this.lastY != y)
        {

            if(button == INMouse.LEFT_BUTTON || button == INMouse.RIGHT_BUTTON || button == INMouse.MIDDLE_BUTTON)
            {
                INEventManager.dispatch(new INMouseEvent(null, INMouseEvent.MOUSE_MOVE, this.lastX, this.lastY, x, y, button, isAlt, isShift, isControl, true));
            }
            else
            {
                INEventManager.dispatch(new INMouseEvent(null, INMouseEvent.MOUSE_MOVE, this.lastX, this.lastY, x, y, INMouse.NOBUTTON, isAlt, isShift, isControl, true));
            }

            this.lastX = x;
            this.lastY = y;
        }
    }
}
