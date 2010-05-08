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

public class CCSprite extends CCNode implements CCRGBAProtocol, CCTextureProtocol
{
    protected CCTextureAtlas textures;
    protected int index;
    protected CCSpriteSheet spriteSheet;
    protected boolean dirty;
    protected boolean recursiveDirty;
    protected boolean hasChildren;

    protected CCBlend blend;
    protected CCTexture2D texture;

    protected boolean useSpriteSheet;
    protected Rectangle rect;

    protected HonorParentTransform honorTransform;

    protected QuadTexture3F quad;
    protected int opacity;
    protected Color3B colorUnmodified;
    protected Color3B color;
    protected boolean opacityModifiesRGB;

    protected boolean flipX;
    protected boolean flipY;

    protected Hashtable<String, CCAnimation> animations;
    protected CCSpriteFrame currentFrame;

    public CCSprite() throws Exception
    {
        this.dirty = this.recursiveDirty = false;

        this.useSelfRender();

        this.opacityModifiesRGB = true;
        this.blend = new CCBlend();

        this.setTexture(null);
        this.quad = new QuadTexture3F();

        this.flipX = this.flipY = false;
        this.anchorPoint = new Vertex2F(0.5f, 0.5f);

        this.offsetPosition = new Vertex2F();
        this.honorTransform = HonorParentTransform.CCHonorParentTransformAll;
        this.hasChildren = false;

        Color4F tempColor = new Color4F(1.0f,1.0f,1.0f,1.0f);
        this.quad.tl.color = tempColor;
        this.quad.tr.color = tempColor;
        this.quad.bl.color = tempColor;
        this.quad.br.color = tempColor;

        this.setTextureRect(new Rectangle(0,0,0,0));
    }

    public CCSprite(CCTexture2D tex) throws Exception
    {
        this(tex, new Rectangle(0,0,tex.getImageSize().width,tex.getImageSize().height));
    }

    public CCSprite(CCSpriteFrame spriteFrame) throws Exception
    {
        this(spriteFrame.getTexture(), spriteFrame.getRect());
        this.setDisplayFrame(spriteFrame);
    }

    public CCSprite(CCSpriteSheet sheet, Rectangle rect) throws Exception
    {
        this(sheet.getTexture(), rect);
        this.setUseSpriteSheet(sheet);
    }

    public CCSprite(CCTexture2D tex, Rectangle rect) throws Exception
    {
        this.dirty = this.recursiveDirty = false;

        this.useSelfRender();

        this.opacityModifiesRGB = true;
        this.blend = new CCBlend();

        this.setTexture(tex);
        this.quad = new QuadTexture3F();

        this.flipX = this.flipY = false;
        this.anchorPoint = new Vertex2F(0.5f, 0.5f);

        this.offsetPosition = new Vertex2F();
        this.honorTransform = HonorParentTransform.CCHonorParentTransformAll;
        this.hasChildren = false;

        Color4F tempColor = new Color4F(1.0f,1.0f,1.0f,1.0f);
        this.quad.tl.color = tempColor;
        this.quad.tr.color = tempColor;
        this.quad.bl.color = tempColor;
        this.quad.br.color = tempColor;

        this.setTextureRect(rect);
    }

    public CCSprite(CCTexture2D tex, Rectangle rect, Point2D offset) throws Exception
    {
        this.dirty = this.recursiveDirty = false;

        this.useSelfRender();

        this.opacityModifiesRGB = true;
        this.blend = new CCBlend();

        this.setTexture(tex);
        this.quad = new QuadTexture3F();

        this.flipX = this.flipY = false;
        this.anchorPoint = new Vertex2F(0.5f, 0.5f);

        this.offsetPosition = new Vertex2F(offset);
        this.honorTransform = HonorParentTransform.CCHonorParentTransformAll;
        this.hasChildren = false;

        Color4F tempColor = new Color4F(1.0f,1.0f,1.0f,1.0f);
        this.quad.tl.color = tempColor;
        this.quad.tr.color = tempColor;
        this.quad.bl.color = tempColor;
        this.quad.br.color = tempColor;

        this.setTextureRect(rect);
    }

    public CCSprite(String file) throws Exception
    {
        CCTexture2D tex = CCTextureCache.addImage(file);
        Rectangle lrect = new Rectangle(0,0,tex.getImageSize().width,tex.getImageSize().height);

        this.dirty = this.recursiveDirty = false;

        this.useSelfRender();

        this.opacityModifiesRGB = true;
        this.blend = new CCBlend();

        this.setTexture(tex);
        this.quad = new QuadTexture3F();

        this.flipX = this.flipY = false;
        this.anchorPoint = new Vertex2F(0.5f, 0.5f);

        this.offsetPosition = new Vertex2F();
        this.honorTransform = HonorParentTransform.CCHonorParentTransformAll;
        this.hasChildren = false;

        Color4F tempColor = new Color4F(1.0f,1.0f,1.0f,1.0f);
        this.quad.tl.color = tempColor;
        this.quad.tr.color = tempColor;
        this.quad.bl.color = tempColor;
        this.quad.br.color = tempColor;

        this.setTextureRect(lrect);
    }
    
