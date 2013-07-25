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

package ion2d.interfaces;
import ion2d.events.*;

public interface INKeyboardProtocol
{
    /**
     * Called when a key is pressed
     * @param int key
     * @param INKeyboardEvent event
     */
    public void onKeyPressed(int key, INKeyboardEvent event);

    /**
     * Called when a key is first pressed
     * @param int key
     * @param INKeyboardEvent event
     */
    public void onKeyDown(int key, INKeyboardEvent event);

    /**
     * Called when a key is release
     * @param int key
     * @param INKeyboardEvent event
     */
    public void onKeyUp(int key, INKeyboardEvent event);
}
