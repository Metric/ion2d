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

import ion2d.support.INTypes.*;

public interface INBlendProtocol
{
    /**
     * Sets the blending
     * @param INBlend blend
     */
    public void setBlendFunction(INBlend blend);

    /**
     * Gets the blending
     * @return INBlend
     */
    public INBlend getBlendFunction();
}
