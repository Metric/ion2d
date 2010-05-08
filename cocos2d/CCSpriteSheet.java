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

import cocos2d.interfaces.*;
import cocos2d.support.CCMath;
import cocos2d.support.CCTypes.*;
import java.awt.Rectangle;
import java.awt.geom.*;
import java.util.*;
import java.io.*;
import java.awt.Dimension;
import org.lwjgl.opengl.*;
import java.nio.*;

public class CCSpriteSheet extends CCNode implements CCTextureProtocol
{
    protected CCTextureAtlas textureAtlas;
    protected CCBlend blend;

    /**
     * Default constructor
     */
    public CCSpriteSheet() throws Exception
    {
        throw new Exception("CCSpriteSheet default constructor is not supported");
    }

    /**
     * Secondary Constructor
     * Automatically sets the initial capacity of the spritesheet to 29 children
     * @param CCTexture2D texture
     */
    public CCSpriteSheet(CCTexture2D texture)
    {
        this(texture, 29);
    }

    /**
     * Third Constructor
     * @param CCTexture2D texture
     * @param int capacity
     */
    public CCSpriteSheet(CCTexture2D texture, int capacity)
    {
        this.blend = new CCBlend();

        this.textureAtlas = new CCTextureAtlas(texture, capacity);

        this.children = new Vector<CCNode>(capacity);
    }

    /**
     * Fourth Constructor
     * Automatically sets the initial capacity of the spritesheet to 29 children
     * Loads the spritesheet from an image file
     * @param String filepath
     */
    public CCSpriteSheet(String filepath) throws Exception
    {
        this(filepath, 29);
    }

    /**
     * Fifth Constructor
     * @param String filepath
     * @param int capacity
     */
    public CCSpriteSheet(String filepath, int capacity) throws Exception
    {
        CCTexture2D texture = CCTextureCache.addImage(filepath);
        this.textureAtlas = new CCTextureAtlas(texture, capacity);

        this.children = new Vector<CCNode>(capacity);
    }

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
     * @param CCNode node
     * @param boolean cleanup
     */
    public void removeChild(CCNode node, boolean cleanup)
    {
        int index = this.getIndexForChild(node);
        this.textureAtlas.removeQuad(index);

        CCSprite sprite = (CCSprite)node;
        sprite.useSelfRender();

        //Since we are removing the child from the Sprite Sheet we
        //also need to remove it from the sprites parent if it is
        //not the sprite sheet itself
        if(!sprite.parent.equals(this))
            sprite.parent.removeChild(sprite, cleanup);

        super.removeChild(node, cleanup);
        
        //Now we need to go through the children of the sprite itself and remove them
        //We will do it recursively
        for(CCNode child : sprite.children)
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
        CCNode node = this.getChildByTag(tag);

        int index = this.getIndexForChild(node);
        this.textureAtlas.removeQuad(index);

        CCSprite sprite = (CCSprite)node;
        sprite.useSelfRender();

        //Since we are removing the child from the Sprite Sheet we
        //also need to remove it from the sprites parent if it is
        //not the sprite sheet itself
        if(!sprite.parent.equals(this))
            sprite.parent.removeChild(sprite, cleanup);

        super.removeChild(node, cleanup);

        //Now we need to go through the children of the sprite itself and remove them
        //We will do it recursively
        for(CCNode child : sprite.children)
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
        for(CCNode child : this.children)
        {
            if(!child.equals(this))
            {
                //We do not care about the children associated with
                //The top children so we remove all of them.
                CCSprite sprite = (CCSprite)child;
                sprite.useSelfRender();
                sprite.removeAllChildrenWithCleanup(cleanup);
            }
        }

        super.removeAllChildrenWithCleanup(cleanup);
        this.textureAtlas.removeAllQuads();
    }