    public CCSprite(String file, Rectangle rect) throws Exception
    {
        CCTexture2D tex = CCTextureCache.addImage(file);

        this.dirty = this.recursiveDirty = false;

        this.useSelfRender();

        this.opacityModifiesRGB = true;
        this.blend = new CCBlend();

        this.setTexture(tex);
        this.quad = new QuadTexture3F();

        this.flipX = this.flipY = false;
        this.anchorPoint = new Vertex2F(0.5f, 0.5f);

        this.offsetPosition = new Vertex2F();
        this.honorTransform = HonorParentTransform.CCHonorParentTransformAll;
        this.hasChildren = false;

        Color4F tempColor = new Color4F(1.0f,1.0f,1.0f,1.0f);
        this.quad.tl.color = tempColor;
        this.quad.tr.color = tempColor;
        this.quad.bl.color = tempColor;
        this.quad.br.color = tempColor;

        this.setTextureRect(rect);
    }
    
    public CCSprite(String file, Rectangle rect, Point2D offset) throws Exception
    {
        CCTexture2D tex = CCTextureCache.addImage(file);

        this.dirty = this.recursiveDirty = false;

        this.useSelfRender();

        this.opacityModifiesRGB = true;
        this.blend = new CCBlend();

        this.setTexture(tex);
        this.quad = new QuadTexture3F();

        this.flipX = this.flipY = false;
        this.anchorPoint = new Vertex2F(0.5f, 0.5f);

        this.offsetPosition = new Vertex2F(offset);
        this.honorTransform = HonorParentTransform.CCHonorParentTransformAll;
        this.hasChildren = false;

        Color4F tempColor = new Color4F(1.0f,1.0f,1.0f,1.0f);
        this.quad.tl.color = tempColor;
        this.quad.tr.color = tempColor;
        this.quad.bl.color = tempColor;
        this.quad.br.color = tempColor;

        this.setTextureRect(rect);
    }

    public CCSprite(CCImage image) throws Exception
    {
        CCTexture2D tex = CCTextureCache.addImage(image, image.image.toString());
        Rectangle lrect = new Rectangle(0,0,tex.getImageSize().width,tex.getImageSize().height);

        this.dirty = this.recursiveDirty = false;

        this.useSelfRender();

        this.opacityModifiesRGB = true;
        this.blend = new CCBlend();

        this.setTexture(tex);
        this.quad = new QuadTexture3F();

        this.flipX = this.flipY = false;
        this.anchorPoint = new Vertex2F(0.5f, 0.5f);

        this.offsetPosition = new Vertex2F();
        this.honorTransform = HonorParentTransform.CCHonorParentTransformAll;
        this.hasChildren = false;

        Color4F tempColor = new Color4F(1.0f,1.0f,1.0f,1.0f);
        this.quad.tl.color = tempColor;
        this.quad.tr.color = tempColor;
        this.quad.bl.color = tempColor;
        this.quad.br.color = tempColor;

        this.setTextureRect(lrect);
    }
    
    public CCSprite(CCImage image, Rectangle rect) throws Exception
    {
        CCTexture2D tex = CCTextureCache.addImage(image, image.image.toString());

        this.dirty = this.recursiveDirty = false;

        this.useSelfRender();

        this.opacityModifiesRGB = true;
        this.blend = new CCBlend();

        this.setTexture(tex);
        this.quad = new QuadTexture3F();

        this.flipX = this.flipY = false;
        this.anchorPoint = new Vertex2F(0.5f, 0.5f);

        this.offsetPosition = new Vertex2F();
        this.honorTransform = HonorParentTransform.CCHonorParentTransformAll;
        this.hasChildren = false;

        Color4F tempColor = new Color4F(1.0f,1.0f,1.0f,1.0f);
        this.quad.tl.color = tempColor;
        this.quad.tr.color = tempColor;
        this.quad.bl.color = tempColor;
        this.quad.br.color = tempColor;

        this.setTextureRect(rect);
    }
    
    public CCSprite(CCImage image, Rectangle rect, Point2D offset) throws Exception
    {
         CCTexture2D tex = CCTextureCache.addImage(image, image.image.toString());

        this.dirty = this.recursiveDirty = false;

        this.useSelfRender();

        this.opacityModifiesRGB = true;
        this.blend = new CCBlend();

        this.setTexture(tex);
        this.quad = new QuadTexture3F();

        this.flipX = this.flipY = false;
        this.anchorPoint = new Vertex2F(0.5f, 0.5f);

        this.offsetPosition = new Vertex2F(offset);
        this.honorTransform = HonorParentTransform.CCHonorParentTransformAll;
        this.hasChildren = false;

        Color4F tempColor = new Color4F(1.0f,1.0f,1.0f,1.0f);
        this.quad.tl.color = tempColor;
        this.quad.tr.color = tempColor;
        this.quad.bl.color = tempColor;
        this.quad.br.color = tempColor;

        this.setTextureRect(rect);
    }

