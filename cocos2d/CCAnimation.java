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

import java.util.*;
import java.awt.*;
import java.awt.geom.*;

public class CCAnimation
{
    String name;
    float delay;
    Vector<CCSpriteFrame> frames;

    public CCAnimation() throws Exception
    {
        throw new Exception("Default CCAnimation constructor not supported");
    }

    public CCAnimation(String name, float delay)
    {
        this(name,delay,null);
    }
    
    public CCAnimation(String name, float delay, Vector<CCSpriteFrame> frames)
    {
        this.delay = delay;
        this.name = name;

        if(frames != null)
            this.frames = frames;
        else
            this.frames = new Vector<CCSpriteFrame>(10);
    }

    //Copy Constructor
    public CCAnimation(CCAnimation original)
    {
        if(original == null) throw new NullPointerException();
        
        this.delay = original.delay;
        this.name = original.name;
        this.frames = (Vector<CCSpriteFrame>)original.frames.clone();
    }

    //Clone override method
    public CCAnimation clone()
    {
        return new CCAnimation(this);
    }

    public void addFrame(CCSpriteFrame frame)
    {
        this.frames.add(frame);
    }

    public void addFrame(String filepath)
    {
        CCTexture2D texture = CCTextureCache.addImage(filepath);
        Rectangle rect = new Rectangle(0,0,texture.imageSize.width, texture.imageSize.height);
        CCSpriteFrame frame = new CCSpriteFrame(texture, rect, new Point2D.Float(0,0));
        this.frames.add(frame);
    }

    public void addFrameWithTexture(CCTexture2D texture, Rectangle rect)
    {
        CCSpriteFrame frame = new CCSpriteFrame(texture, rect, new Point2D.Float(0,0));
        this.frames.add(frame);
    }

    //Getters
    public String getName()
    {
        return this.name;
    }

    public float getDelay()
    {
        return this.delay;
    }

    public Vector<CCSpriteFrame> getFrames()
    {
        return (Vector<CCSpriteFrame>)this.frames.clone();
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
                CCAnimation animation = (CCAnimation) obj;

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
