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

import java.awt.geom.*;
import ion2d.support.INTypes.*;

public class INLabel extends INSprite
{
    protected INImage image;
    protected INFont font;
    protected String[] lines;
    protected Rectangle2D size;
    protected String data;


    /**
     * Default constructor
     * @throws Exception
     */
    public INLabel() throws Exception
    {
        this.font = new INFont();
        this.data = "";
        this.size = null;
        this.image = null;
        this.texture = null;
    }

    /**
     * Creates a new Label with a String
     * @param string
     * @throws Exception
     */
    public INLabel(String string) throws Exception
    {
        this(new INFont(), string);
    }

    /**
     * Creates a new label with the specified font and string
     * @param INFont font
     * @param String string
     * @throws Exception
     */
    public INLabel(INFont font, String string) throws Exception
    {
        this(font, string, font.getBounds(string));
    }

    /**
     * Creates a new lable with the specified font, string, and rectangle size
     * @param INFont font
     * @param String string
     * @param Rectangle2D size
     * @throws Exception
     */
    public INLabel(INFont font, String string, Rectangle2D size) throws Exception
    {
        this.data = string;
        this.lines = string.split("\n");

        this.image = new INImage((int)size.getWidth(), (int)size.getHeight());
        this.size = size;
        this.font = font;

        this.image.setFont(this.font);

        for(int i = 0; i < this.lines.length; i++)
        {
            this.image.drawString(this.lines[i], 0, (this.font.getSize() * i) + this.font.getSize());
        }

        this.setTexture(INTextureCache.addImage(this.image, this.data + this.font.getFont().getFontName() + this.font.getStyle().toString() + Float.toString(this.font.size)));
        this.setTextureRect(this.size.getBounds());
    }

    /**
     * Sets the text of the label
     * @param String text
     */
    public void setText(String text) throws Exception
    {
        this.data = text;
        this.lines = text.split("\n");
        this.size = this.font.getBounds(text);
        this.image = new INImage((int)this.size.getWidth(), (int)this.size.getHeight());
        this.image.setFont(this.font);

        for(int i = 0; i < this.lines.length; i++)
        {
            this.image.drawString(this.lines[i], 0, (this.font.getSize() * i) + this.font.getSize());
        }

        this.setTexture(INTextureCache.addImage(this.image, this.data + this.font.getFont().getFontName() + this.font.getStyle().toString() + Float.toString(this.font.size)));
        this.setTextureRect(this.size.getBounds());
    }

    /**
     * Sets the font color of the label
     * @param Color3B color
     */
    public void setFontColor(Color3B color) throws Exception
    {
        this.image = new INImage((int)this.size.getWidth(), (int)this.size.getHeight());
        this.image.setFont(this.font);
        
        for(int i = 0; i < this.lines.length; i++)
        {
            this.image.drawString(this.lines[i], 0, (this.font.getSize() * i) + this.font.getSize());
        }

        this.setTexture(INTextureCache.addImage(this.image, this.data + this.font.getFont().getFontName() + this.font.getStyle().toString() + Float.toString(this.font.size)));
        this.setTextureRect(this.size.getBounds());
    }
}