    public CCSprite(File file) throws Exception
    {
        CCTexture2D tex = CCTextureCache.addImage(file, file.getName());
        Rectangle lrect = new Rectangle(0,0,tex.getImageSize().width,tex.getImageSize().height);
        
        this.dirty = this.recursiveDirty = false;

        this.useSelfRender();

        this.opacityModifiesRGB = true;
        this.blend = new CCBlend();

        this.setTexture(tex);
        this.quad = new QuadTexture3F();

        this.flipX = this.flipY = false;
        this.anchorPoint = new Vertex2F(0.5f, 0.5f);

        this.offsetPosition = new Vertex2F();
        this.honorTransform = HonorParentTransform.CCHonorParentTransformAll;
        this.hasChildren = false;

        Color4F tempColor = new Color4F(1.0f,1.0f,1.0f,1.0f);
        this.quad.tl.color = tempColor;
        this.quad.tr.color = tempColor;
        this.quad.bl.color = tempColor;
        this.quad.br.color = tempColor;

        this.setTextureRect(lrect);
    }
    
    public CCSprite(File file, Rectangle rect) throws Exception
    {
        CCTexture2D tex = CCTextureCache.addImage(file, file.getName());

        this.dirty = this.recursiveDirty = false;

        this.useSelfRender();

        this.opacityModifiesRGB = true;
        this.blend = new CCBlend();

        this.setTexture(tex);
        this.quad = new QuadTexture3F();

        this.flipX = this.flipY = false;
        this.anchorPoint = new Vertex2F(0.5f, 0.5f);

        this.offsetPosition = new Vertex2F();
        this.honorTransform = HonorParentTransform.CCHonorParentTransformAll;
        this.hasChildren = false;

        Color4F tempColor = new Color4F(1.0f,1.0f,1.0f,1.0f);
        this.quad.tl.color = tempColor;
        this.quad.tr.color = tempColor;
        this.quad.bl.color = tempColor;
        this.quad.br.color = tempColor;

        this.setTextureRect(rect);
    }
    
    public CCSprite(File file, Rectangle rect, Point2D offset) throws Exception
    {
        CCTexture2D tex = CCTextureCache.addImage(file, file.getName());

        this.dirty = this.recursiveDirty = false;

        this.useSelfRender();

        this.opacityModifiesRGB = true;
        this.blend = new CCBlend();

        this.setTexture(tex);
        this.quad = new QuadTexture3F();

        this.flipX = this.flipY = false;
        this.anchorPoint = new Vertex2F(0.5f, 0.5f);

        this.offsetPosition = new Vertex2F(offset);
        this.honorTransform = HonorParentTransform.CCHonorParentTransformAll;
        this.hasChildren = false;

        Color4F tempColor = new Color4F(1.0f,1.0f,1.0f,1.0f);
        this.quad.tl.color = tempColor;
        this.quad.tr.color = tempColor;
        this.quad.bl.color = tempColor;
        this.quad.br.color = tempColor;

        this.setTextureRect(rect);

    }

    public void useSelfRender()
    {
        this.index = -1;
        this.useSpriteSheet = false;
        this.textures = null;
        //Add Spritesheet support
        this.dirty = this.recursiveDirty = false;
        
        float x1 = this.offsetPosition.x;
        float y1 = this.offsetPosition.y;
        float x2 = x1 + this.rect.width;
        float y2 = y1 + this.rect.height;

        this.quad.bl.vertice = new Vertex3F(x1, y1, 0);
        this.quad.br.vertice = new Vertex3F(x2, y1, 0);
        this.quad.tl.vertice = new Vertex3F(x1, y2, 0);
        this.quad.tr.vertice = new Vertex3F(x2, y2, 0);
    }

    public void setTextureRect(Rectangle rect)
    {
        this.setTextureRect(rect, rect.getSize());
    }
    
    public void setTextureRect(Rectangle rect, Dimension untrimmed)
    {
        this.rect = rect;

        this.setContentSize(untrimmed);
        this.updateTextureCoords(rect);

        if(this.useSpriteSheet)
        {
            this.dirty = true;
        }
        else
        {
            float x1 = this.offsetPosition.x;
            float y1 = this.offsetPosition.y;
            float x2 = x1 + rect.width;
            float y2 = y1 + rect.height;

            this.quad.bl.vertice = new Vertex3F(x1, y1, 0);
            this.quad.br.vertice = new Vertex3F(x2, y1, 0);
            this.quad.tl.vertice = new Vertex3F(x1, y2, 0);
            this.quad.tr.vertice = new Vertex3F(x2, y2, 0);
        }
    }

