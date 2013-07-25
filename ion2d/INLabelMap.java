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
import ion2d.interfaces.*;
import java.awt.*;
import org.lwjgl.opengl.*;

public class INLabelMap extends INNode implements INTextureProtocol
{
    protected int itemsPerRow;
    protected int itemsPerColumn;

    protected int itemWidth;
    protected int itemHeight;

    protected String data;
    protected char firstChar;

    protected String[] lines;

    protected Color3B color;

    protected INTextureAtlas atlas;

    protected INBlend blend;

    /**
     * Default constructor please use one of the others
     * Everything is set to null with this constructor
     */
    public INLabelMap()
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
    public INLabelMap(INTexture2D characterTexture, String text, int itemWidth, int itemHeight, char firstChar) throws Exception
    {
        this.data = text;
        this.itemWidth = itemWidth;
        this.itemHeight = itemHeight;
        this.firstChar = firstChar;

        this.blend = new INBlend();
        this.atlas = new INTextureAtlas(characterTexture,this.data.length());

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
    public INLabelMap(String characterFile, String text, int itemWidth, int itemHeight, char firstChar) throws Exception
    {
        this.data = text;
        this.itemWidth = itemWidth;
        this.itemHeight = itemHeight;
        this.firstChar = firstChar;
    
        this.blend = new INBlend();
        this.atlas = new INTextureAtlas(characterFile,this.data.length());

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
        Dimension size = this.atlas.texture.getImageSize();

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
        
        int c = 0;

        for(int i = 0; i < this.lines.length; i++)
        {
            char[] characters = this.lines[i].toCharArray();

            for(int n = 0; n < characters.length; n++)
            {
                int charOffset = Math.abs(characters[n] - this.firstChar);
                int x = (charOffset % this.itemsPerRow) * this.itemWidth;
                int y = (charOffset / this.itemsPerRow) * this.itemHeight;

                QuadTexture3F quad = this.createQuad(new Rectangle(x,y,this.itemWidth,this.itemHeight));
                this.setQuadPosition(quad, n * this.itemWidth, this.itemHeight * Math.abs(i - this.lines.length + 1));

                if(this.atlas.getQuad(c) == null)
                {
                    this.atlas.insertQuad(quad, c);
                }
                else
                {
                    this.atlas.updateQuad(quad, c);
                }
                c++;
            }
        }
    }

    /**
     * Visit override
     */
    public void visit()
    {
        if(!this.visible)
            return;
        
        GL11.glPushMatrix();
        
        if(this.grid != null && this.grid.getActive())
        {
            this.grid.beforeDraw();
            this.transformAncestors();
        }

        this.transform();

        this.draw();

        if(this.grid != null && this.grid.getActive())
        {
            this.grid.afterDraw(this);
        }

        GL11.glPopMatrix();
    }

        /**
     * Draws the sprite sheet to screen
     */
    public void draw()
    {
        //If there are no quads do nothing
        if(this.atlas.getTotalQuads() == 0 || this.atlas.getTotalVisibleQuads() == 0)
            return;

        boolean newBlend = false;
        
        if(this.blend.source != GL11.GL_SRC_ALPHA || this.blend.destination != GL11.GL_ONE_MINUS_SRC_ALPHA)
        {
            GL11.glBlendFunc(this.blend.source, this.blend.destination);
            newBlend = true;
        }

        this.atlas.drawQuads();

        if(newBlend)
            GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
    }

    /**
     * Creates a quad with texuture coordinates and color
     * @param Rectangle rect
     * @return QuadTexture3F
     */
    protected final QuadTexture3F createQuad(Rectangle rect)
    {
        QuadTexture3F quad = new QuadTexture3F();
        Color4F quadColor = new Color4F((this.color.getRed() / 255) * 1.0f,
                                         (this.color.getGreen() / 255) * 1.0f,
                                         (this.color.getBlue() / 255) * 1.0f,
                                         1.0f);
        quad.bl.color = quadColor;
        quad.br.color = quadColor;
        quad.tl.color = quadColor;
        quad.tr.color = quadColor;

        float atlasWidth = this.atlas.texture.getImageSize().width;
        float atlasHeight = this.atlas.texture.getImageSize().height;

        float left = rect.x / atlasWidth;
        float right = (rect.x + rect.width) / atlasWidth;
        float top = rect.y / atlasHeight;
        float bottom = (rect.y + rect.height) / atlasHeight;

        quad.bl.coords.x = left;
        quad.bl.coords.y = bottom;
        quad.br.coords.x = right;
        quad.br.coords.y = bottom;
        quad.tl.coords.x = left;
        quad.tl.coords.y = top;
        quad.tr.coords.x = right;
        quad.tr.coords.y = top;

        return quad;
    }

    /**
     * Sets the quads position
     * @param QuadTexture3F quad
     * @param float x
     * @param float y
     */
    protected final void setQuadPosition(QuadTexture3F quad, float x, float y)
    {
        quad.bl.vertice = new Vertex3F(x, y, 0);
        quad.br.vertice = new Vertex3F(x + this.itemWidth, y, 0);
        quad.tr.vertice = new Vertex3F(x + this.itemWidth, y + this.itemHeight, 0);
        quad.tl.vertice = new Vertex3F(x, y + this.itemHeight, 0);
    }

    /**
     * Sets a new text phrase on label
     * @param String text
     * @throws Exception
     */
    public final void setText(String text) throws Exception
    {
        if(text.length() > this.data.length())
        {
            this.atlas.resize(text.length());
        }

        this.data = text;
        this.lines = this.data.split("\n");
        this.setContentSize(new Dimension(this.itemWidth*this.longestString(this.lines),this.itemHeight*this.lines.length));
        this.createLabel();
    }

    /**
     * Add a child to the node. Returns itself to allow for chaining
     * @param INNode node
     * @return INNode
     */
    public INNode addChild(INNode node) throws Exception
    {
        throw new Exception("INLabelMap does not support children");
    }

    /**
     * Adds a child to the node and sets the z order of the child
     * @param INNode node
     * @param int z
     * @return INNode
     */
    public INNode addChild(INNode node, int z) throws Exception
    {
        throw new Exception("INLabelMap does not support children");
    }

    /**
     * Adds a child to the node, sets the zorder and tag of the child
     * @param INNode node
     * @param int z
     * @param int tag
     * @return INNode
     */
    public INNode addChild(INNode node, int z, int tag) throws Exception
    {
        throw new Exception("INLabelMap does not support children");
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

        /**
     * Sets the texture of the texture Atlas
     * @param INTexture2D texture
     */
    public void setTexture(INTexture2D texture)
    {
        this.atlas.setTexture(texture);
    }

    /**
     * Sets the blend function of the sprite sheet
     * @param INBlend blend
     */
    public void setBlendFunction(INBlend blend)
    {
        this.blend = blend;
    }

    /**
     * Gets the texture atlas of the sprite sheet
     * The returned texture atlas is a shallow copy
     * Any changes made will affect the sprite sheet
     * @return INTextureAtlas
     */
    public INTextureAtlas getTextureAtlas()
    {
        return this.atlas;
    }

    /**
     * Returns the current blend function
     * The blend function is a clone and any changes
     * will not affect the sprite sheet
     * @return INBlend
     */
    public INBlend getBlendFunction()
    {
        return this.blend.clone();
    }

    /**
     * Gets the texture of the texture atlas
     * The return texture is a shallow copy and
     * any changes made will affect the texture on the atlas
     * @return INTexture2D
     */
    public INTexture2D getTexture()
    {
        return this.atlas.getTexture();
    }

}
