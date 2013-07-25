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

import java.awt.*;
import java.io.*;
import java.net.*;
import java.awt.geom.*;

public class INFont
{
    protected Font loadedFont;
    protected float size;
    protected FontStyle style;

    public enum FontStyle {
        BOLD,
        ITALIC,
        PLAIN,
        BOLDANDITALIC
    }

    /**
     * Default constructor
     * Makes an INFont with size of 12
     * Plain style and with the Arial Font
     */
    public INFont()
    {
        this.size = 12;
        this.style = FontStyle.PLAIN;
        this.loadedFont = Font.decode("Arial-12");
    }

    /**
     * Creates a new font with the specified size and styles from a fontFile
     * @param File fontFile
     * @param float fontSize
     * @param FontStyle fontStyle
     * @throws FontFormatException
     * @throws IOException
     */
    public INFont(File fontFile, float fontSize, FontStyle fontStyle) throws FontFormatException, IOException
    {
        this.loadedFont = Font.createFont(Font.TRUETYPE_FONT, fontFile);

        this.loadedFont = this.loadedFont.deriveFont(fontSize);

        this.style = fontStyle;
        this.size = fontSize;

        switch(fontStyle)
        {
            case BOLD:
                this.loadedFont = this.loadedFont.deriveFont(Font.BOLD);
                break;
            case ITALIC:
                this.loadedFont = this.loadedFont.deriveFont(Font.ITALIC);
                break;
            case PLAIN:
                this.loadedFont = this.loadedFont.deriveFont(Font.PLAIN);
                break;
            case BOLDANDITALIC:
                this.loadedFont = this.loadedFont.deriveFont(Font.BOLD + Font.ITALIC);
                break;
        }
    }

    /**
     * Loads a font based on file path
     * @param String filePath
     * @param float fontSize
     * @param FontStyle fontStyle
     * @throws FontFormatException
     * @throws IOException
     */
    public INFont(String filePath, float fontSize, FontStyle fontStyle) throws FontFormatException, IOException
    {
        File fontFile = new File(filePath);
        
        this.loadedFont = Font.createFont(Font.TRUETYPE_FONT, fontFile);
        this.loadedFont = this.loadedFont.deriveFont(fontSize);

        this.style = fontStyle;
        this.size = fontSize;

        switch(fontStyle)
        {
            case BOLD:
                this.loadedFont = this.loadedFont.deriveFont(Font.BOLD);
                break;
            case ITALIC:
                this.loadedFont = this.loadedFont.deriveFont(Font.ITALIC);
                break;
            case PLAIN:
                this.loadedFont = this.loadedFont.deriveFont(Font.PLAIN);
                break;
            case BOLDANDITALIC:
                this.loadedFont = this.loadedFont.deriveFont(Font.BOLD + Font.ITALIC);
                break;
        }
    }

    /**
     * Loads a font based on URL Path
     * @param String filePath
     * @param float fontSize
     * @param FontStyle fontStyle
     * @throws FontFormatException
     * @throws IOException
     */
    public INFont(URL filePath, float fontSize, FontStyle fontStyle) throws FontFormatException, IOException
    {
        File fontFile = new File(filePath.toString());
        
        this.loadedFont = Font.createFont(Font.TRUETYPE_FONT, fontFile);
        this.loadedFont = this.loadedFont.deriveFont(fontSize);

        this.style = fontStyle;
        this.size = fontSize;

        switch(fontStyle)
        {
            case BOLD:
                this.loadedFont = this.loadedFont.deriveFont(Font.BOLD);
                break;
            case ITALIC:
                this.loadedFont = this.loadedFont.deriveFont(Font.ITALIC);
                break;
            case PLAIN:
                this.loadedFont = this.loadedFont.deriveFont(Font.PLAIN);
                break;
            case BOLDANDITALIC:
                this.loadedFont = this.loadedFont.deriveFont(Font.BOLD + Font.ITALIC);
                break;
        }
    }

    /**
     * Gets the bounding rect for the string based on the font
     * @param string
     * @return Rectangle
     */
    public Rectangle2D getBounds(String string)
    {
        Rectangle2D rect;

        String[] rows = string.split("\n");

        float height = rows.length * this.size;
        float width = this.longestString(rows) * this.size;

        rect = new Rectangle.Float(0,0,width,height);

        return rect.getBounds2D();
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
     * Returns the actual Font object
     * @return Font
     */
    public final Font getFont()
    {
        return this.loadedFont;
    }

    /**
     * Gets the font size
     * @return float
     */
    public final float getSize()
    {
        return this.size;
    }

    /**
     * Gets the font style
     * @return FontStyle
     */
    public final FontStyle getStyle()
    {
        return this.style;
    }
}