    public void updateTextureCoords(Rectangle rect)
    {
        float atlasWidth = this.texture.getImageSize().width;
        float atlasHeight = this.texture.getImageSize().height;
        
        float left = rect.x / atlasWidth;
        float right = (rect.x + rect.width) / atlasWidth;
        float top = rect.y / atlasHeight;
        float bottom = (rect.y + rect.height) / atlasHeight;
        
        if(this.flipX)
        {
            float temp = right;
            right = left;
            left = temp;
        }

        if(this.flipY)
        {
            float temp = bottom;
            bottom = top;
            top = temp;
        }

        this.quad.bl.coords.x = left;
        this.quad.bl.coords.y = bottom;
        this.quad.br.coords.x = right;
        this.quad.br.coords.y = bottom;
        this.quad.tl.coords.x = left;
        this.quad.tl.coords.y = top;
        this.quad.tr.coords.x = right;
        this.quad.tr.coords.y = top;
    }

    public void updateTransform()
    {
        AffineTransform matrix;

        if(this.useSpriteSheet)
        {

            if(!this.visible)
            {
                this.quad.br.vertice = quad.tl.vertice = quad.tr.vertice = quad.bl.vertice = new Vertex3F(0,0,0);
                this.textures.updateQuad(this.quad, this.index);
                this.dirty = this.recursiveDirty = false;
                return;
            }

            if(this.parent == null || this.parent.equals(this.spriteSheet))
            {
                float radians = (float)CCMath.degreesToRadians(this.rotation);
                float c = (float)Math.cos(radians);
                float s = (float)Math.sin(radians);

                matrix = new AffineTransform(c * this.scaleX, s * this.scaleX, -s * this.scaleY,
                                                            c * this.scaleY, this.position.x, this.position.y);

                matrix.translate(-this.anchorPointInPixel.x, this.anchorPointInPixel.y);
            }
            else
            {
                matrix = new AffineTransform();
                matrix.setToIdentity();

                HonorParentTransform prev = HonorParentTransform.CCHonorParentTransformAll;

                for(CCNode p = this; p != this.spriteSheet; p = p.parent)
                {
                    AffineTransform newMatrix = new AffineTransform();
                    newMatrix.setToIdentity();

                    //2nd translate rotate and scale
                    if(prev == HonorParentTransform.CCHonorParentTransformTranslate)
                    {
                        newMatrix.translate(p.getPosition().x, p.getPosition().y);
                    }
                    else if(prev == HonorParentTransform.CCHonorParentTransformRotate)
                    {
                        newMatrix.rotate(CCMath.degreesToRadians(p.getRotation()));
                    }
                    else if(prev == HonorParentTransform.CCHonorParentTransformScale)
                    {
                        newMatrix.scale(p.getScaleX(), p.getScaleY());
                    }

                    //3rd: translate anchor point
                    newMatrix.translate(-p.getAnchorePointInPixel().x, -p.getAnchorePointInPixel().y);

                    //4th: Matrix multiplication
                    matrix.concatenate(newMatrix);

                    CCSprite temp = (CCSprite)p;
                    prev = temp.honorsParentTransform();
                }
            }

            //
            // Calculate the Quad based ont he Affine Matrix
            //
            float x1 = this.offsetPosition.x;
            float y1 = this.offsetPosition.y;
            float x2 = x1 + this.rect.width;
            float y2 = y1 + this.rect.height;

            float x = (float)matrix.getTranslateX();
            float y = (float)matrix.getTranslateY();
            double[] matrices = new double[6];
            matrix.getMatrix(matrices);
            float cr = (float)matrices[0];
            float sr = (float)matrices[1];
            float cr2 = (float)matrices[2];
            float sr2 = (float)-matrices[3];

            float ax = x1 * cr - y1 * sr2 + x;
            float ay = x1 * sr + y1 * cr2 + y;

            float bx = x2 * cr - y1 * sr2 + x;
            float by = x2 * sr + y1 * cr2 + y;

            float cx = x2 * cr - y2 * sr2 + x;
            float cy = x2 * sr + y2 * cr2 + y;

            float dx = x1 * cr - y2 * sr2 + x;
            float dy = x1 * sr + y2 * cr2 + y;

            this.quad.bl.vertice = new Vertex3F(ax, ay, this.vertexZ);
            this.quad.br.vertice = new Vertex3F(bx, by, this.vertexZ);
            this.quad.tl.vertice = new Vertex3F(dx, dy, this.vertexZ);
            this.quad.tr.vertice = new Vertex3F(cx, cy, this.vertexZ);

            this.textures.updateQuad(this.quad, this.index);
            this.dirty = this.recursiveDirty = false;
        }
    }

    public void draw()
    {
        //If the sprite is using a sprite sheet then do not draw via
        //The sprite and let the sprite sheet handle the drawing  instead
        if(this.useSpriteSheet) return;

        //This is pretty obvious, if the sprite isn't visible then
        //Do not draw the sprite!
        if(!this.visible) return;

        //If the sprites quad is off the screen there is no point in
        //Drawing the sprite
        if(this.isQuadOffScreen(this.quad)) return;

        boolean newBlend = false;
        
        if(this.blend.source != GL11.GL_SRC_ALPHA || this.blend.destination != GL11.GL_ONE_MINUS_SRC_ALPHA )
        {
            newBlend = true;
            GL11.glBlendFunc(this.blend.source, this.blend.destination);
        }

        this.texture.bind();

        //Vertex
        GL11.glVertexPointer(3, 0, this.getVertexArray());

        //Color
        GL11.glColorPointer(4, 0, this.getColorArray());

        //Texture
        GL11.glTexCoordPointer(2, 0, this.getTextureCoordinates());

        GL11.glDrawArrays(GL11.GL_QUADS, 0, 1);

        if(newBlend)
        {
            GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        }
    }

