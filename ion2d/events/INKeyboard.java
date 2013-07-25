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

import org.lwjgl.input.Keyboard;

public class INKeyboard
{
    private static INKeyboard instance;

    protected int[] keys;

    private INKeyboard()
    {
        this.keys = new int[256];
    }

    /**
     * Checks to see if the instance is created yet
     * If not we create it
     */
    protected static final void checkInstance()
    {
        if(instance == null)
        {
            instance = new INKeyboard();
        }
    }

    /**
     * Performs checking of incoming keyboard polling
     * Also sends out to each listener the incoming key
     */
    public static final void update()
    {
        checkInstance();

        Keyboard.poll();

        while(Keyboard.next())
        {
            int key = Keyboard.getEventKey();
            instance.checkKey(key);
        }
    }

    /**
     * Checks to see if the specified key is pressed
     * @param int key
     * @return boolean
     */
    protected static final boolean isKeyDown(int key)
    {
        return Keyboard.isKeyDown(key);
    }

    /**
     * Checks the current incoming key to see if it is pressed
     * Releaseed, or has just been pressed
     * @param int key
     */
    protected final void checkKey(int key)
    {
        boolean isAlt = (Keyboard.isKeyDown(INKey.LALT) || Keyboard.isKeyDown(INKey.RALT));
        boolean isShift = (Keyboard.isKeyDown(INKey.LSHIFT) || Keyboard.isKeyDown(INKey.LSHIFT));
        boolean isControl = (Keyboard.isKeyDown(INKey.LCONTROL) || Keyboard.isKeyDown(INKey.RCONTROL));

        if(this.keys[key] == 0)
        {
            INEventManager.dispatch(new INKeyboardEvent(null,INKeyboardEvent.KEY_DOWN, key, isAlt, isShift, isControl, true));
        }
        else if(this.keys[key] == 1)
        {
            INEventManager.dispatch(new INKeyboardEvent(null,INKeyboardEvent.KEY_PRESSED, key, isAlt, isShift, isControl, true));
        }

        if(Keyboard.isKeyDown(key) == false)
        {
            INEventManager.dispatch(new INKeyboardEvent(null,INKeyboardEvent.KEY_UP, key, isAlt, isShift, isControl, true));
            this.keys[key] = 0;
        }
    }
}
