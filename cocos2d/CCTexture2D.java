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

import org.lwjgl.opengl.*;
import org.lwjgl.util.glu.MipMap;
import cocos2d.support.*;
import cocos2d.support.CCTypes.*;
import java.awt.geom.*;
import java.awt.image.*;
import java.awt.*;
import java.io.*;
import java.nio.*;
import java.net.*;
import java.awt.color.*;
import java.util.*;

public class CCTexture2D
{
    protected int id;

    protected Dimension imageSize;
    protected int width;
    protected int height;
    protected float heightRatio;
    protected float widthRatio;

    protected ByteBuffer texture;
    protected CCImage image;
    
    protected static ColorModel glAlphaColorModel;
    protected static ColorModel glColorModel;

    static {
        glAlphaColorModel = new ComponentColorModel(ColorSpace.getInstance(ColorSpace.CS_sRGB),
                                     new int[] {8,8,8,8},
                                     true, false,
                                     ComponentColorModel.TRANSLUCENT,
                                     DataBuffer.TYPE_BYTE);
        
        glColorModel = new ComponentColorModel(ColorSpace.getInstance(ColorSpace.CS_sRGB),
                                                new int[] {8,8,8,0},
                                                false, false,
                                                ComponentColorModel.OPAQUE,
                                                DataBuffer.TYPE_BYTE);
    }

    public CCTexture2D(CCImage image)
    {
        this.imageSize = new Dimension(image.getBufferedImage().getWidth(), image.getBufferedImage().getHeight());
        this.image = image;
        this.width = CCMath.getPowerOfTwo(this.imageSize.width);
        this.height = CCMath.getPowerOfTwo(this.imageSize.height);

        this.widthRatio = this.imageSize.width / (float) this.width;
        this.heightRatio = this.imageSize.height / (float) this.height;
        this.id = -1;
        this.createTexture();
    }

    public CCTexture2D(String filepath) throws Exception
    {
        CCImage imagev = new CCImage(filepath);
        
        this.imageSize = new Dimension(imagev.getBufferedImage().getWidth(), imagev.getBufferedImage().getHeight());

        this.image = imagev;

        this.width = CCMath.getPowerOfTwo(this.imageSize.width);
        this.height = CCMath.getPowerOfTwo(this.imageSize.height);

        this.widthRatio = this.imageSize.width / (float) this.width;
        this.heightRatio = this.imageSize.height / (float) this.height;
        this.id = -1;
        this.createTexture();
    }

    public CCTexture2D(URL filePath) throws IOException
    {
         CCImage imagev = new CCImage(filePath);

        this.imageSize = new Dimension(imagev.getBufferedImage().getWidth(), imagev.getBufferedImage().getHeight());

        this.image = imagev;

        this.width = CCMath.getPowerOfTwo(this.imageSize.width);
        this.height = CCMath.getPowerOfTwo(this.imageSize.height);

        this.widthRatio = this.imageSize.width / (float) this.width;
        this.heightRatio = this.imageSize.height / (float) this.height;
        this.id = -1;
        this.createTexture();
    }

    public CCTexture2D(File textureFile) throws IOException
    {
        CCImage imagev = new CCImage(textureFile);

        this.imageSize = new Dimension(imagev.getBufferedImage().getWidth(), imagev.getBufferedImage().getHeight());

        this.image = imagev;

        this.width = CCMath.getPowerOfTwo(this.imageSize.width);
        this.height = CCMath.getPowerOfTwo(this.imageSize.height);

        this.widthRatio = this.imageSize.width / (float) this.width;
        this.heightRatio = this.imageSize.height / (float) this.height;
        this.id = -1;
        this.createTexture();
    }

    //Copy Constructor
    public CCTexture2D(CCTexture2D original)
    {
        if(original == null) throw new NullPointerException();
        
        this.image = original.image.clone();

        this.imageSize = new Dimension(this.image.getBufferedImage().getWidth(), this.image.getBufferedImage().getHeight());

        this.width = CCMath.getPowerOfTwo(this.imageSize.width);
        this.height = CCMath.getPowerOfTwo(this.imageSize.height);

        this.widthRatio = this.imageSize.width / (float) this.width;
        this.heightRatio = this.imageSize.height / (float) this.height;
        this.id = -1;
        this.createTexture();
    }