    /**
     * Gets the texture coordinates of the sprites quad
     * @return FloatBuffer
     */
    private FloatBuffer getTextureCoordinates()
    {
        FloatBuffer coordBuffer = FloatBuffer.allocate(2*4);
        
        coordBuffer.put(this.quad.bl.coords.x);
        coordBuffer.put(this.quad.bl.coords.y);
        
        coordBuffer.put(this.quad.br.coords.x);
        coordBuffer.put(this.quad.br.coords.y);
        
        coordBuffer.put(this.quad.tl.coords.x);
        coordBuffer.put(this.quad.tl.coords.y);
        
        coordBuffer.put(this.quad.tr.coords.x);
        coordBuffer.put(this.quad.tr.coords.y);

        return coordBuffer;
    }

    /**
     * Gets the colors from the sprites quad
     * @return FloatBuffer
     */
    private FloatBuffer getColorArray()
    {
        FloatBuffer colorBuffer = FloatBuffer.allocate(4*4);
        
        colorBuffer.put(this.quad.bl.color.getRed());
        colorBuffer.put(this.quad.bl.color.getGreen());
        colorBuffer.put(this.quad.bl.color.getBlue());
        colorBuffer.put(this.quad.bl.color.getAlpha());
        
        colorBuffer.put(this.quad.br.color.getRed());
        colorBuffer.put(this.quad.br.color.getGreen());
        colorBuffer.put(this.quad.br.color.getBlue());
        colorBuffer.put(this.quad.br.color.getAlpha());
        
        colorBuffer.put(this.quad.tl.color.getRed());
        colorBuffer.put(this.quad.tl.color.getGreen());
        colorBuffer.put(this.quad.tl.color.getBlue());
        colorBuffer.put(this.quad.tl.color.getAlpha());
        
        colorBuffer.put(this.quad.tr.color.getRed());
        colorBuffer.put(this.quad.tr.color.getGreen());
        colorBuffer.put(this.quad.tr.color.getBlue());
        colorBuffer.put(this.quad.tr.color.getAlpha());

        return colorBuffer;
    }

    /**
     * Gets the vertices for the sprites quad
     * @return FloatBuffer
     */
    private FloatBuffer getVertexArray()
    {
        FloatBuffer vertexBuffer = FloatBuffer.allocate(3 * 4);
        
        vertexBuffer.put(this.quad.bl.vertice.x);
        vertexBuffer.put(this.quad.bl.vertice.y);
        vertexBuffer.put(this.quad.bl.vertice.z);
        
        vertexBuffer.put(this.quad.br.vertice.x);
        vertexBuffer.put(this.quad.br.vertice.y);
        vertexBuffer.put(this.quad.br.vertice.z);
        
        vertexBuffer.put(this.quad.tl.vertice.x);
        vertexBuffer.put(this.quad.tl.vertice.y);
        vertexBuffer.put(this.quad.tl.vertice.z);
        
        vertexBuffer.put(this.quad.tr.vertice.x);
        vertexBuffer.put(this.quad.tr.vertice.y);
        vertexBuffer.put(this.quad.tr.vertice.z);
        
        return vertexBuffer;
    }

    /**
     * addChild override
     * @param CCNode node
     * @return CCNode
     * @throws Exception
     */
    public CCNode addChild(CCNode node) throws Exception
    {
        CCNode returnNode = super.addChild(node);
        
        if(this.useSpriteSheet)
        {
            if(this.getClass() != node.getClass()) throw new Exception("CCSprite only supports CCSprites as children when using SpriteSheet");
            CCSprite sprite = (CCSprite)node;

            if(this.textures.texture.getId() != sprite.texture.getId()) throw new Exception("CCSprite is not using the same texture id");

            this.spriteSheet.addChild(node);
        }

        this.hasChildren = true;
        return returnNode;
    }

    /**
     * addChild override
     * @param CCNode node
     * @param int z
     * @return CCNode
     */
    public CCNode addChild(CCNode node, int z) throws Exception
    {
         CCNode returnNode = super.addChild(node, z);
        
        if(this.useSpriteSheet)
        {
            if(this.getClass() != node.getClass()) throw new Exception("CCSprite only supports CCSprites as children when using SpriteSheet");
            CCSprite sprite = (CCSprite)node;

            if(this.textures.texture.getId() != sprite.texture.getId()) throw new Exception("CCSprite is not using the same texture id");

            this.spriteSheet.addChild(node, z);
        }

        this.hasChildren = true;
        return returnNode;       
    }

