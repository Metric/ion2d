/**
 * cocos2d for Java
 *
 * Copyright (C) 2010 Vantage Technic
 *
 * instance program is free software; you can redistribute it and/or modify
 * it under the terms of the 'cocos2d for Java' license.
 *
 * You will find a copy of instance license within the cocos2d for Java
 * distribution inside the "LICENSE" file.
 */

package cocos2d;

import cocos2d.support.CCTypes.*;
import java.io.*;
import java.net.*;
import java.util.*;
import java.nio.*;
import org.lwjgl.opengl.*;
import java.awt.*;

public class CCTextureAtlas
{
    protected int totalQuads;
    protected int capacity;

    protected Vector<QuadTexture3F> quads;
    protected CCTexture2D texture;
    protected IntBuffer indices;
    protected IntBuffer vboBuffers;

    protected FloatBuffer colorCoordinates;
    protected FloatBuffer vertices;
    protected FloatBuffer textureCoordinates;

    protected int totalVisible;
    protected int lastTotalVisible;
    protected boolean visibleQuadsChanged;

    public CCTextureAtlas() throws Exception
    {
        throw new Exception("CCTextureAtlas does not respond to default constructor");
    }

    public CCTextureAtlas(String filePath, int capacity)
    {
        this(CCTextureCache.addImage(filePath), capacity);
    }

    public CCTextureAtlas(File file, int capacity)
    {
        this(CCTextureCache.addImage(file, file.getName()), capacity);
    }

    public CCTextureAtlas(URL filePath, int capacity)
    {
        this(CCTextureCache.addImage(filePath, filePath.toString()), capacity);
    }

    public CCTextureAtlas(CCTexture2D texture, int capacity)
    {
        this.quads = new Vector<QuadTexture3F>(capacity);
        this.texture = texture;
        this.capacity = capacity;
        this.totalQuads = 0;
        this.totalVisible = 0;
        this.lastTotalVisible = 0;

        this.vboBuffers = IntBuffer.allocate(2);
        GL15.glGenBuffers(this.vboBuffers);

        this.initIndices();
    }

