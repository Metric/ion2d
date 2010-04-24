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

import java.awt.*;
import java.io.*;
import java.awt.geom.*;
import java.awt.font.*;

public class CCFont
{
    protected Font loadedFont;

    public enum FontStyle {
        BOLD,
        ITALIC,
        PLAIN,
        BOLDANDITALIC
    }

    public CCFont() throws Exception
    {
        throw new Exception("The default CCFont constructor is not supported");
    }

    /**
     * Creates a new font with the specified size and styles from a fontFile
     * @param File fontFile
     * @param float fontSize
     * @param in ... styles
     * @throws FontFormatException
     * @throws IOException
     */
    public CCFont(File fontFile, float fontSize, FontStyle fontStyle) throws FontFormatException, IOException
    {
        this.loadedFont = Font.createFont(Font.TRUETYPE_FONT, fontFile);

        this.loadedFont = this.loadedFont.deriveFont(fontSize);

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

    public CCFont(String filePath, float fontSize, FontStyle fontStyle) throws FontFormatException, IOException
    {
        File fontFile = new File(filePath);
        
        this.loadedFont = Font.createFont(Font.TRUETYPE_FONT, fontFile);
        this.loadedFont = this.loadedFont.deriveFont(fontSize);

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
     * @return
     */
    public Rectangle getBoundingRect(String string, FontRenderContext frc)
    {
        Rectangle2D rect = this.loadedFont.getStringBounds(string, frc);

        return rect.getBounds();
    }


    /**
     * Returns the actual Font object
     * @return Font
     */
    public Font getFont()
    {
        return this.loadedFont;
    }
}