    /**
     * CCSprite addChild Override
     * @param CCNode node
     * @param int z
     * @param int tag
     * @return CCNode
     */
    public CCNode addChild(CCNode node, int z, int tag) throws Exception
    {
        CCNode returnNode = super.addChild(node, z, tag);
        
        if(this.useSpriteSheet)
        {
            if(this.getClass() != node.getClass()) throw new Exception("CCSprite only supports CCSprites as children when using SpriteSheet");
            CCSprite sprite = (CCSprite)node;

            if(this.textures.texture.getId() != sprite.texture.getId()) throw new Exception("CCSprite is not using the same texture id");

            this.spriteSheet.addChild(node, z, tag);
        }

        this.hasChildren = true;

        return returnNode;
    }

    public void removeChild(CCSprite sprite, boolean cleanup)
    {
        if(this.useSpriteSheet)
            this.spriteSheet.removeChild(sprite, cleanup);
        
        super.removeChild(sprite, cleanup);
        
        if(this.children.size() > 0) 
            this.hasChildren = true;
        else
            this.hasChildren = false;
    }

    /**
     * Removes a child by the specified tag
     * @param int tag
     * @param boolean cleanup
     */
    public void removeChildByTag(int tag, boolean cleanup)
    {
        CCSprite sprite = (CCSprite)this.getChildByTag(tag);
        
        if(this.useSpriteSheet)
           this.spriteSheet.removeChild(sprite, cleanup);
        
        if(this.children.size() > 0) 
            this.hasChildren = true;
        else
            this.hasChildren = false;
    }

    public void removeAllChildrenWithCleanup(boolean cleanup)
    {
        if(this.useSpriteSheet)
        {
            this.spriteSheet.removeAllChildrenWithCleanup(cleanup);
        }
        
        super.removeAllChildrenWithCleanup(cleanup);

        this.hasChildren = false;
    }

    public void setDirtyRecursively(boolean doit)
    {
        this.dirty = this.recursiveDirty = doit;
        if(this.hasChildren)
        {
            for(CCNode child : this.children)
            {
                CCSprite temp = (CCSprite)child;
                temp.setDirtyRecursively(true);
            }
        }
    }

    public void setPosition(Vertex2F point)
    {
        super.setPosition(point);
        if(this.useSpriteSheet && !this.recursiveDirty)
            this.setDirtyRecursively(true);
    }

    public void setRotation(float rotation)
    {
        super.setRotation(rotation);
        if(this.useSpriteSheet && !this.recursiveDirty)
            this.setDirtyRecursively(true);
    }

    public void setScaleX(float scale)
    {
        super.setScaleX(scale);
        if(this.useSpriteSheet && !this.recursiveDirty)
            this.setDirtyRecursively(true);
    }

    public void setScaleY(float scale)
    {
        super.setScaleY(scale);
        if(this.useSpriteSheet && !this.recursiveDirty)
            this.setDirtyRecursively(true);
    }

    public void setScale(float scale)
    {
        super.setScale(scale);
        if(this.useSpriteSheet && !this.recursiveDirty)
            this.setDirtyRecursively(true);
    }

    public void setVertexZ(float z)
    {
        super.setVertexZ(z);
        if(this.useSpriteSheet && !this.recursiveDirty)
            this.setDirtyRecursively(true);
    }

    public void setAnchorPoint(Vertex2F point)
    {
        super.setAnchorPoint(point);
        if(this.useSpriteSheet && !this.recursiveDirty)
            this.setDirtyRecursively(true);
    }

    public void setRelativeAnchorPoint(boolean relative)
    {
        if(this.useSpriteSheet) return;
        
        super.setIsRelativeAnchorePoint(relative);
    }

    public void setVisible(boolean visible)
    {
        if(visible != this.visible)
        {
            super.setVisible(visible);
            if(this.useSpriteSheet && !this.recursiveDirty)
            {
                this.dirty = this.recursiveDirty = true;
                for(CCNode child : this.children)
                {
                    child.setVisible(visible);
                }
            }
        }
    }

    public void setFlipX(boolean flip)
    {
        if(this.flipX != flip)
        {
            this.flipX = flip;
            this.setTextureRect(this.rect);
        }
    }

    public void setFlipY(boolean flip)
    {
        if(this.flipY != flip)
        {
            this.flipY = flip;
            this.setTextureRect(this.rect);
        }
    }

    /**
     * Sets the display frame of the CCSpriteFrame
     * @param CCSpriteFrame frame
     * @throws Exception
     */
    public void setDisplayFrame(CCSpriteFrame frame) throws Exception
    {
        this.currentFrame = frame;
        this.offsetPosition = new Vertex2F(frame.getOffset());
        Rectangle frameRect = frame.getRect();
        Dimension frameSize = frame.getOriginalSize();
        
        this.offsetPosition.x += (frameSize.width - frameRect.width) / 2;
        this.offsetPosition.y += (frameSize.height - frameRect.height) / 2;
        
        if(frame.texture.getId() != this.texture.getId())
        {
            this.setTexture(frame.getTexture());
        }

        this.setTextureRect(frameRect, frameSize);
    }

