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

public interface INMouseProtocol
{
    /**
     * Called when a button is pressed
     * @param int button
     * @param INMouseEvent event
     */
    public void onMousePressed(int button, INMouseEvent event);

    /**
     * Called when a button is first pressed
     * @param int button
     * @param INMouseEvent event
     */
    public void onMouseDown(int button, INMouseEvent event);

    /**
     * Called when the button is release
     * @param int button
     * @param INMouseEvent event
     */
    public void onMouseUp(int button, INMouseEvent event);

    /**
     * Called when the mouse is moved
     * @param int button
     * @param INMouseEvent event
     */
    public void onMouseMove(int button, INMouseEvent event);
}
