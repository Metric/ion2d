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
import org.lwjgl.opengl.*;
import java.nio.*;
import java.awt.Dimension;

public class CCTiledGrid3D extends CCGridBase
{
    protected FloatBuffer vertices;
    protected FloatBuffer originalVertices;
    protected FloatBuffer textureCoordinates;
    protected IntBuffer indices;

    protected int totalQuads;

    //Default Constructor
    public CCTiledGrid3D() throws Exception
    {
        this(new CCGridSize(10,10));
    }

    //Secondary Constructor
    public CCTiledGrid3D(CCGridSize size) throws Exception
    {
        super(size);

        this.totalQuads = size.x * size.y;

        //Allocate enough space in the float buffer for storing the vertices
        //Size of float buffer should be X+1 * Y+1 * 3
        //We multiple by 3 to account for each vertex x, y, and z
        this.vertices = FloatBuffer.allocate(this.totalQuads*12);
        this.originalVertices = FloatBuffer.allocate(this.vertices.capacity());

        //Same thing for texture coordinates but times 2 instead
        this.textureCoordinates = FloatBuffer.allocate(this.totalQuads*8);

        //Allocate enough space for the indices
        this.indices = IntBuffer.allocate(this.totalQuads * 36);

        this.calculateVertexPoints();
    }

    /**
     * Draw the Grid3D to the output
     */
    public void blit()
    {
 	// Default GL states: GL_TEXTURE_2D, GL_VERTEX_ARRAY, GL_COLOR_ARRAY, GL_TEXTURE_COORD_ARRAY
	// Needed states: GL_TEXTURE_2D, GL_VERTEX_ARRAY, GL_TEXTURE_COORD_ARRAY
	// Unneeded states: GL_COLOR_ARRAY
        GL11.glDisableClientState(GL11.GL_COLOR_ARRAY);

        GL11.glVertexPointer(3, 0, this.vertices);
        GL11.glTexCoordPointer(2, 0, this.textureCoordinates);
        GL11.glDrawElements(GL11.GL_QUADS, this.indices);

        //Restore GL Default State
        GL11.glEnableClientState(GL11.GL_COLOR_ARRAY);
    }

    /**
     * Calculates the vertex positions for the grid
     */
    protected void calculateVertexPoints()
    {
        int x, y;

        Dimension pixelSize = this.texture.getImageSize();

        for(x = 0; x < this.gridSize.x; x++)
        {
            for(y = 0; y < this.gridSize.y; y++)
            {
                float x1 = x * this.step.x;
                float x2 = x1 + this.step.x;
                float y1 = y * this.step.y;
                float y2 = y1 + this.step.y;

                this.vertices.put(x1);
                this.vertices.put(y1);
                this.vertices.put(0);

                this.vertices.put(x2);
                this.vertices.put(y1);
                this.vertices.put(0);

                this.vertices.put(x1);
                this.vertices.put(y2);
                this.vertices.put(0);

                this.vertices.put(x2);
                this.vertices.put(y2);
                this.vertices.put(0);

                float ax = x1 / pixelSize.width;
                float bx = x2 / pixelSize.width;
                float ay = y1 / pixelSize.height;
                float by = y2 / pixelSize.height;

                this.textureCoordinates.put(ax);
                this.textureCoordinates.put(ay);

                this.textureCoordinates.put(bx);
                this.textureCoordinates.put(ay);

                this.textureCoordinates.put(ax);
                this.textureCoordinates.put(by);

                this.textureCoordinates.put(bx);
                this.textureCoordinates.put(by);
            }
        }

        for(x = 0; x < this.totalQuads; x++)
        {
            this.indices.put(x*6, x*4);
            this.indices.put(x*6+1, x*4+1);
            this.indices.put(x*6+2, x*4+2);
            this.indices.put(x*6+3, x*4+1);
            this.indices.put(x*6+4, x*4+2);
            this.indices.put(x*6+5, x*4+3);
        }

        this.vertices.rewind();
        this.textureCoordinates.rewind();
        this.originalVertices.put(this.vertices);
    }

    /**
     * Gets the Quad from the specified x,y position
     * @param Vertex2F position
     * @return Quad3
     */
    public Quad3 getTile(Vertex2F position)
    {
        int index = Math.round(this.gridSize.y * position.x + position.y) * 12;

        Vertex3F bl = new Vertex3F(this.vertices.get(index), this.vertices.get(index+1), this.vertices.get(index+2));
        Vertex3F br = new Vertex3F(this.vertices.get(index+3), this.vertices.get(index+4), this.vertices.get(index+5));
        Vertex3F tl = new Vertex3F(this.vertices.get(index+6), this.vertices.get(index+7), this.vertices.get(index+8));
        Vertex3F tr = new Vertex3F(this.vertices.get(index+9), this.vertices.get(index+10), this.vertices.get(index+11));
        
        Quad3 quad = new Quad3(tl, tr, bl, br);

        return quad;
    }

    /**
     * Gets the original Quad from the specified x, y position
     * @param Vertex2F position
     * @return Quad3
     */
    public Quad3 getOriginalTile(Vertex2F position)
    {
        int index = Math.round(this.gridSize.y * position.x + position.y) * 12;

        Vertex3F bl = new Vertex3F(this.originalVertices.get(index), this.originalVertices.get(index+1), this.originalVertices.get(index+2));
        Vertex3F br = new Vertex3F(this.originalVertices.get(index+3), this.originalVertices.get(index+4), this.originalVertices.get(index+5));
        Vertex3F tl = new Vertex3F(this.originalVertices.get(index+6), this.originalVertices.get(index+7), this.originalVertices.get(index+8));
        Vertex3F tr = new Vertex3F(this.originalVertices.get(index+9), this.originalVertices.get(index+10), this.originalVertices.get(index+11));

        Quad3 quad = new Quad3(tl, tr, bl, br);

        return quad;
    }

    /**
     * Sets the Quad at the given position
     * @param Vertex2F position
     * @param Quad3 tile
     */
    public void setTile(Vertex2F position, Quad3 tile)
    {
        int index = Math.round(this.gridSize.y * position.x + position.y) * 12;

        this.vertices.put(index, tile.bl.x);
        this.vertices.put(index+1, tile.bl.y);
        this.vertices.put(index+2, tile.bl.z);

        this.vertices.put(index+3, tile.br.x);
        this.vertices.put(index+4, tile.br.y);
        this.vertices.put(index+5, tile.br.z);

        this.vertices.put(index+6, tile.tl.x);
        this.vertices.put(index+7, tile.tl.y);
        this.vertices.put(index+8, tile.tl.z);

        this.vertices.put(index+9, tile.tr.x);
        this.vertices.put(index+10, tile.tr.y);
        this.vertices.put(index+11, tile.tr.z);
    }

    /**
     * Reset the vertices position to the original state
     */
    public void reuse()
    {
        if(this.reuseGrid > 0)
        {
            this.vertices.clear();
            this.originalVertices.rewind();
            this.vertices.put(this.originalVertices);
            this.reuseGrid--;
        }
    }
}
