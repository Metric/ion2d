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

import ion2d.interfaces.*;
import ion2d.support.INMath;
import ion2d.support.INTypes.*;
import java.awt.Rectangle;
import java.awt.geom.*;
import java.util.*;
import java.io.*;
import java.awt.Dimension;
import org.lwjgl.opengl.*;
import java.nio.*;

public class INSpriteSheet extends INNode implements INTextureProtocol
{
    protected INTextureAtlas textureAtlas;
    protected INBlend blend;

    /**
     * Default constructor
     */
    public INSpriteSheet()
    {
    }

    /**
     * Secondary Constructor
     * Automatically sets the initial capacity of the spritesheet to 29 children
     * @param INTexture2D texture
     */
    public INSpriteSheet(INTexture2D texture)
    {
        this(texture, 29);
    }

    /**
     * Third Constructor
     * @param INTexture2D texture
     * @param int capacity
     */
    public INSpriteSheet(INTexture2D texture, int capacity)
    {
        this.blend = new INBlend();

        this.textureAtlas = new INTextureAtlas(texture, capacity);

        this.children = new Vector<INNode>(capacity);
    }

    /**
     * Fourth Constructor
     * Automatically sets the initial capacity of the spritesheet to 29 children
     * Loads the spritesheet from an image file
     * @param String filepath
     */
    public INSpriteSheet(String filepath) throws Exception
    {
        this(filepath, 29);
    }