    /**
     * Sets the display frame by the animation name and index
     * @param String animationName
     * @param int index
     * @throws Exception
     */
    public void setDisplayFrame(String animationName, int index) throws Exception
    {
        if(this.animations == null)
            this.initAnimations();

        CCAnimation anim = this.animations.get(animationName);
        CCSpriteFrame frame = anim.getFrames().get(index);

        if(frame == null) throw new Exception("CCSprite.setDisplayFrame - Invalid Frame");
        this.setDisplayFrame(frame);
    }

    /**
     * Checks to see if the specified frame is displayed
     * @param CCSpriteFrame frame
     * @return boolean
     */
    public boolean isFrameDisplayed(CCSpriteFrame frame)
    {
        return this.currentFrame.equals(frame);
    }

    /**
     * Gets the currently displayed frame
     * @return CCSpriteFrame
     */
    public CCSpriteFrame getCurrentFrame()
    {
        return this.currentFrame;
    }

    /**
     * Adds an animation to the sprite
     * @param anim
     */
    public void addAnimation(CCAnimation anim)
    {
        if(this.animations == null)
            this.initAnimations();
        
        this.animations.put(anim.getName(), anim);
    }

    /**
     * Gets the animation by name
     * @param String name
     * @return CCAnimation
     */
    public CCAnimation getAnimation(String name)
    {
        return this.animations.get(name);
    }

    private void initAnimations()
    {
        this.animations = new Hashtable<String, CCAnimation>(2,2);
    }

    /**
     * RGBA Protocol
     * @return
     */
    public void updateColor()
    {
        Color4F color4 = new Color4F(this.color.getRed()/255.0f, this.color.getGreen()/255.0f, this.color.getBlue()/255.0f, this.opacity/255.0f);

        this.quad.bl.color = color4;
        this.quad.br.color = color4;
        this.quad.tl.color = color4;
        this.quad.tr.color = color4;

        if(this.useSpriteSheet)
        {
            if(this.index != -1)
            {
                this.textures.updateQuad(this.quad, this.index);
            }
            else
            {
                this.dirty = true;
            }
        }
    }

    //Getters and Setters for variables
    public QuadTexture3F getQuad() { return this.quad.clone(); }

    public boolean isDirty() { return this.dirty; }

    public Rectangle getRect() { return (Rectangle)this.rect.clone(); }

    public boolean getFlipX() { return this.flipX; }

    public boolean getFlipY() { return this.flipY; }

    public Color3B getColor() { if(this.opacityModifiesRGB) return this.colorUnmodified.clone(); else return this.color.clone(); }

    public boolean usesSpriteSheet() { return this.useSpriteSheet; }
    protected void setUseSpriteSheet(CCSpriteSheet sheet)
    {
        this.useSpriteSheet = true;
        this.spriteSheet = sheet;
        this.textures = this.spriteSheet.getTextureAtlas();
    }

    public HonorParentTransform honorsParentTransform() { return this.honorTransform; }
    public void setHonorParentTransform(HonorParentTransform transform)
    {
        this.honorTransform = transform;
    }

    public void setAtlasIndex(int index)
    {
        this.index = index;
    }

    public boolean doesOpacityModifyRGB()
    {
        return this.opacityModifiesRGB;
    }

    public float getOpacity()
    {
        return (this.opacity/255.0f);
    }

    public void setOpacity(float level)
    {
        this.opacity = Math.round((level / 1.0f) * 255);

        if(this.opacityModifiesRGB)
        {
            this.setColor(this.colorUnmodified);
        }
        else
        {
            this.setColor(this.color);
        }
    }

    public void setColor(Color3B color)
    {
        this.color = this.colorUnmodified = color;

        if(this.opacityModifiesRGB)
        {
            this.color.setRed(this.color.getRed() * this.opacity / 255);
            this.color.setGreen(this.color.getGreen() * this.opacity / 255);
            this.color.setBlue((this.color.getBlue() * this.opacity / 255));
        }

        this.updateColor();
    }

    public void setOpacityModifyRGB(boolean modify)
    {
        this.opacityModifiesRGB = modify;
    }

    public void setTexture(CCTexture2D tex) throws Exception
    {
        if(this.useSpriteSheet || tex == null) throw new Exception("Texture cannot be set when using a spritesheet. Texture cannot be a null value");
        this.texture = tex;
    }

    public CCTexture2D getTexture()
    {
        return this.texture;
    }

    public void setBlendFunction(CCBlend blend)
    {
        this.blend = blend;
    }

