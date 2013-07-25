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

import org.lwjgl.opengl.*;
import org.lwjgl.util.glu.MipMap;
import ion2d.support.*;
import ion2d.support.INTypes.*;
import java.awt.geom.*;
import java.awt.image.*;
import java.awt.*;
import java.io.*;
import java.nio.*;
import java.net.*;
import java.awt.color.*;
import java.util.*;
import org.lwjgl.BufferUtils;

public class INTexture2D
{
    protected int id;

    protected Dimension imageSize;
    protected int width;
    protected int height;
    protected float heightRatio;
    protected float widthRatio;

    protected ByteBuffer texture;
    protected INImage image;
    
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

    /**
     * Default constructor
     * If used nothing will be initialized and everything will be set
     * to the default value for its type.
     * Everything will be null and no texture will actually be created.
     * Please use one of the other constructors.
     */
    public INTexture2D()
    {
    }

    /**
     * Creates a INTexture2D from a INImage object
     * @param INImage image
     */
    public INTexture2D(INImage image)
    {
        this.imageSize = new Dimension(image.getBufferedImage().getWidth(), image.getBufferedImage().getHeight());
        this.image = image;
        this.width = INMath.getPowerOfTwo(this.imageSize.width);
        this.height = INMath.getPowerOfTwo(this.imageSize.height);

        this.widthRatio = this.imageSize.width / (float) this.width;
        this.heightRatio = this.imageSize.height / (float) this.height;
        this.id = -1;
        this.createTexture();
    }

    /**
     * Creates a INTexture2D from a string file path
     *
     * @param String filepath
     * @throws Exception
     */
    public INTexture2D(String filepath) throws Exception
    {
        INImage imagev = new INImage(filepath);
        
        this.imageSize = new Dimension(imagev.getBufferedImage().getWidth(), imagev.getBufferedImage().getHeight());

        this.image = imagev;

        this.width = INMath.getPowerOfTwo(this.imageSize.width);
        this.height = INMath.getPowerOfTwo(this.imageSize.height);

        this.widthRatio = this.imageSize.width / (float) this.width;
        this.heightRatio = this.imageSize.height / (float) this.height;
        this.id = -1;
        this.createTexture();
    }

    /**
     * Creates a INTexture2D from a URL file path
     * @param URL filePath
     * @throws IOException
     */
    public INTexture2D(URL filePath) throws IOException
    {
         INImage imagev = new INImage(filePath);

        this.imageSize = new Dimension(imagev.getBufferedImage().getWidth(), imagev.getBufferedImage().getHeight());

        this.image = imagev;

        this.width = INMath.getPowerOfTwo(this.imageSize.width);
        this.height = INMath.getPowerOfTwo(this.imageSize.height);

        this.widthRatio = this.imageSize.width / (float) this.width;
        this.heightRatio = this.imageSize.height / (float) this.height;
        this.id = -1;
        this.createTexture();
    }

    /**
     * Creates a INTexture2D from a File object
     * @param File textureFile
     * @throws IOException
     */
    public INTexture2D(File textureFile) throws IOException
    {
        INImage imagev = new INImage(textureFile);

        this.imageSize = new Dimension(imagev.getBufferedImage().getWidth(), imagev.getBufferedImage().getHeight());

        this.image = imagev;

        this.width = INMath.getPowerOfTwo(this.imageSize.width);
        this.height = INMath.getPowerOfTwo(this.imageSize.height);

        this.widthRatio = this.imageSize.width / (float) this.width;
        this.heightRatio = this.imageSize.height / (float) this.height;
        this.id = -1;
        this.createTexture();
    }

    /**
     * Copy Constructor
     */
    public INTexture2D(INTexture2D original)
    {
        if(original == null) throw new NullPointerException();
        
        this.image = original.image.clone();

        this.imageSize = new Dimension(this.image.getBufferedImage().getWidth(), this.image.getBufferedImage().getHeight());

        this.width = INMath.getPowerOfTwo(this.imageSize.width);
        this.height = INMath.getPowerOfTwo(this.imageSize.height);

        this.widthRatio = this.imageSize.width / (float) this.width;
        this.heightRatio = this.imageSize.height / (float) this.height;
        this.id = -1;
        this.createTexture();
    }