    /**
     * addChild override
     * @param CCNode node
     * @return CCNode
     * @throws Exception
     */
    public CCNode addChild(CCNode node) throws Exception
    {
        if(node == null) throw new Exception("addChild child must not be null");
        if(node.getClass() != CCSprite.class) throw new Exception("CCSpriteSheet only support CCSprites as children");
        
        CCSprite sprite = (CCSprite)node;
        
        if(sprite.getTexture().getId() != this.textureAtlas.getTexture().getId()) throw new Exception("CCSprite is not using the same texture id as CCSpriteSheet");
        
        CCNode child = super.addChild(node);
        
        if(this.children.size() > this.textureAtlas.capacity())
            this.textureAtlas.resize(this.children.size() + 1);

        this.reorderAtlas();
        this.insertChild(sprite, this.getIndexForChild(node));
        return child;        
    }

    /**
     * addChild Override
     * @param CCNode node
     * @param int z
     * @return CCNode
     * @throws Exception
     */
    public CCNode addChild(CCNode node, int z) throws Exception
    {
        if(node == null) throw new Exception("addChild child must not be null");
        if(node.getClass() != CCSprite.class) throw new Exception("CCSpriteSheet only support CCSprites as children");
        
        CCSprite sprite = (CCSprite)node;
        
        if(sprite.getTexture().getId() != this.textureAtlas.getTexture().getId()) throw new Exception("CCSprite is not using the same texture id as CCSpriteSheet");
        
        CCNode child = super.addChild(node, z);
        
        if(this.children.size() > this.textureAtlas.capacity())
            this.textureAtlas.resize(this.children.size() + 1);

        this.reorderAtlas();
        this.insertChild(sprite, this.getIndexForChild(node));
        return child;        
    }

    /**
     * addChild override
     * @param CCNode node
     * @param int z
     * @param int tag
     * @return CCNode
     */
    public CCNode addChild(CCNode node, int z, int tag) throws Exception
    {
        if(node == null) throw new Exception("addChild child must not be null");
        if(node.getClass() != CCSprite.class) throw new Exception("CCSpriteSheet only support CCSprites as children");
        
        CCSprite sprite = (CCSprite)node;
        
        if(sprite.getTexture().getId() != this.textureAtlas.getTexture().getId()) throw new Exception("CCSprite is not using the same texture id as CCSpriteSheet");
        
        CCNode child = super.addChild(node, z, tag);
        
        if(this.children.size() > this.textureAtlas.capacity())
            this.textureAtlas.resize(this.children.size() + 1);

        this.reorderAtlas();
        this.insertChild(sprite, this.getIndexForChild(node));
        return child;
    }

    /**
     * Inserts the child into the textureAtlas
     * @param CCSprite sprite
     * @param int index
     */
    public void insertChild(CCSprite sprite, int index)
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
        for(CCNode child : this.children)
        {
            if(!child.equals(this))
            {
                CCSprite sprite = (CCSprite)child;

                int index = this.getIndexForChild(child);
                sprite.setAtlasIndex(index);

                this.textureAtlas.updateQuad(sprite.getQuad(), index);
            }
        }
    }

    public void draw()
    {
        //If there are no quads do nothing
        if(this.textureAtlas.getTotalQuads() == 0 || this.textureAtlas.getTotalVisibleQuads() == 0)
            return;

        for(CCNode child : this.children)
        {
            CCSprite sprite = (CCSprite)child;
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

    //Setters

    /**
     * Sets the texture of the texture Atlas
     * @param CCTexture2D texture
     */
    public void setTexture(CCTexture2D texture)
    {
        this.textureAtlas.setTexture(texture);
    }

    public void setBlendFunction(CCBlend blend)
    {
        this.blend = blend;
    }

    //Getters
    public CCTextureAtlas getTextureAtlas()
    {
        return this.textureAtlas;
    }

    public CCBlend getBlendFunction()
    {
        return this.blend.clone();
    }

    public CCTexture2D getTexture()
    {
        return this.textureAtlas.getTexture();
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
                CCSpriteSheet node = (CCSpriteSheet) obj;

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
