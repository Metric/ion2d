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

import java.util.*;
import java.awt.*;
import java.awt.geom.*;

public class INAnimation
{
    protected String name;
    protected float delay;
    protected ArrayList<INSpriteFrame> frames;

    /**
     * Default constructor
     * Everything will be null do not use
     * Instead use one of the other constructors
     */
    public INAnimation()
    {
    }

    /**
     * Secondary Constructor
     * @param String name
     * @param float delay
     */
    public INAnimation(String name, float delay)
    {
        this(name,delay,null);
    }

    /**
     * Third Constructor
     * @param String name
     * @param float delay
     * @param Vector<INSpriteFrame> frames
     */
    public INAnimation(String name, float delay, ArrayList<INSpriteFrame> frames)
    {
        this.delay = delay;
        this.name = name;

        if(frames != null)
            this.frames = frames;
        else
            this.frames = new ArrayList<INSpriteFrame>(10);
    }

    /**
     * Copy Constructor
     * @param INAnimation original
     */
    public INAnimation(INAnimation original)
    {
        if(original == null) throw new NullPointerException();
        
        this.delay = original.delay;
        this.name = original.name;
        this.frames = (ArrayList<INSpriteFrame>)original.frames.clone();
    }

    /**
     * Clone override method
     */
    public INAnimation clone()
    {
        return new INAnimation(this);
    }

    /**
     * Adds a frame to the animation
     * @param INSpriteFrame frame
     */
    public void addFrame(INSpriteFrame frame)
    {
        this.frames.add(frame);
    }

    /**
     * Adds a frame to the animation based on the file path
     * @param String filepath
     */
    public void addFrame(String filepath)
    {
        INTexture2D texture = INTextureCache.addImage(filepath);
        Rectangle rect = new Rectangle(0,0,texture.imageSize.width, texture.imageSize.height);
        INSpriteFrame frame = new INSpriteFrame(texture, rect, new Point2D.Float(0,0));
        this.frames.add(frame);
    }

    /**
     * Adds a frame with a predefined texture and rectangle
     * @param INTexture2D texture
     * @param Rectangle rect
     */
    public void addFrameWithTexture(INTexture2D texture, Rectangle rect)
    {
        INSpriteFrame frame = new INSpriteFrame(texture, rect, new Point2D.Float(0,0));
        this.frames.add(frame);
    }

    /**
     * Gets the animation's name
     * @return String
     */
    public String getName()
    {
        return this.name;
    }

    /**
     * Gets the animation's delay
     * @return float
     */
    public float getDelay()
    {
        return this.delay;
    }

    /**
     * Gets the animation's frames
     * @return Vector<INSpriteFrame>
     */
    public ArrayList<INSpriteFrame> getFrames()
    {
        return (ArrayList<INSpriteFrame>)this.frames.clone();
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
                INAnimation animation = (INAnimation) obj;

                if(this.delay != animation.delay || !this.frames.equals(animation.frames)
                    || !this.name.equals(animation.name))
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
