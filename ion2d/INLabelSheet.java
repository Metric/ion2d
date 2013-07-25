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
import java.awt.*;

public class INLabelSheet extends INSpriteSheet
{
    protected int itemsPerRow;
    protected int itemsPerColumn;

    protected float stepX;
    protected float stepY;

    protected int itemWidth;
    protected int itemHeight;

    protected String data;
    protected char firstChar;

    protected String[] lines;

    protected Color3B color;

    /**
     * Default constructor please use one of the others
     * Everything is set to null with this constructor
     */
    public INLabelSheet()
    {
    }

    /**
     * Second constructor
     * @param INTexture2D characterTexture
     * @param String text
     * @param int itemWidth
     * @param int itemHeight
     * @param char firstChar
     * @throws Exception
     */
    public INLabelSheet(INTexture2D characterTexture, String text, int itemWidth, int itemHeight, char firstChar) throws Exception
    {
        super(characterTexture, text.length());
        this.data = text;
        this.itemWidth = itemWidth;
        this.itemHeight = itemHeight;
        this.firstChar = firstChar;
        
        this.lines = this.data.split("\n");
        this.setContentSize(new Dimension(this.itemWidth*this.longestString(this.lines),this.itemHeight*this.lines.length));
        this.calculateItems();
        this.color = new Color3B(255,255,255);
        this.createLabel();
    }

    /**
     * Third Constructor
     * @param String characterFile
     * @param String text
     * @param int itemWidth
     * @param int itemHeight
     * @param char firstChar
     * @throws Exception
     */
    public INLabelSheet(String characterFile, String text, int itemWidth, int itemHeight, char firstChar) throws Exception
    {
        super(characterFile, text.length());
        this.data = text;
        this.itemWidth = itemWidth;
        this.itemHeight = itemHeight;
        this.firstChar = firstChar;

        this.lines = this.data.split("\n");
        this.setContentSize(new Dimension(this.itemWidth*this.longestString(this.lines),this.itemHeight*this.lines.length));
        this.calculateItems();
        this.color = new Color3B(255,255,255);
        this.createLabel();
    }

   /**
     * Determines the longest string of strings in an array
     * @param String[] rows
     * @return int
     */
    protected final int longestString(String[] rows)
    {
        int longest = 0;

        for(int i = 0; i < rows.length; i++)
        {
            if(longest < rows[i].length())
            {
                longest = rows[i].length();
            }
        }

        return longest;
    }

    /**
     * Calculates the items per row and items per column
     * Also calculates the needed stepping size for
     * the texture coordinates
     */
    protected void calculateItems()
    {
        Dimension size = this.textureAtlas.texture.getImageSize();

        this.itemsPerRow = size.width / this.itemWidth;
        this.itemsPerColumn = size.height / this.itemHeight;
    }

    /**
     * Creates the sprites for label text
     * @throws Exception
     */
    protected void createLabel() throws Exception
    {
        this.removeAllChildrenWithCleanup(true);
        
        for(int i = 0; i < this.lines.length; i++)
        {
            char[] characters = this.lines[i].toCharArray();

            for(int n = 0; n < characters.length; n++)
            {
                int charOffset = Math.abs(characters[n] - this.firstChar);
                int x = (charOffset % this.itemsPerRow) * this.itemWidth;
                int y = (charOffset / this.itemsPerRow) * this.itemHeight;

                INSprite newChar = new INSprite(this, new Rectangle(x,y,this.itemWidth,this.itemHeight));
                newChar.setPosition(new Vertex2F(this.itemWidth * n, this.itemHeight * (Math.abs(i - this.lines.length + 1))));
                newChar.setOpacityModifyRGB(false);
                newChar.setColor(this.color);
            }
        }
    }

    /**
     * Sets a new text phrase on label
     * @param String text
     * @throws Exception
     */
    public final void setText(String text) throws Exception
    {
        this.data = text;
        this.lines = this.data.split("\n");
        this.setContentSize(new Dimension(this.itemWidth*this.longestString(this.lines),this.itemHeight*this.lines.length));
        this.createLabel();
    }

    /**
     * Gets the current text on the label
     * @return String
     */
    public final String getText()
    {
        return this.data;
    }

    public final void setColor(Color3B color) throws Exception
    {
        this.color = color;
        this.createLabel();
    }

    /**
     * Gets the current color
     * @return Color3B
     */
    public Color3B getColor()
    {
        return this.color.clone();
    }
}
