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

package ion2d;

import java.awt.*;
import ion2d.support.INTypes.*;

public class INLayer extends INNode
{
    /**
     * Default constructor
     */
    public INLayer()
    {
        Dimension size = INDirector.getScreenSize();
        this.anchorPoint = new Vertex2F(0.5f,0.5f);
        this.isRelativeAnchorePoint = false;
        this.setContentSize(size);
    }
}