    private void initIndices()
    {
        this.indices = IntBuffer.allocate(this.capacity * 36);

        for(int i = 0; i < this.capacity; i++)
        {
            this.indices.put(i*6, i*4);
            this.indices.put(i*6+1, i*4+1);
            this.indices.put(i*6+2, i*4+2);
            this.indices.put(i*6+3, i*4+1);
            this.indices.put(i*6+4, i*4+2);
            this.indices.put(i*6+5, i*4+3);
        }

        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, this.vboBuffers.get(0));
        GL15.glBufferData(GL15.GL_ARRAY_BUFFER, this.getVertices(), GL15.GL_DYNAMIC_DRAW);
        GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, this.vboBuffers.get(1));
        GL15.glBufferData(GL15.GL_ELEMENT_ARRAY_BUFFER, this.indices, GL15.GL_STATIC_DRAW);
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
        GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, 0);
    }

    /**
     * Updates the visible quad buffers
     * @param QuadTexture3F quad
     * @param int index
     */
    protected void updateVisibleQuads(QuadTexture3F quad, int index)
    {
        //If the total number of visible quads have changed we rebuild the entire
        //Visible quads buffer or We also rebuild if any of the buffers are null
        //Otherwise if a real quad is provided and that quad is on screen then
        //we only update the one quad In the visible buffer
        if(this.visibleQuadsChanged || this.vertices == null || this.colorCoordinates == null || this.textureCoordinates == null)
        {
            this.vertices = this.getVertices();
            this.colorCoordinates = this.getColors();
            this.textureCoordinates = this.getTextureCoordinates();
            this.visibleQuadsChanged = false;
        }
        else if(quad != null && !this.isQuadOffScreen(quad))
        {
            this.updateVertice(quad, index);
            this.updateTextureCoordinates(quad, index);
            this.updateColors(quad, index);
        }
    }

    protected void updateVertice(QuadTexture3F quad, int index)
    {
        if(index > this.totalVisible) return;

        int offset = index * 12;

        this.vertices.put(offset,quad.bl.vertice.x);
        this.vertices.put(offset+1,quad.bl.vertice.y);
        this.vertices.put(offset+2,quad.bl.vertice.z);

        this.vertices.put(offset+3,quad.br.vertice.x);
        this.vertices.put(offset+4,quad.br.vertice.y);
        this.vertices.put(offset+5,quad.br.vertice.z);

        this.vertices.put(offset+6,quad.tl.vertice.x);
        this.vertices.put(offset+7,quad.tl.vertice.y);
        this.vertices.put(offset+8,quad.tl.vertice.z);

        this.vertices.put(offset+9,quad.tr.vertice.x);
        this.vertices.put(offset+10,quad.tr.vertice.y);
        this.vertices.put(offset+11,quad.tr.vertice.z);
    }

    /**
     * Updates the quad texture coordinates in the texture coordinates buffer
     * In the currently visible quads
     * @param QuadTexture3F quad
     * @param int index
     */
    protected void updateTextureCoordinates(QuadTexture3F quad, int index)
    {
        if(index > this.totalVisible) return;

        int offset = index * 8;

        this.textureCoordinates.put(offset,quad.bl.coords.x);
        this.textureCoordinates.put(offset+1,quad.bl.coords.y);

        this.textureCoordinates.put(offset+2,quad.br.coords.x);
        this.textureCoordinates.put(offset+3,quad.br.coords.y);

        this.textureCoordinates.put(offset+4,quad.tl.coords.x);
        this.textureCoordinates.put(offset+5,quad.tl.coords.y);

        this.textureCoordinates.put(offset+6,quad.tr.coords.x);
        this.textureCoordinates.put(offset+7,quad.tr.coords.y);
    }
    
    protected void updateColors(QuadTexture3F quad, int index)
    {
        if(index > this.totalVisible) return;

        int offset = index * 16;

        this.colorCoordinates.put(offset,quad.bl.color.getRed());
        this.colorCoordinates.put(offset+1,quad.bl.color.getGreen());
        this.colorCoordinates.put(offset+2,quad.bl.color.getBlue());
        this.colorCoordinates.put(offset+3,quad.bl.color.getAlpha());

        this.colorCoordinates.put(offset+4,quad.br.color.getRed());
        this.colorCoordinates.put(offset+5,quad.br.color.getGreen());
        this.colorCoordinates.put(offset+6,quad.br.color.getBlue());
        this.colorCoordinates.put(offset+7,quad.br.color.getAlpha());

        this.colorCoordinates.put(offset+8,quad.tl.color.getRed());
        this.colorCoordinates.put(offset+9,quad.tl.color.getGreen());
        this.colorCoordinates.put(offset+10,quad.tl.color.getBlue());
        this.colorCoordinates.put(offset+11,quad.tl.color.getAlpha());

        this.colorCoordinates.put(offset+12,quad.tr.color.getRed());
        this.colorCoordinates.put(offset+13,quad.tr.color.getGreen());
        this.colorCoordinates.put(offset+14,quad.tr.color.getBlue());
        this.colorCoordinates.put(offset+15,quad.tr.color.getAlpha());
    }

    public void updateQuad(QuadTexture3F quad, int index)
    {
        if(index >= 0 && index < this.capacity)
        {
            this.quads.set(index, quad);
            this.QuadsVisibleChanged();
            this.updateVisibleQuads(quad, index);
        }
    }

    public void insertQuad(QuadTexture3F quad, int index)
    {
        if(index >= 0 && index < this.capacity)
        {
            this.totalQuads++;
            this.quads.add(index, quad);
            this.QuadsVisibleChanged();
            this.updateVisibleQuads(quad, index);
        }
    }

    public void removeQuad(int index)
    {
        this.quads.remove(index);
        this.totalQuads--;
        this.QuadsVisibleChanged();
        this.updateVisibleQuads(null, index);
    }

    public void removeAllQuads()
    {
        this.quads.clear();
        this.vertices = null;
        this.colorCoordinates = null;
        this.textureCoordinates = null;
        this.totalQuads = 0;
        this.totalVisible = 0;
        this.lastTotalVisible = 0;
    }

    public void resize(int size)
    {
        if(size < this.quads.size())
        {
            for(int i = size; i < this.quads.size(); i ++)
            {
                this.quads.remove(i);
            }
        }

        this.quads.ensureCapacity(size);
        this.capacity = size;

        if(size < this.totalQuads)
            this.totalQuads = size;

        this.QuadsVisibleChanged();
        this.updateVisibleQuads(null, 0);
    }

    public int capacity()
    {
        return this.capacity;
    }

    public int getTotalQuads()
    {
        return this.totalQuads;
    }

    public void drawQuads()
    {
        if(this.totalVisible == 0 || this.totalQuads == 0)
            return;

        this.texture.bind();

        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, this.vboBuffers.get(0));
        GL11.glVertexPointer(3, 0, this.vertices);

        GL11.glColorPointer(4, 0, this.colorCoordinates);

        GL11.glTexCoordPointer(2, 0, this.textureCoordinates);

        GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, this.vboBuffers.get(1));
        GL12.glDrawRangeElements(GL11.GL_QUADS, 0, this.totalVisible*6, this.indices.limit(), GL11.GL_UNSIGNED_INT, 0);

        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
        GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, 0);
    }

    /**
     * Calculates the total number of visible quads on the screen
     */
    protected void totalQuadsOnScreen()
    {
        this.totalVisible = 0;

        for(int i = 0; i < this.totalQuads; i++)
        {
            QuadTexture3F quad = this.quads.get(i);

            if(!this.isQuadOffScreen(quad))
            {
                this.totalVisible++;
            }
        }
    }

    /**
     * Checks to see if the total number of quads visible has changed
     * If so it flags the boolean so that we know we need to rebuild the
     * visible quad buffers to include any new quads.
     */
    protected void QuadsVisibleChanged()
    {
        this.totalQuadsOnScreen();
        
        if(this.lastTotalVisible != this.totalVisible)
        {
            this.lastTotalVisible = this.totalVisible;
            this.visibleQuadsChanged = true;
        }
    }

    public int getTotalVisibleQuads()
    {
        return this.totalVisible;
    }

    protected boolean isQuadOffScreen(QuadTexture3F quad)
    {
        Dimension screen = CCDirector.getScreenSize();

        //We need to take into account any rotation transforms
        //Technically bl and tl could be greater than tr and br
        //Because of a rotation and the quad could technically still be on the screen
        if(quad.bl.vertice.x < quad.br.vertice.x || quad.tl.vertice.x  < quad.tr.vertice.x)
        {
            if(quad.bl.vertice.y < quad.tl.vertice.y || quad.br.vertice.y < quad.tr.vertice.y)
            {
                if((quad.bl.vertice.x > screen.width && quad.tl.vertice.x > screen.width)
                      || (quad.bl.vertice.y > screen.height && quad.br.vertice.y > screen.height)
                      || (quad.tr.vertice.y < 0 && quad.tl.vertice.y < 0)
                      || (quad.tr.vertice.x < 0 && quad.br.vertice.x < 0))
                {
                    return true;
                }
            }
            else if(quad.bl.vertice.y > quad.tl.vertice.y || quad.br.vertice.y > quad.tr.vertice.y)
            {
                if((quad.bl.vertice.x > screen.width && quad.tl.vertice.x > screen.width)
                   || (quad.tl.vertice.y > screen.height && quad.tr.vertice.y > screen.height)
                   || (quad.bl.vertice.y < 0 && quad.br.vertice.y < 0)
                   || (quad.tr.vertice.x < 0 && quad.br.vertice.x < 0))
                {
                    return true;
                }
            }
        }
        else if(quad.bl.vertice.x > quad.br.vertice.x || quad.tl.vertice.x > quad.tr.vertice.x)
        {
            if(quad.bl.vertice.y < quad.tl.vertice.y || quad.br.vertice.y < quad.tr.vertice.y)
            {
                if((quad.tr.vertice.x > screen.width && quad.br.vertice.x > screen.width)
                      || (quad.bl.vertice.y > screen.height && quad.br.vertice.y > screen.height)
                      || (quad.tr.vertice.y < 0 && quad.tl.vertice.y < 0)
                      || (quad.tl.vertice.x < 0 && quad.bl.vertice.x < 0))
                {
                    return true;
                }
            }
            else if(quad.bl.vertice.y > quad.tl.vertice.y || quad.br.vertice.y > quad.tr.vertice.y)
            {
                if((quad.tr.vertice.x > screen.width && quad.br.vertice.x > screen.width)
                   || (quad.tl.vertice.y > screen.height && quad.tr.vertice.y > screen.height)
                   || (quad.bl.vertice.y < 0 && quad.br.vertice.y < 0)
                   || (quad.tl.vertice.x < 0 && quad.bl.vertice.x < 0))
                {
                    return true;
                }
            }
        }

        //There is one more else if we need to check for and that is if all the points
        //Have been collapsed onto each other, even though this is technically not offscreen
        //It can still save memory by not trying to draw the collapsed quad.
        else if(quad.bl.vertice.x == 0 && quad.bl.vertice.y == 0 && quad.bl.vertice.z == 0
                && quad.br.vertice.x == 0 && quad.br.vertice.y == 0 && quad.br.vertice.z == 0
                && quad.tl.vertice.x == 0 && quad.tl.vertice.y == 0 && quad.tl.vertice.z == 0
                && quad.tr.vertice.x == 0 && quad.tr.vertice.y == 0 && quad.tr.vertice.z == 0)
        {
            return true;
        }
       
       return false;
    }

    private FloatBuffer getVertices()
    {
        FloatBuffer tempBuffer = FloatBuffer.allocate(this.totalVisible * 12);

        for(int i = 0; i < this.totalQuads; i++)
        {
            QuadTexture3F quad = this.quads.get(i);

            if(!this.isQuadOffScreen(quad))
            {
                tempBuffer.put(quad.bl.vertice.x);
                tempBuffer.put(quad.bl.vertice.y);
                tempBuffer.put(quad.bl.vertice.z);

                tempBuffer.put(quad.br.vertice.x);
                tempBuffer.put(quad.br.vertice.y);
                tempBuffer.put(quad.br.vertice.z);

                tempBuffer.put(quad.tl.vertice.x);
                tempBuffer.put(quad.tl.vertice.y);
                tempBuffer.put(quad.tl.vertice.z);

                tempBuffer.put(quad.tr.vertice.x);
                tempBuffer.put(quad.tr.vertice.y);
                tempBuffer.put(quad.tr.vertice.z);
            }
        }

        return tempBuffer;
    }

    protected FloatBuffer getColors()
    {
        FloatBuffer tempBuffer = FloatBuffer.allocate(this.totalVisible * 16);

        for(int i = 0; i < this.totalQuads; i++)
        {
            QuadTexture3F quad = this.quads.get(i);

            if(!this.isQuadOffScreen(quad))
            {
                tempBuffer.put(quad.bl.color.getRed());
                tempBuffer.put(quad.bl.color.getGreen());
                tempBuffer.put(quad.bl.color.getBlue());
                tempBuffer.put(quad.bl.color.getAlpha());

                tempBuffer.put(quad.br.color.getRed());
                tempBuffer.put(quad.br.color.getGreen());
                tempBuffer.put(quad.br.color.getBlue());
                tempBuffer.put(quad.br.color.getAlpha());

                tempBuffer.put(quad.tl.color.getRed());
                tempBuffer.put(quad.tl.color.getGreen());
                tempBuffer.put(quad.tl.color.getBlue());
                tempBuffer.put(quad.tl.color.getAlpha());

                tempBuffer.put(quad.tr.color.getRed());
                tempBuffer.put(quad.tr.color.getGreen());
                tempBuffer.put(quad.tr.color.getBlue());
                tempBuffer.put(quad.tr.color.getAlpha());
            }
        }

        return tempBuffer;        
    }

    protected FloatBuffer getTextureCoordinates()
    {
        FloatBuffer tempBuffer = FloatBuffer.allocate(this.totalVisible * 8);

        for(int i = 0; i < this.totalQuads; i++)
        {
             QuadTexture3F quad = this.quads.get(i);

             if(!this.isQuadOffScreen(quad))
             {
                tempBuffer.put(quad.bl.coords.x);
                tempBuffer.put(quad.bl.coords.y);

                tempBuffer.put(quad.br.coords.x);
                tempBuffer.put(quad.br.coords.y);

                tempBuffer.put(quad.tl.coords.x);
                tempBuffer.put(quad.tl.coords.y);

                tempBuffer.put(quad.tr.coords.x);
                tempBuffer.put(quad.tr.coords.y);
             }
        }

        return tempBuffer;
    }

    public CCTexture2D getTexture()
    {
        return this.texture;
    }

    public void setTexture(CCTexture2D texture)
    {
        this.texture = texture;
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
                CCTextureAtlas atlas = (CCTextureAtlas) obj;

                if(this.totalQuads != atlas.totalQuads || this.capacity != atlas.capacity
                   || !this.quads.equals(atlas.quads) || !this.texture.equals(atlas.texture)
                   || !this.indices.equals(atlas.indices) || !this.vboBuffers.equals(atlas.vboBuffers))
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
