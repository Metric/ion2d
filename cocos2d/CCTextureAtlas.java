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

public class CCTextureAtlas
{
    protected int totalQuads;
    protected int capacity;

    protected Vector<QuadTexture3F> quads;
    protected CCTexture2D texture;
    protected IntBuffer indices;
    protected IntBuffer vboBuffers;

    public CCTextureAtlas() throws Exception
    {
        throw new Exception("CCTextureAtlas does not respond to default constructor");
    }

    public CCTextureAtlas(String filePath, int capacity)
    {
        this(CCTextureCache.addImage(filePath), filePath, capacity);
    }

    public CCTextureAtlas(File file, int capacity)
    {
        this(CCTextureCache.addImage(file, file.getName()), file.getName(), capacity);
    }

    public CCTextureAtlas(URL filePath, int capacity)
    {
        this(CCTextureCache.addImage(filePath, filePath.toString()), filePath.toString(), capacity);
    }

    public CCTextureAtlas(CCTexture2D texture, String key, int capacity)
    {
        this.quads = new Vector<QuadTexture3F>(capacity);
        this.texture = texture;
        this.capacity = capacity;
        this.totalQuads = 0;

        this.vboBuffers = IntBuffer.allocate(2);
        GL15.glGenBuffers(this.vboBuffers);

        this.initIndices();
    }

    private void initIndices()
    {
        this.indices = IntBuffer.allocate(this.capacity * 6 * 6);

        for(int i = 0; i < this.capacity; i++)
        {
            this.indices.put(i*6, i*4);
            this.indices.put(i*6+1, i*4+1);
            this.indices.put(i*6+2, i*4+2);
            this.indices.put(i*6+3, i*4+3);
            this.indices.put(i*6+4, i*4+2);
            this.indices.put(i*6+5, i*4+1);
        }

        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, this.vboBuffers.get(0));
        GL15.glBufferData(GL15.GL_ARRAY_BUFFER, this.getVertices(), GL15.GL_DYNAMIC_DRAW);
        GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, this.vboBuffers.get(1));
        GL15.glBufferData(GL15.GL_ELEMENT_ARRAY_BUFFER, this.indices, GL15.GL_STATIC_DRAW);
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
        GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, 0);
    }

    protected FloatBuffer getVertices()
    {
        FloatBuffer tempBuffer = FloatBuffer.allocate(this.totalQuads * 4 * 3);

        for(int i = 0; i < this.totalQuads; i++)
        {
            QuadTexture3F quad = this.quads.get(i);
            
            tempBuffer.put(quad.tl.vertice.x);
            tempBuffer.put(quad.tl.vertice.y);
            tempBuffer.put(quad.tl.vertice.z);
            
            tempBuffer.put(quad.tr.vertice.x);
            tempBuffer.put(quad.tr.vertice.y);
            tempBuffer.put(quad.tr.vertice.z);

            tempBuffer.put(quad.br.vertice.x);
            tempBuffer.put(quad.br.vertice.y);
            tempBuffer.put(quad.br.vertice.z);

            tempBuffer.put(quad.bl.vertice.x);
            tempBuffer.put(quad.bl.vertice.y);
            tempBuffer.put(quad.bl.vertice.z);
        }

        return tempBuffer;
    }

    protected FloatBuffer getColors()
    {
        FloatBuffer tempBuffer = FloatBuffer.allocate(this.totalQuads * 4 * 4);

        for(int i = 0; i < this.totalQuads; i++)
        {
            QuadTexture3F quad = this.quads.get(i);
            
            tempBuffer.put(quad.tl.color.getRed());
            tempBuffer.put(quad.tl.color.getGreen());
            tempBuffer.put(quad.tl.color.getBlue());
            tempBuffer.put(quad.tl.color.getAlpha());
            
            tempBuffer.put(quad.tr.color.getRed());
            tempBuffer.put(quad.tr.color.getGreen());
            tempBuffer.put(quad.tr.color.getBlue());
            tempBuffer.put(quad.tr.color.getAlpha());

            tempBuffer.put(quad.br.color.getRed());
            tempBuffer.put(quad.br.color.getGreen());
            tempBuffer.put(quad.br.color.getBlue());
            tempBuffer.put(quad.br.color.getAlpha());

            tempBuffer.put(quad.bl.color.getRed());
            tempBuffer.put(quad.bl.color.getGreen());
            tempBuffer.put(quad.bl.color.getBlue());
            tempBuffer.put(quad.bl.color.getAlpha());  
        }

        return tempBuffer;        
    }

    protected FloatBuffer getTextureCoordinates()
    {
        FloatBuffer tempBuffer = FloatBuffer.allocate(this.totalQuads * 4 * 2);

        for(int i = 0; i < this.totalQuads; i++)
        {
             QuadTexture3F quad = this.quads.get(i);
            tempBuffer.put(quad.tl.coords.x);
            tempBuffer.put(quad.tl.coords.y);

            tempBuffer.put(quad.tr.coords.x);
            tempBuffer.put(quad.tr.coords.y);  

            tempBuffer.put(quad.br.coords.x);
            tempBuffer.put(quad.br.coords.y);   

            tempBuffer.put(quad.bl.coords.x);
            tempBuffer.put(quad.bl.coords.y);         
        }

        return tempBuffer;
    }

    public void updateQuad(QuadTexture3F quad, int index)
    {
        if(index >= 0 && index < this.capacity)
        {
            this.quads.set(index, quad);
        }
    }

    public void insertQuad(QuadTexture3F quad, int index)
    {
        if(index >= 0 && index < this.capacity)
        {
            this.totalQuads++;
            this.quads.add(index, quad);
        }
    }

    public void removeQuad(int index)
    {
        this.quads.remove(index);
        this.totalQuads--;
    }

    public void removeAllQuads()
    {
        this.quads.clear();
        this.totalQuads = 0;
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
    }

    public int capacity()
    {
        return this.capacity;
    }

    public int getTotalQuads()
    {
        return this.totalQuads;
    }

    public void drawQuads(int n)
    {
        this.texture.bind();
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, this.vboBuffers.get(0));
        GL11.glVertexPointer(3, 0, this.getVertices());
        
        GL11.glColorPointer(4, 0, this.getColors());
        
        GL11.glTexCoordPointer(2, 0, this.getTextureCoordinates());

        GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, this.vboBuffers.get(1));
        GL12.glDrawRangeElements(GL11.GL_QUADS, 0, n*6, this.indices.limit(), GL11.GL_UNSIGNED_INT, 0);

        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
        GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, 0);
    }

    public void drawQuads()
    {
        this.drawQuads(this.totalQuads);
    }
}
