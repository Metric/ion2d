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

import java.awt.image.BufferedImage;
import javax.imageio.*;
import java.net.URL;
import cocos2d.support.CCTypes.*;
import java.awt.*;
import java.io.*;

//Image holder for loading images
public class CCImage
{
    protected BufferedImage image;
    protected URL location;
    protected Graphics2D gd;

    public CCImage()
    {
        this(120,15);
    }

    public CCImage(File file) throws IOException
    {
        this.image = ImageIO.read(file);
        this.gd = this.image.createGraphics();
    }
    
    public CCImage(BufferedImage image)
    {
        this.image = image;
        this.gd = this.image.createGraphics();
    }

    public CCImage(int width, int height)
    {
        this.image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        this.location = null;
        this.gd = this.image.createGraphics();
    }

    public CCImage(String file) throws IOException
    {
        this.location = new URL(file);
        this.image = ImageIO.read(this.location);
        this.gd = this.image.createGraphics();
    }

    public CCImage(URL link) throws IOException
    {
        this.image = ImageIO.read(this.location);
        this.gd = this.image.createGraphics();
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
        this.gd.drawString(text, x, y);
    }
    
    /**
     * Draws a string with the specified font at 0,0
     * The previous font is restored after drawing the string
     * witht he new font
     * @param String text
     * @param CCFont font
     */
    public void drawString(String text, CCFont font)
    {
        Font tempFont = this.gd.getFont();
        this.gd.setFont(font.getFont());
        this.gd.drawString(text, 0, 0);
        this.gd.setFont(tempFont);
    }

    /**
     * Draws a string with the specified font at the specified position
     * The previous font is restored after drawing the string
     * witht he new font
     * @param String text
     * @param CCFont font
     */
     public void drawString(String text, CCFont font, float x, float y)
     {
        Font tempFont = this.gd.getFont();
        this.gd.setFont(font.getFont());
        this.gd.drawString(text, x, y);
        this.gd.setFont(tempFont);         
     }

    /**
     * Sets the font to use for when drawing strings
     * @param CCFont font
     */
    public void setFont(CCFont font)
    {
        this.gd.setFont(font.getFont());
    }

    /**
     * Returns the actual reference to the buffered image
     * @return
     */
    public BufferedImage getBufferedImage()
    {
        return this.image;
    }
}
