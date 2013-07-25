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
import java.awt.geom.*;

public class INSpriteFrame
{
    protected Rectangle rect;
    protected Point2D offset;
    protected INTexture2D texture;
    protected Dimension originalSize;

    /**
     * Default constructor. Do not use!
     * Instead use one of the other constructors
     */
    public INSpriteFrame()
    {
    }

    /**
     * Creates a sprite frame from a specified INTexture2D, Rectangle
     * and Point2D offset
     * @param INTexture2D tex
     * @param Rectangle rect
     * @param Point2D offset
     */
    public INSpriteFrame(INTexture2D tex, Rectangle rect, Point2D offset)
    {
        this(tex, rect, offset, rect.getSize());
    }

    /**
     * Creates a sprite frame from a specified INTexture2D, Rectangle,
     * Point2D offset, and the Dimension for size
     * @param INTexture2D tex
     * @param Rectangle rect
     * @param Point2D offset
     * @param Dimension size
     */
    public INSpriteFrame(INTexture2D tex, Rectangle rect, Point2D offset, Dimension size)
    {
        this.texture = tex;
        this.rect = rect;
        this.offset = offset;
        this.originalSize = size;
    }

    /**
     * Copy Constructor
     * @param INSpriteFrame original
     */
    public INSpriteFrame(INSpriteFrame original)
    {
        if(original == null) throw new NullPointerException();
        
        this.texture = original.texture;
        this.rect = (Rectangle)original.rect.clone();
        this.offset = (Point2D)offset.clone();
        this.originalSize = (Dimension)original.originalSize.clone();
    }

    /**
     * Clone override
     * @return INSpriteFrame
     */
    public INSpriteFrame clone()
    {
        return new INSpriteFrame(this);
    }

    /**
     * Gets the sprite frame's rectangle
     * @return Rectangle
     */
    public Rectangle getRect()
    {
        return this.rect;
    }

    /**
     * Gets the sprite frame's Point2D offset
     * @return Point2D
     */
    public Point2D getOffset()
    {
        return this.offset;
    }

    /**
     * Gets the original size of the sprite frame
     * @return Dimension
     */
    public Dimension getOriginalSize()
    {
        return this.originalSize;
    }

    /**
     * Gets the texture of the sprite frame
     * The texture is a shallow copy and any modification made
     * will affect the sprite frame
     * @return INTexture2D
     */
    public INTexture2D getTexture()
    {
        return this.texture;
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
                INSpriteFrame frame = (INSpriteFrame) obj;

                if(!this.offset.equals(frame.offset) || !this.originalSize.equals(frame.originalSize)
                        || !this.rect.equals(frame.rect) || !this.texture.equals(frame.texture))
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
