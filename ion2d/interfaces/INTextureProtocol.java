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

import ion2d.*;

public interface INTextureProtocol extends INBlendProtocol
{
    /**
     * Gets the texture
     * @return INTexture2D
     */
    public INTexture2D getTexture();

    /**
     * Sets the texture
     * @param INTexture2D texture
     * @throws Exception
     */
    void setTexture(INTexture2D texture) throws Exception;
}
