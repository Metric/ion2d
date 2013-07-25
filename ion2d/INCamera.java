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

import ion2d.support.INTypes.*;
import ion2d.support.*;
import org.lwjgl.util.glu.*;


public class INCamera
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

    /**
     * Default constructor
     */
    public INCamera()
    {
        this.restore();
    }

    /**
     * Copy constructor
     * @param INCamera original
     */
    public INCamera(INCamera original)
    {
        if(original == null) throw new NullPointerException();
        
        this.centerX = original.centerX;
        this.centerY = original.centerY;
        this.centerZ = original.centerZ;
        this.dirty = original.dirty;
        this.eyeX = original.eyeX;
        this.eyeY = original.eyeY;
        this.eyeZ = original.eyeZ;
        this.upX = original.upX;
        this.upY = original.upY;
        this.upZ = original.upZ;
    }

    /**
     * Clone override
     */
     public INCamera clone()
     {
        return new INCamera(this);
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
        return INTypes.FLT_EPSILON;
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