    /**
     * Clone override
     * @return INTexture2D
     */
    public INTexture2D clone()
    {
        return new INTexture2D(this);
    }

    /**
     * Creates the actual OpenGL texture to memory
     */
    protected void createTexture()
    {
        this.convertToPowerOfTwo();

        if(this.texture != null)
        {
            IntBuffer tmp = BufferUtils.createIntBuffer(1);
            GL11.glGenTextures(tmp);
            this.id = tmp.get(0);
            this.bind();

            int src = GL11.GL_RGBA;

            if(!this.image.getBufferedImage().getColorModel().hasAlpha())
            {
                src = GL11.GL_RGB;
            }

            GL11.glPixelStorei(GL11.GL_UNPACK_ALIGNMENT, 1);

            this.setAntiAlias();
            GL11.glTexEnvf(GL11.GL_TEXTURE_ENV, GL11.GL_TEXTURE_ENV_MODE, GL11.GL_MODULATE);

            GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, src, this.width, this.height,0, src, GL11.GL_UNSIGNED_BYTE, this.texture);
        }
    }


    /**
     * Converts the image into a compatible OpenGL power of two RGBA image
     */
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

    /**
     * Convertes the processed BufferedImage into a ByteBuffer for OpenGL
     * Also flips the texture to correspond to OpenGL specifications
     * @param BufferedImage image
     */
    protected void createTextureBuffer(BufferedImage image)
    {
        byte[] bytes = ((DataBufferByte) image.getRaster().getDataBuffer()).getData();

        this.texture = BufferUtils.createByteBuffer(bytes.length);
        this.texture.put(bytes);
        this.texture.flip();  
    }

    /**
     * Binds the texture to the current OpenGL Context
     */
    public void bind()
    {
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, this.id);
    }

    /**
     * Releases the texture from OpenGL memory
     */
    public void destroy()
    {
        GL11.glDeleteTextures(this.id);
    }

    /**
     * Retrieves the id of the OpenGL texture
     * @return
     */
    public int getId()
    {
        return this.id;
    }

    /**
     * Gets the width of the texture
     * @return int
     */
    public int getWidth()
    {
        return this.width;
    }

    /**
     * Gets the height of the texture
     * @return int
     */
    public int getHeight()
    {
        return this.height;
    }

    /**
     * Gets the original image size
     * @return Dimension
     */
    public Dimension getImageSize()
    {
        return (Dimension)this.imageSize.clone();
    }

    /**
     * Gets the width ratio of the texture to original image width
     * @return float
     */
    public float getRatioX()
    {
        return this.widthRatio;
    }

    /**
     * Gets the height ratio of the texture to original image height
     * @return float
     */
    public float getRatioY()
    {
        return this.heightRatio;
    }

    /**
     * Gets the image associated with the texture
     * @return INImage
     */
    public INImage getImage()
    {
        return this.image.clone();
    }

    /**
     * Gets the actual bytebuffer of the texture
     * It is a copy of the texture and any editing of the buffer
     * does not alter the actual texture
     * @return ByteBuffer
     */
    public ByteBuffer getTexture()
    {
        ByteBuffer tempBuffer = BufferUtils.createByteBuffer(this.texture.limit());
        tempBuffer.put(this.texture);
        tempBuffer.rewind();

        return tempBuffer;
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

    /**
     * Allows for the min filter, mag filter, wrap s, and wrap t to be changed
     * Please use the corresponding proper GL constants for the values
     *
     * @param int minFilter
     * @param int magFilter
     * @param int wrapS
     * @param int wrapT
     */
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

    /**
     * Turns on AntiAlias for the texture
     */
    public void setAntiAlias()
    {
        this.setTextureParameters(GL11.GL_LINEAR, GL11.GL_LINEAR, GL12.GL_CLAMP_TO_EDGE, GL12.GL_CLAMP_TO_EDGE);
    }

    /**
     * Finalize override
     */
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
                INTexture2D tex = (INTexture2D) obj;

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