    public CCBlend getBlendFunction()
    {
        return this.blend.clone();
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
                CCSprite sprite = (CCSprite) obj;

                if(this.rotation != sprite.rotation || this.scaleX != sprite.scaleX
                   || this.scaleY != sprite.scaleY || !this.position.equals(sprite.position)
                   || this.visible != sprite.visible || !this.anchorPointInPixel.equals(sprite.anchorPointInPixel)
                   || !this.anchorPoint.equals(sprite.anchorPoint) || this.isRelativeAnchorePoint != sprite.isRelativeAnchorePoint
                   || !this.contentSize.equals(sprite.contentSize) || !this.transform.equals(sprite.transform)
                   || this.vertexZ != sprite.vertexZ || this.zOrder != sprite.zOrder || this.tag != sprite.tag
                   || !this.userData.equals(sprite.userData) || this.isRunning != sprite.isRunning
                   || this.isTransformDirty != sprite.isTransformDirty || this.isInverseDirty != sprite.isInverseDirty
                   || !this.offsetPosition.equals(sprite.offsetPosition)
                   || this.index != sprite.index || !this.spriteSheet.equals(sprite.spriteSheet)
                   || this.dirty != sprite.dirty || this.recursiveDirty != sprite.recursiveDirty
                   || this.hasChildren != sprite.hasChildren || !this.blend.equals(sprite.blend)
                   || !this.texture.equals(sprite.texture) || this.useSpriteSheet != sprite.useSpriteSheet
                   || !this.rect.equals(sprite.rect) || !this.honorTransform.equals(sprite.honorTransform)
                   || !this.quad.equals(sprite.quad) || this.opacity != sprite.opacity
                   || !this.colorUnmodified.equals(sprite.colorUnmodified) || !this.color.equals(sprite.color)
                   || this.opacityModifiesRGB != sprite.opacityModifiesRGB || this.flipX != sprite.flipX
                   || this.flipY != sprite.flipY || !this.animations.equals(sprite.animations))
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

    protected boolean isQuadOffScreen(QuadTexture3F quad)
    {
        Dimension screen = CCDirector.getScreenSize();

        //We need to take into account any rotation transforms
        //Technically bl and tl could be greater than tr and br
        //Because of a rotation and the quad could technically still be on the screen
        if(quad.bl.vertice.x < quad.br.vertice.x || quad.tl.vertice.x  < quad.tr.vertice.x)
        {
            if(quad.bl.vertice.y < quad.tl.vertice.y || quad.br.vertice.y < quad.tr.vertice.y)
            {
                if((quad.bl.vertice.x > screen.width && quad.tl.vertice.x > screen.width)
                      || (quad.bl.vertice.y > screen.height && quad.br.vertice.y > screen.height)
                      || (quad.tr.vertice.y < 0 && quad.tl.vertice.y < 0)
                      || (quad.tr.vertice.x < 0 && quad.br.vertice.x < 0))
                {
                    return true;
                }
            }
            else if(quad.bl.vertice.y > quad.tl.vertice.y || quad.br.vertice.y > quad.tr.vertice.y)
            {
                if((quad.bl.vertice.x > screen.width && quad.tl.vertice.x > screen.width)
                   || (quad.tl.vertice.y > screen.height && quad.tr.vertice.y > screen.height)
                   || (quad.bl.vertice.y < 0 && quad.br.vertice.y < 0)
                   || (quad.tr.vertice.x < 0 && quad.br.vertice.x < 0))
                {
                    return true;
                }
            }
        }
        else if(quad.bl.vertice.x > quad.br.vertice.x || quad.tl.vertice.x > quad.tr.vertice.x)
        {
            if(quad.bl.vertice.y < quad.tl.vertice.y || quad.br.vertice.y < quad.tr.vertice.y)
            {
                if((quad.tr.vertice.x > screen.width && quad.br.vertice.x > screen.width)
                      || (quad.bl.vertice.y > screen.height && quad.br.vertice.y > screen.height)
                      || (quad.tr.vertice.y < 0 && quad.tl.vertice.y < 0)
                      || (quad.tl.vertice.x < 0 && quad.bl.vertice.x < 0))
                {
                    return true;
                }
            }
            else if(quad.bl.vertice.y > quad.tl.vertice.y || quad.br.vertice.y > quad.tr.vertice.y)
            {
                if((quad.tr.vertice.x > screen.width && quad.br.vertice.x > screen.width)
                   || (quad.tl.vertice.y > screen.height && quad.tr.vertice.y > screen.height)
                   || (quad.bl.vertice.y < 0 && quad.br.vertice.y < 0)
                   || (quad.tl.vertice.x < 0 && quad.bl.vertice.x < 0))
                {
                    return true;
                }
            }
        }

        //There is one more else if we need to check for and that is if all the points
        //Have been collapsed onto each other, even though this is technically not offscreen
        //It can still save memory by not trying to draw the collapsed quad.
        else if(quad.bl.vertice.x == 0 && quad.bl.vertice.y == 0 && quad.bl.vertice.z == 0
                && quad.br.vertice.x == 0 && quad.br.vertice.y == 0 && quad.br.vertice.z == 0
                && quad.tl.vertice.x == 0 && quad.tl.vertice.y == 0 && quad.tl.vertice.z == 0
                && quad.tr.vertice.x == 0 && quad.tr.vertice.y == 0 && quad.tr.vertice.z == 0)
        {
            return true;
        }

       return false;
    }
}
