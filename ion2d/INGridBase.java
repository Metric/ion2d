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
import java.awt.*;
import org.lwjgl.opengl.*;

public class INGridBase
{
    protected boolean active;
    protected int reuseGrid;
    protected GridSize gridSize;
    protected INTexture2D texture;
    protected Vertex2F step;
    protected INGrabber grabber;

    /**
     * Default constructor do not use!
     */
    public INGridBase()
    {
    }

    /**
     * Copy constructor
     * @param INGridBase original
     */
    public INGridBase(INGridBase original)
    {
        if(original == null) throw new NullPointerException();
        
        this.active = original.active;
        this.grabber = original.grabber.clone();
        this.gridSize = original.gridSize.clone();
        this.reuseGrid = original.reuseGrid;
        this.step = original.step.clone();
        this.texture = original.texture.clone();
    }

    /**
     * Clone Override
     * @return INGridBase
     */
    public INGridBase clone()
    {
        return new INGridBase(this);
    }

    /**
     * Secondary Constructor
     * @param CCGridSize size
     * @throws Exception
     */
    public INGridBase(GridSize size) throws Exception
    {
        this.active = false;
        this.reuseGrid = 0;
        this.gridSize = size;
        
        Dimension window = INDirector.getScreenSize();
        INImage image = new INImage(window.width, window.height);
        this.texture = new INTexture2D(image);

        this.grabber = new INGrabber();
        
        this.grabber.grab(this.texture);
        this.step.x = window.width / this.gridSize.x;
        this.step.y = window.height / this.gridSize.y;
    }

    /**
     * Before drawing the grid
     */
    public void beforeDraw()
    {
        INDirector.setProjection(ProjectionFormat.DirectorProjection2D);
        this.grabber.beforeRender();
    }

    /**
     * After drawing the grid
     * @param target
     */
    public void afterDraw(INNode target)
    {
        this.grabber.afterRender();

        INDirector.setProjection(ProjectionFormat.DirectorProjection3D);

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

    /**
     * draws the grid to screen
     * Override this on sub classes
     */
    public void blit() {};

    /**
     * Reuse grid / resets the grid
     * Override this on sub classes
     */
    public void reuse() {};

    /**
     * Sets the grid active
     * @param boolean active
     */
    public void setActive(boolean active)
    {
        this.active = active;

        if(!this.active)
        {
            INDirector.setProjection(ProjectionFormat.DirectorProjection2D);
        }
    }

    /**
     * Gets the active state of the grid
     * @return boolean
     */
    public boolean getActive()
    {
        return this.active;
    }

    /**
     * Gets the number of reuses left for the grid
     * @return int
     */
    public int getReuseGrid()
    {
        return this.reuseGrid;
    }

    /**
     * Gets the grid size
     * @return CCGridSize
     */
    public GridSize getGridSize()
    {
        return this.gridSize.clone();
    }

    /**
     * Gets the next step for the grid
     * @return Vertex2F
     */
    public Vertex2F getStep()
    {
        return this.step.clone();
    }

    /**
     * Gets the texture used by the grid
     * This texture is a clone and any modifications
     * Do not affect the grid texture
     * @return INTexture2D
     */
    public INTexture2D getTexture()
    {
        return this.texture.clone();
    }

    /**
     * Gets the grabber for the grid
     * This grabber is a clone and does not
     * affect the grid grabber
     * @return INGrabber
     */
    public INGrabber getGrabber()
    {
        return this.grabber.clone();
    }

    /**
     * Sets the number of reuses for the grid
     * @param int times
     */
    public void setReuse(int times)
    {
        this.reuseGrid = times;
    }
}
