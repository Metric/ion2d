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

import java.nio.*;
import org.lwjgl.input.Cursor;
import java.awt.image.*;

public class INCursor
{
    private INImage image;
    private int hotSpotX;
    private int hotSpotY;

    /**
     * Default constructor
     * Do not use. Instead use the other one.
     */
    public INCursor()
    {
    }

    /**
     * Secondary Constructor
     * @param INImage image
     * @param int hotSpotX
     * @param int hotSpotY
     */
    public INCursor(INImage image, int hotSpotX, int hotSpotY)
    {
        this.image = image;
        this.hotSpotX = hotSpotX;
        this.hotSpotY = hotSpotY;
    }

    /**
     * Gets the native image data as a IntBuffer
     * Already flipped for use with opengl
     * @return IntBuffer
     */
    protected final IntBuffer getNativeData()
    {
        BufferedImage nativeImage = this.image.getBufferedImage();

        int[] pixels = ((DataBufferInt) nativeImage.getData().getDataBuffer()).getData();

        IntBuffer outputPixels = ByteBuffer.allocateDirect(pixels.length * 4).asIntBuffer();
        outputPixels.put(pixels);
        outputPixels.rewind();
        outputPixels.flip();

        return outputPixels;
    }

    /**
     * Gets the actual native cursor object
     * @return Cursor
     */
    public Cursor getCursor()
    {
        BufferedImage nativeImage = this.image.getBufferedImage();

        try
        {
            Cursor nativeCursor = new Cursor(nativeImage.getWidth(), nativeImage.getHeight(),this.hotSpotX, this.hotSpotY,1,this.getNativeData(),null);
            return nativeCursor;
        } catch (Exception e)
        {
            System.out.println(e.getMessage());
            e.printStackTrace();
            return null;
        }
    }
}