    /**
     * Fifth Constructor
     * @param String filepath
     * @param int capacity
     */
    public INSpriteSheet(String filepath, int capacity) throws Exception
    {
        INTexture2D texture = INTextureCache.addImage(filepath);
        this.textureAtlas = new INTextureAtlas(texture, capacity);

        this.children = new Vector<INNode>(capacity);
        this.blend = new INBlend();
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
     * Removes the specified child node
     * @param INNode node
     * @param boolean cleanup
     */
    public void removeChild(INNode node, boolean cleanup)
    {
        int index = this.getIndexForChild(node);
        this.textureAtlas.removeQuad(index);

        INSprite sprite = (INSprite)node;
        sprite.useSelfRender();

        //Since we are removing the child from the Sprite Sheet we
        //also need to remove it from the sprites parent if it is
        //not the sprite sheet itself
        if(!sprite.parent.equals(this))
            sprite.parent.removeChild(sprite, cleanup);

        super.removeChild(node, cleanup);
        
        //Now we need to go through the children of the sprite itself and remove them
        //We will do it recursively
        for(INNode child : sprite.children)
        {
            this.removeChild(child, cleanup);
        }
    }

    /**
     * Removes a child by the specified tag
     * @param int tag
     * @param boolean cleanup
     */
    public void removeChildByTag(int tag, boolean cleanup)
    {
        INNode node = this.getChildByTag(tag);

        int index = this.getIndexForChild(node);
        this.textureAtlas.removeQuad(index);

        INSprite sprite = (INSprite)node;
        sprite.useSelfRender();

        //Since we are removing the child from the Sprite Sheet we
        //also need to remove it from the sprites parent if it is
        //not the sprite sheet itself
        if(!sprite.parent.equals(this))
            sprite.parent.removeChild(sprite, cleanup);

        super.removeChild(node, cleanup);

        //Now we need to go through the children of the sprite itself and remove them
        //We will do it recursively
        for(INNode child : sprite.children)
        {
            this.removeChild(child, cleanup);
        }
    }

    /**
     * Removes all child nodes
     * @param cleanup
     */
    public void removeAllChildrenWithCleanup(boolean cleanup)
    {
        for(INNode child : this.children)
        {
            if(!child.equals(this))
            {
                //We do not care about the children associated with
                //The top children so we remove all of them.
                INSprite sprite = (INSprite)child;
                sprite.useSelfRender();
                sprite.removeAllChildrenWithCleanup(cleanup);
            }
        }

        super.removeAllChildrenWithCleanup(cleanup);
        this.textureAtlas.removeAllQuads();
    }

    /**
     * addChild override
     * @param INNode node
     * @return INNode
     * @throws Exception
     */
    public INNode addChild(INNode node) throws Exception
    {
        if(node == null) throw new Exception("addChild child must not be null");
        if(node.getClass() != INSprite.class) throw new Exception("INSpriteSheet only support INSprites as children");
        
        INSprite sprite = (INSprite)node;
        
        if(sprite.getTexture().getId() != this.textureAtlas.getTexture().getId()) throw new Exception("INSprite is not using the same texture id as CCSpriteSheet");
        
        INNode child = super.addChild(node);
        
        if(this.children.size() > this.textureAtlas.capacity())
            this.textureAtlas.resize(this.children.size() + 1);

        this.reorderAtlas();
        this.insertChild(sprite, this.getIndexForChild(node));
        return child;        
    }

    /**
     * addChild Override
     * @param INNode node
     * @param int z
     * @return INNode
     * @throws Exception
     */
    public INNode addChild(INNode node, int z) throws Exception
    {
        if(node == null) throw new Exception("addChild child must not be null");
        if(node.getClass() != INSprite.class) throw new Exception("CCSpriteSheet only support CCSprites as children");
        
        INSprite sprite = (INSprite)node;
        
        if(sprite.getTexture().getId() != this.textureAtlas.getTexture().getId()) throw new Exception("CCSprite is not using the same texture id as CCSpriteSheet");
        
        INNode child = super.addChild(node, z);
        
        if(this.children.size() > this.textureAtlas.capacity())
            this.textureAtlas.resize(this.children.size() + 1);

        this.reorderAtlas();
        this.insertChild(sprite, this.getIndexForChild(node));
        return child;        
    }

    /**
     * addChild override
     * @param INNode node
     * @param int z
     * @param int tag
     * @return INNode
     */
    public INNode addChild(INNode node, int z, int tag) throws Exception
    {
        if(node == null) throw new Exception("addChild child must not be null");
        if(node.getClass() != INSprite.class) throw new Exception("CCSpriteSheet only support CCSprites as children");
        
        INSprite sprite = (INSprite)node;
        
        if(sprite.getTexture().getId() != this.textureAtlas.getTexture().getId()) throw new Exception("CCSprite is not using the same texture id as CCSpriteSheet");
        
        INNode child = super.addChild(node, z, tag);
        
        if(this.children.size() > this.textureAtlas.capacity())
            this.textureAtlas.resize(this.children.size() + 1);

        this.reorderAtlas();
        this.insertChild(sprite, this.getIndexForChild(node));
        return child;
    }

    /**
     * Inserts the child into the textureAtlas
     * @param INSprite sprite
     * @param int index
     */
    public void insertChild(INSprite sprite, int index)
    {
        this.textureAtlas.insertQuad(sprite.getQuad(), index);
        sprite.setAtlasIndex(index);
    }

    /**
     * reorders the texture atlas quads based on the
     * ordering of the children, which is determined by the Zorder
     * Simpler cleaner and more efficient then removing and re-adding
     */
    public void reorderAtlas()
    {
        for(INNode child : this.children)
        {
            if(!child.equals(this))
            {
                INSprite sprite = (INSprite)child;

                int index = this.getIndexForChild(child);
                sprite.setAtlasIndex(index);

                this.textureAtlas.updateQuad(sprite.getQuad(), index);
            }
        }
    }

    /**
     * Draws the sprite sheet to screen
     */
    public void draw()
    {
        //If there are no quads do nothing
        if(this.textureAtlas.getTotalQuads() == 0 || this.textureAtlas.getTotalVisibleQuads() == 0)
            return;

        for(INNode child : this.children)
        {
            INSprite sprite = (INSprite)child;
            if(sprite.isDirty())
            {
                sprite.updateTransform();
            }
        }

        boolean newBlend = false;
        if(this.blend.source != GL11.GL_SRC_ALPHA || this.blend.destination != GL11.GL_ONE_MINUS_SRC_ALPHA)
        {
            GL11.glBlendFunc(this.blend.source, this.blend.destination);
            newBlend = true;
        }

        this.textureAtlas.drawQuads();

        if(newBlend)
            GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
    }
    /**
     * Sets the texture of the texture Atlas
     * @param INTexture2D texture
     */
    public void setTexture(INTexture2D texture)
    {
        this.textureAtlas.setTexture(texture);
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
        return this.textureAtlas;
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
        return this.textureAtlas.getTexture();
    }

    /**
     * Finalize override
     */
    public void finalize()
    {
        this.removeAllChildrenWithCleanup(true);
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
                INSpriteSheet node = (INSpriteSheet) obj;

                if(this.rotation != node.rotation || this.scaleX != node.scaleX
                   || this.scaleY != node.scaleY || !this.position.equals(node.position)
                   || this.visible != node.visible || !this.anchorPointInPixel.equals(node.anchorPointInPixel)
                   || !this.anchorPoint.equals(node.anchorPoint) || this.isRelativeAnchorePoint != node.isRelativeAnchorePoint
                   || !this.contentSize.equals(node.contentSize) || !this.transform.equals(node.transform)
                   || this.vertexZ != node.vertexZ || this.zOrder != node.zOrder || this.tag != node.tag
                   || !this.userData.equals(node.userData) || this.isRunning != node.isRunning
                   || this.isTransformDirty != node.isTransformDirty || this.isInverseDirty != node.isInverseDirty
                   || !this.offsetPosition.equals(node.offsetPosition)
                   || !this.textureAtlas.equals(node.textureAtlas) || !this.blend.equals(node.blend))
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
