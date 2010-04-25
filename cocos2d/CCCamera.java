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

import cocos2d.support.CCTypes.*;
import cocos2d.support.*;
import org.lwjgl.util.glu.*;


public class CCCamera
{
    protected float eyeX;
    protected float eyeY;
    protected float eyeZ;

    protected float centerX;
    protected float centerY;
    protected float centerZ;

    protected float upX;
    protected float upY;
    protected float upZ;

    protected boolean dirty;

    public CCCamera()
    {
        this.restore();
    }

    /**
     * Sets the camera in the default position
     */
    public void restore()
    {
        this.eyeX = this.eyeY = 0;
        this.eyeZ = this.getZEye();

        this.centerX = this.centerY = this.centerZ = 0;

        this.upX = 0.0f;
        this.upY = 1.0f;
        this.upZ = 0.0f;

        this.dirty = false;
    }

    /**
     * Sets the camera using gluLookAt using its eye, center, and up vector
     */
    public void locate()
    {
        if(this.dirty)
        {
            GLU.gluLookAt(this.eyeX, this.eyeY, this.eyeZ, this.centerX,
                    this.centerY, this.centerZ, this.upX, this.upY, this.upZ);
        }
    }

    //Setters
    public void setEye(float x, float y, float z)
    {
        this.dirty = true;
        this.eyeX = x;
        this.eyeY = y;
        this.eyeZ = z;
    }

    public void setCenter(float x, float y, float z)
    {
        this.dirty = true;
        this.centerX = x;
        this.centerY = y;
        this.centerZ = z;
    }

    public void setUp(float x, float y, float z)
    {
        this.dirty = true;
        this.upX = x;
        this.upY = y;
        this.upZ = z;
    }



    //Getters
    public boolean getDirty()
    {
        return this.dirty;
    }

    public float getZEye()
    {
        return CCTypes.FLT_EPSILON;
    }

    public Vertex3F getEye()
    {
        return new Vertex3F(this.eyeX, this.eyeY, this.eyeZ);
    }

    public Vertex3F getCenter()
    {
        return new Vertex3F(this.centerX, this.centerY, this.centerZ);
    }

    public Vertex3F getUp()
    {
        return new Vertex3F(this.upX, this.upY, this.upZ);
    }
}