    public CCTexture2D clone()
    {
        return new CCTexture2D(this);
    }

    protected void createTexture()
    {
        this.convertToPowerOfTwo();

        if(this.texture != null)
        {
            IntBuffer tmp = createIntBuffer(1);
            GL11.glGenTextures(tmp);
            this.id = tmp.get(0);
            this.bind();

            int src = GL11.GL_RGBA;

            if(!this.image.getBufferedImage().getColorModel().hasAlpha())
            {
                src = GL11.GL_RGB;
            }

            GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, src, this.width, this.height,0, src, GL11.GL_UNSIGNED_BYTE, this.texture);
        }
    }

    protected IntBuffer createIntBuffer(int size)
    {
        ByteBuffer temp = ByteBuffer.allocateDirect(size * 4).order(ByteOrder.nativeOrder());
        return temp.asIntBuffer();
    }
    
    protected void convertToPowerOfTwo()
    {
        BufferedImage imagev;
        WritableRaster raster;

        if(this.image.getBufferedImage().getColorModel().hasAlpha())
        {
            raster = Raster.createInterleavedRaster(DataBuffer.TYPE_BYTE, this.width, this.height, 4, null);
            imagev = new BufferedImage(glAlphaColorModel, raster, false, new Hashtable());
        }
        else
        {
            raster = Raster.createInterleavedRaster(DataBuffer.TYPE_BYTE, this.width, this.height, 3, null);
            imagev = new BufferedImage(glColorModel, raster, false, new Hashtable());
        }

        Graphics g = imagev.createGraphics();
        g.setColor(new Color(0f, 0f, 0f, 0f));
        g.fillRect(0,0, this.width, this.height);
        g.drawImage(this.image.getBufferedImage(), 0, 0, null);
        
        this.createTextureBuffer(imagev);
    }

    protected void createTextureBuffer(BufferedImage image)
    {
        byte[] bytes = ((DataBufferByte) image.getRaster().getDataBuffer()).getData();

        this.texture = ByteBuffer.allocateDirect(bytes.length);
        this.texture.order(ByteOrder.nativeOrder());
        this.texture.put(bytes);
        this.texture.flip();
        
    }

    public void bind()
    {
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, this.id);
    }

    public void destroy()
    {
        GL11.glDeleteTextures(this.id);
    }

    public int getId()
    {
        return this.id;
    }

    public int getWidth()
    {
        return this.width;
    }
    
    public int getHeight()
    {
        return this.height;
    }
    
    public Dimension getImageSize()
    {
        return this.imageSize;
    }

    public float getRatioX()
    {
        return this.widthRatio;
    }

    public float getRatioY()
    {
        return this.heightRatio;
    }

    public CCImage getImage()
    {
        return this.image;
    }

    /**
     * Gets the actual bytebuffer of the texture
     * @return ByteBuffer
     */
    public ByteBuffer getTexture()
    {
        return this.texture;
    }

    /**
     * Draws a quad at the specified point
     * With the texture
     * @param Point2D point
     */
    public void drawAtPoint(Point2D point, boolean center)
    {
        float[] coordinates = {0.0f, this.heightRatio, this.widthRatio,
                                this.heightRatio, 0.0f, 0.0f, this.widthRatio, 0.0f};
        float[] vertices = new float[12];

        if(center)
        {
            vertices[0] = -this.width / 2 + (float)point.getX();
            vertices[1] = -this.height / 2 + (float)point.getY();
            vertices[2] = 0.0f;
            vertices[3] = this.width / 2 + (float)point.getX();
            vertices[4] = -this.height / 2 + (float)point.getY();
            vertices[5] = 0.0f;
            vertices[6] = -this.width / 2 + (float)point.getX();
            vertices[7] = this.height / 2 + (float)point.getY();
            vertices[8] = 0.0f;
            vertices[9] = this.width / 2 + (float)point.getX();
            vertices[10] = this.height / 2 + (float)point.getY();
            vertices[11] = 0.0f;
        }
        else
        {
            vertices[0] = (float)point.getX();
            vertices[1] = (float)point.getY();
            vertices[2] = 0.0f;
            vertices[3] = this.width + (float)point.getX();
            vertices[4] = (float)point.getY();
            vertices[5] = 0.0f;
            vertices[6] = (float)point.getX();
            vertices[7] = this.height + (float)point.getY();
            vertices[8] = 0.0f;
            vertices[9] = this.width  + (float)point.getX();
            vertices[10] = this.height  + (float)point.getY();
            vertices[11] = 0.0f;
        }

        FloatBuffer verticeBuffer = FloatBuffer.allocate(vertices.length);
        verticeBuffer.put(vertices);
        FloatBuffer coordinatesBuffer = FloatBuffer.allocate(coordinates.length);
        coordinatesBuffer.put(coordinates);

        this.bind();
        GL11.glVertexPointer(3, 0, verticeBuffer);
        GL11.glTexCoordPointer(2, 0, coordinatesBuffer);
        GL11.glDrawArrays(GL11.GL_TRIANGLE_STRIP, 0, 4);
    }

    /**
     * Draws a quad based on the Rectangle given
     * @param Rectangle rect
     */
    public void drawInRect(Rectangle rect)
    {
        float[] coordinates = {0.0f, this.heightRatio, this.widthRatio,
                                this.heightRatio, 0.0f, 0.0f, this.widthRatio, 0.0f};
        float[] vertices = new float[12];

            vertices[0] = rect.x;
            vertices[1] = rect.y;
            vertices[2] = 0.0f;
            vertices[3] = rect.x + (float)rect.getWidth();
            vertices[4] = rect.y;
            vertices[5] = 0.0f;
            vertices[6] = rect.x;
            vertices[7] = rect.y + (float)rect.getHeight();
            vertices[8] = 0.0f;
            vertices[9] = rect.x + (float)rect.getWidth();
            vertices[10] = rect.y + (float)rect.getHeight();
            vertices[11] = 0.0f;

        FloatBuffer verticeBuffer = FloatBuffer.allocate(vertices.length);
        verticeBuffer.put(vertices);
        FloatBuffer coordinatesBuffer = FloatBuffer.allocate(coordinates.length);
        coordinatesBuffer.put(coordinates);

        this.bind();
        GL11.glVertexPointer(3, 0, verticeBuffer);
        GL11.glTexCoordPointer(2, 0, coordinatesBuffer);
        GL11.glDrawArrays(GL11.GL_TRIANGLE_STRIP, 0, 4);
    }

    /**
     * The device must have support for gluBuild2DMipmaps
     */
    public void gluGenerateMipMap()
    {
        this.bind();
        MipMap.gluBuild2DMipmaps(GL11.GL_TEXTURE_2D, 4, this.width, this.height, GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, texture);
    }

    /**
     * Can only generate mipmap with opengl 1.4 or greater
     */
    public void glGenerateMipMap()
    {
        this.bind();
        GL11.glTexParameterf(GL11.GL_TEXTURE_2D, GL14.GL_GENERATE_MIPMAP, GL11.GL_TRUE);
    }

    public void setTextureParameters(int minFilter, int magFilter, int wrapS, int wrapT)
    {
        this.bind();
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, minFilter);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, magFilter);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_S, wrapS);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_T, wrapT);
    }

    /**
     * Both AntiAlias and Alias need OpenGL 1.2 or greater to work properly
     */
    public void setAlias()
    {
        this.setTextureParameters(GL11.GL_NEAREST, GL11.GL_NEAREST, GL12.GL_CLAMP_TO_EDGE, GL12.GL_CLAMP_TO_EDGE);
    }

    public void setAntiAlias()
    {
        this.setTextureParameters(GL11.GL_LINEAR, GL11.GL_LINEAR, GL12.GL_CLAMP_TO_EDGE, GL12.GL_CLAMP_TO_EDGE);
    }

    public void finalize()
    {
        this.destroy();
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
                CCTexture2D tex = (CCTexture2D) obj;

                if(this.height != tex.height || this.heightRatio != tex.heightRatio
                        || this.id != tex.id || !this.imageSize.equals(tex.imageSize)
                        || !this.texture.equals(tex.texture) || this.width != tex.width
                        || this.widthRatio != tex.widthRatio)
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
