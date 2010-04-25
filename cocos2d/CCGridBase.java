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
import java.awt.*;
import java.awt.geom.*;
import org.lwjgl.opengl.*;

public abstract class CCGridBase
{
    protected boolean active;
    protected int reuseGrid;
    protected CCGridSize gridSize;
    protected CCTexture2D texture;
    protected Vertex2F step;
    protected CCGrabber grabber;

    public CCGridBase() throws Exception
    {
        throw new Exception("Default CCGridBase constructor not supported");
    }

    public CCGridBase(CCGridSize size) throws Exception
    {
        this.active = false;
        this.reuseGrid = 0;
        this.gridSize = size;
        
        Dimension window = CCDirector.getWindowSize();
        CCImage image = new CCImage(window.width, window.height);
        this.texture = new CCTexture2D(image);

        this.grabber = new CCGrabber();
        
        this.grabber.grab(this.texture);
        this.step.x = window.width / this.gridSize.x;
        this.step.y = window.height / this.gridSize.y;
    }

    public void beforeDraw()
    {
        CCDirector.set2DProjection();
        this.grabber.beforeRender();
    }

    public void afterDraw(CCNode target)
    {
        this.grabber.afterRender();

        CCDirector.set3DProjection();
        CCDirector.applyLandscape();

        if(target.getCamera().getDirty())
        {
            Vertex2F offset = target.getAnchorePointInPixel();

            //Camera should be applied to AnchorPoint
            GL11.glTranslatef(offset.x, offset.y, 0);
            target.getCamera().locate();
            GL11.glTranslatef(-offset.x, -offset.y, 0);
        }

        this.texture.bind();
        this.blit();
    }

    protected abstract void blit();

    protected abstract void reuse();

    //Setters
    public void setActive(boolean active)
    {
        this.active = active;

        if(!this.active)
        {
            CCDirector.setProjection(CCDirector.getProjection());
        }
    }

    //Getters
    public boolean getActive()
    {
        return this.active;
    }

    public int getReuseGrid()
    {
        return this.reuseGrid;
    }

    public CCGridSize getGridSize()
    {
        return this.gridSize.clone();
    }

    public Vertex2F getStep()
    {
        return this.step.clone();
    }

    public CCTexture2D getTexture()
    {
        return this.texture.clone();
    }

    public CCGrabber getGrabber()
    {
        return this.grabber.clone();
    }
}
