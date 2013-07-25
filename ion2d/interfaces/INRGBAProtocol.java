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

public interface INRGBAProtocol {

    /**
     * Sets the color
     * @param Color3B color
     */
    public void setColor(Color3B color);

    /**
     * Gets the color
     * @return Color3B
     */
    public Color3B getColor();

    /**
     * Gets the opacity
     * Ranges from 0.0 to 1.0
     * @return float
     */
    public float getOpacity();

    /**
     * Sets the opactiy
     * Ranges from 0.0 to 1.0
     * @param float opacity
     */
    public void setOpacity(float opacity);

    /**
     * Sets whether the opacity modifies the RGB
     * @param boolean modify
     */
    public void setOpacityModifyRGB(boolean modify);

    /**
     * Returns the current status of
     * Whether the opacity modifies the RGB
     * @return boolean
     */
    public boolean doesOpacityModifyRGB();
}
