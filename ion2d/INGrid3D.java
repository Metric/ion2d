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
import org.lwjgl.opengl.*;
import java.nio.*;
import java.awt.Dimension;
import org.lwjgl.BufferUtils;

public class INGrid3D extends INGridBase
{
    protected FloatBuffer vertices;
    protected FloatBuffer originalVertices;
    protected FloatBuffer textureCoordinates;
    protected IntBuffer indices;

    protected int totalTriangles;

    /**
     * Default constructor
     * @throws Exception
     */
    public INGrid3D() throws Exception
    {
        this(new GridSize(10,10));
    }

    /**
     * Copy constructor
     * @param INGrid3D original
     */
    public INGrid3D(INGrid3D original) throws Exception
    {
        if(original == null) throw new NullPointerException();
        
        this.active = original.active;
        this.grabber = original.grabber.clone();
        this.gridSize = original.gridSize.clone();

        this.indices = BufferUtils.createIntBuffer(original.indices.limit());
        this.indices.put(original.indices);

        this.originalVertices = BufferUtils.createFloatBuffer(original.originalVertices.limit());
        this.originalVertices.put(original.originalVertices);
        
        this.reuseGrid = original.reuseGrid;
        this.step = original.step.clone();
        this.texture = original.texture.clone();

        this.textureCoordinates = BufferUtils.createFloatBuffer(original.textureCoordinates.limit());
        this.textureCoordinates.put(original.textureCoordinates);


        this.totalTriangles = original.totalTriangles;

        this.vertices = BufferUtils.createFloatBuffer(original.vertices.limit());
        this.vertices.put(original.vertices);
    }

    /**
     * Clone Override
     * @return INGrid3D
     */
    public INGrid3D clone()
    {
        try
        {
            return new INGrid3D(this);
        }
        catch (Exception e)
        {
            System.out.println(e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Secondary Constructor
     * @param CCGridSize size
     * @throws Exception
     */
    public INGrid3D(GridSize size) throws Exception
    {
        super(size);

        this.totalTriangles = size.x * size.y;

        //Allocate enough space in the float buffer for storing the vertices
        //Size of float buffer should be X+1 * Y+1 * 3
        //We multiple by 12 to account for each vertex x, y, and z plus 4 vertex points
        this.vertices = BufferUtils.createFloatBuffer(this.totalTriangles * 12);
        this.originalVertices = BufferUtils.createFloatBuffer(this.vertices.limit());
   
        //Same thing for texture coordinates but times 2 instead
        this.textureCoordinates = BufferUtils.createFloatBuffer(this.totalTriangles * 8);

        //Allocate enough space for the indices
        this.indices = BufferUtils.createIntBuffer(this.totalTriangles * 36);

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
        GL11.glDrawElements(GL11.GL_TRIANGLES, this.indices);

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

        for(x = 0; x < this.totalTriangles; x++)
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
        this.originalVertices.rewind();
    }

    /**
     * Gets the vertex from the specified x,y position
     * @param Vertex2F position
     * @return Vertex2F
     */
    public Vertex3F getVertex(Vertex2F position)
    {
        int index = Math.round((position.x * this.gridSize.y + position.y) * 3);
        Vertex3F vertex = new Vertex3F(this.vertices.get(index), this.vertices.get(index+1), this.vertices.get(index+2));

        return vertex;
    }

    /**
     * Gets the original Vertex from the specified x, y position
     * @param Vertex2F position
     * @return Vertex2F
     */
    public Vertex3F getOriginalVertex(Vertex2F position)
    {
        int index = Math.round((position.x * this.gridSize.y + position.y) * 3);
        Vertex3F vertex = new Vertex3F(this.originalVertices.get(index), this.originalVertices.get(index+1), this.originalVertices.get(index+2));

        return vertex;        
    }

    /**
     * Sets the vertex at the given position
     * @param Vertex2F position
     * @param Vertex3F vertex
     */
    public void setVertex(Vertex2F position, Vertex3F vertex)
    {
        int index = Math.round((position.x * this.gridSize.y + position.y) * 3);

        this.vertices.put(index, vertex.x);
        this.vertices.put(index+1, vertex.y);
        this.vertices.put(index+2, vertex.z);
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
