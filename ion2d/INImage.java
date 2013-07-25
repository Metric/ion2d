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

import javax.imageio.*;
import java.net.URL;
import ion2d.support.INTypes.*;
import java.awt.*;
import java.io.*;
import java.awt.image.*;
import java.awt.color.*;

//Image holder for loading images
public class INImage
{
    protected BufferedImage image;
    protected URL location;
    protected Graphics2D gd;
    protected String name;

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
     */
    public INImage()
    {
        this(120,15);
    }

    /**
     * Loads an image from a file object
     * @param File file
     * @throws IOException
     */
    public INImage(File file) throws IOException
    {
        this.name = file.getName();
        BufferedImage originalImage = ImageIO.read(file);
        this.image = new BufferedImage(originalImage.getWidth(), originalImage.getHeight(), BufferedImage.TYPE_INT_ARGB);
        this.gd = this.image.createGraphics();
        this.location = null;
        this.gd.setColor(new Color(0.0f,0.0f,0.0f,0.0f));
        this.gd.fillRect(0, 0, originalImage.getWidth(), originalImage.getHeight());
        this.gd.drawImage(originalImage, 0, 0, null);
    }

    /**
     * Loads an image from a BufferImage object
     * @param BufferedImage image
     */
    public INImage(BufferedImage image)
    {
        this.name = image.toString();
        BufferedImage originalImage = image;
        this.location = null;
        this.image = new BufferedImage(originalImage.getWidth(), originalImage.getHeight(), BufferedImage.TYPE_INT_ARGB);
        this.gd = this.image.createGraphics();

        this.gd.setColor(new Color(0.0f,0.0f,0.0f,0.0f));
        this.gd.fillRect(0, 0, originalImage.getWidth(), originalImage.getHeight());
        this.gd.drawImage(originalImage, 0, 0, null);
    }

    /**
     * Creates a blank ARGB BufferedImage with the specified height and width.
     * @param int width
     * @param int height
     */
    public INImage(int width, int height)
    {
        this.image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        this.name = this.image.toString();
        this.location = null;
        this.gd = this.image.createGraphics();
        this.gd.setColor(new Color(0.0f,0.0f,0.0f,0.0f));
        this.gd.fillRect(0, 0, width, height);
    }

    /**
     * Loads a image from the specified file path
     * @param String file
     * @throws IOException
     */
    public INImage(String file) throws IOException
    {
        this.name = file;
        this.location = new URL(file);
        BufferedImage originalImage = ImageIO.read(this.location);
        this.image = new BufferedImage(originalImage.getWidth(), originalImage.getHeight(), BufferedImage.TYPE_INT_ARGB);
        this.gd = this.image.createGraphics();

        this.gd.setColor(new Color(0.0f,0.0f,0.0f,0.0f));
        this.gd.fillRect(0, 0, originalImage.getWidth(), originalImage.getHeight());
        this.gd.drawImage(originalImage, 0, 0, null);
    }

    /**
     * Loads an image from the specified URL object
     * @param URL link
     * @throws IOException
     */
    public INImage(URL link) throws IOException
    {
        this.name = link.toString();
        this.location = link;
        BufferedImage originalImage = ImageIO.read(this.location);
        this.image = new BufferedImage(originalImage.getWidth(), originalImage.getHeight(), BufferedImage.TYPE_INT_ARGB);
        this.gd = this.image.createGraphics();

        this.gd.setColor(new Color(0.0f,0.0f,0.0f,0.0f));
        this.gd.fillRect(0, 0, originalImage.getWidth(), originalImage.getHeight());
        this.gd.drawImage(originalImage, 0, 0, null);
    }

    /**
     * Copy constructor
     * @param INImage original
     */
    public INImage(INImage original)
    {
        if(original == null) throw new NullPointerException();

        BufferedImage originalImage = original.getBufferedImage();
        this.image = new BufferedImage(originalImage.getWidth(), originalImage.getHeight(), BufferedImage.TYPE_INT_ARGB);
        this.name = original.name;
        this.location = original.location;
        this.gd = this.image.createGraphics();

        this.gd.setColor(new Color(0.0f,0.0f,0.0f,0.0f));
        this.gd.fillRect(0, 0, originalImage.getWidth(), originalImage.getHeight());
        this.gd.drawImage(originalImage, 0, 0, null);
    }

    /**
     * Clone override
     * @return INImage
     */
    public INImage clone()
    {
        return new INImage(this);
    }

    /**
     * Returns a Graphics2D object so that you can
     * draw on the image
     * @return Graphics2D
     */
    public Graphics2D getGraphics()
    {
        return this.gd;
    }

    /**
     * Short Cuts for graphics 2d draw functions
     */

    /**
     * Draws the string to the image at position 0,0
     * @param String text
     */
    public void drawString(String text)
    {
        this.gd.setColor(new Color(255,255,255,255));
        this.gd.drawString(text, 0, 0);
    }

    /**
     * Draws the string at the specified position
     * @param String text
     * @param float x
     * @param float y
     */
    public void drawString(String text, float x, float y)
    {
        this.gd.setPaintMode();
        this.gd.setColor(new Color(1.0f,1.0f,1.0f,1.0f));
        this.gd.drawString(text, x, y);
    }
    
    /**
     * Draws a string with the specified font at 0,0
     * The previous font is restored after drawing the string
     * with the new font
     * @param String text
     * @param INFont font
     */
    public void drawString(String text, INFont font)
    {
        Font tempFont = this.gd.getFont();
        this.gd.setFont(font.getFont());
        this.gd.setColor(new Color(255,255,255,255));
        this.gd.drawString(text, 0, 0);
        this.gd.setFont(tempFont);
    }

    /**
     * Draws a string with the specified font at the specified position
     * The previous font is restored after drawing the string
     * witht he new font
     * @param String text
     * @param INFont font
     */
     public void drawString(String text, INFont font, float x, float y)
     {
        Font tempFont = this.gd.getFont();
        this.gd.setFont(font.getFont());
        this.gd.setColor(new Color(1.0f,1.0f,1.0f,1.0f));
        this.gd.drawString(text, x, y);
        this.gd.setFont(tempFont);         
     }

    /**
     * Sets the font to use for when drawing strings
     * @param INFont font
     */
    public void setFont(INFont font)
    {
        this.gd.setFont(font.getFont());
    }

    /**
     * Returns the actual reference to the buffered image
     * @return BufferedImage
     */
    public BufferedImage getBufferedImage()
    {
        return this.image;
    }

    /**
     * Gets the file name / file path of the image / URL
     * @return String
     */
    public String getName()
    {
        return this.name;
    }
}
