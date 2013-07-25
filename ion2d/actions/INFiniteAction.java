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
package ion2d.actions;

import ion2d.*;

public class INFiniteAction extends INAction
{
    protected float duration;

    /**
     * Override me
     * @return INFiniteAction
     */
    public INFiniteAction reverse() throws Exception
    {
        throw new Exception("Reverse Action Not Implemented");
    }

    /**
     * Gets the current duration of the action
     * @return float
     */
    public float getDuration()
    {
        return this.duration;
    }
}
