/**
 * cocos2d for Java
 *
 * Copyright (C) 2010 Vantage Technic
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the 'cocos2d for Java' license.
 *
 * You will find a copy of this license within the cocos2d for Java
 * distribution inside the "LICENSE" file.
 */

package cocos2d;

import java.awt.*;
import java.awt.geom.*;

public class CCSpriteFrame
{
    protected Rectangle rect;
    protected Point2D offset;
    protected CCTexture2D texture;
    protected Dimension originalSize;

    public CCSpriteFrame() throws Exception
    {
        throw new Exception("Default CCSpriteFrame constructor not supported");
    }

    public CCSpriteFrame(CCTexture2D tex, Rectangle rect, Point2D offset)
    {
        this(tex, rect, offset, rect.getSize());
    }
    
    public CCSpriteFrame(CCTexture2D tex, Rectangle rect, Point2D offset, Dimension size)
    {
        this.texture = tex;
        this.rect = rect;
        this.offset = offset;
        this.originalSize = size;
    }

    //Copy Constructor
    public CCSpriteFrame(CCSpriteFrame original)
    {
        if(original == null) throw new NullPointerException();
        
        this.texture = original.texture;
        this.rect = (Rectangle)original.rect.clone();
        this.offset = (Point2D)offset.clone();
        this.originalSize = (Dimension)original.originalSize.clone();
    }

    public CCSpriteFrame clone()
    {
        return new CCSpriteFrame(this);
    }

    /**
     * Getters
     */
    public Rectangle getRect()
    {
        return this.rect;
    }

    public Point2D getOffset()
    {
        return this.offset;
    }

    public Dimension getOriginalSize()
    {
        return this.originalSize;
    }

    public CCTexture2D getTexture()
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
                CCSpriteFrame frame = (CCSpriteFrame) obj;

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
