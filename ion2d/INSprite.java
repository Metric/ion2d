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
import org.lwjgl.BufferUtils;

public class INSprite extends INNode implements INRGBAProtocol, INTextureProtocol
{
    protected INTextureAtlas textures;
    protected int index;
    protected INSpriteSheet spriteSheet;
    protected boolean dirty;
    protected boolean recursiveDirty;
    protected boolean hasChildren;

    protected INBlend blend;
    protected INTexture2D texture;

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

    protected Hashtable<String, INAnimation> animations;
    protected INSpriteFrame currentFrame;

    protected FloatBuffer colorBuffer;
    protected FloatBuffer textureBuffer;
    protected FloatBuffer verticeBuffer;

    /**
     * Default constructor
     * @throws Exception
     */
    public INSprite()
    {
        this.dirty = this.recursiveDirty = false;
        this.quad = new QuadTexture3F();

        Color4F tempColor = new Color4F(1.0f,1.0f,1.0f,1.0f);
        this.color = this.colorUnmodified = new Color3B(255,255,255);
        this.quad.tl.color = tempColor;
        this.quad.tr.color = tempColor;
        this.quad.bl.color = tempColor;
        this.quad.br.color = tempColor;
        this.opacity = 255;

        this.setTextureRect(new Rectangle(0,0,0,0));

        this.useSelfRender();

        this.opacityModifiesRGB = true;
        this.blend = new INBlend();

        this.flipX = this.flipY = false;
        this.anchorPoint = new Vertex2F(0.5f, 0.5f);

        this.offsetPosition = new Vertex2F();
        this.honorTransform = HonorParentTransform.HonorParentTransformAll;
        this.hasChildren = false;
        this.animations = new Hashtable<String, INAnimation>();
    }

    /**
     * Creates a sprite with the specified INTexture2D
     * @param CCTextured2D tex
     * @throws Exception
     */
    public INSprite(INTexture2D tex) throws Exception
    {
        this(tex, new Rectangle(0,0,tex.getImageSize().width,tex.getImageSize().height));
    }

    /**
     * Creates a sprie with the specified INSpriteFrame
     * @param INSpriteFrame spriteFrame
     * @throws Exception
     */
    public INSprite(INSpriteFrame spriteFrame) throws Exception
    {
        this(spriteFrame.getTexture(), spriteFrame.getRect());
        this.setDisplayFrame(spriteFrame);
    }

    /**
     * Creates a sprite with the specified INSpriteSheet and Rectangle
     * @param INSpriteSheet sheet
     * @param Rectangle rect
     * @throws Exception
     */
    public INSprite(INSpriteSheet sheet, Rectangle rect) throws Exception
    {
        this(sheet.getTexture(), rect);
        this.setUseSpriteSheet(sheet);
    }

    /**
     * Creates a sprite with the specified INTexture2D and Rectangle
     * @param INTexture2D tex
     * @param Rectangle rect
     * @throws Exception
     */
    public INSprite(INTexture2D tex, Rectangle rect) throws Exception
    {
        this.dirty = this.recursiveDirty = false;

        this.setTexture(tex);
        this.quad = new QuadTexture3F();

        Color4F tempColor = new Color4F(1.0f,1.0f,1.0f,1.0f);
        this.color = this.colorUnmodified = new Color3B(255,255,255);
        this.quad.tl.color = tempColor;
        this.quad.tr.color = tempColor;
        this.quad.bl.color = tempColor;
        this.quad.br.color = tempColor;

        this.setTextureRect(rect);

        this.useSelfRender();

        this.opacity = 255;
        this.opacityModifiesRGB = true;
        this.blend = new INBlend();

        this.flipX = this.flipY = false;
        this.anchorPoint = new Vertex2F(0.5f, 0.5f);

        this.offsetPosition = new Vertex2F();
        this.honorTransform = HonorParentTransform.HonorParentTransformAll;
        this.hasChildren = false;
        this.animations = new Hashtable<String, INAnimation>();
    }

    /**
     * Creates a sprite with the specified INTexture2D, Rectangle, and Point2D offset
     * @param CCtexture2D tex
     * @param Rectangle rect
     * @param Point2D offset
     * @throws Exception
     */
    public INSprite(INTexture2D tex, Rectangle rect, Point2D offset) throws Exception
    {
        this.dirty = this.recursiveDirty = false;

        this.setTexture(tex);
        this.quad = new QuadTexture3F();

        Color4F tempColor = new Color4F(1.0f,1.0f,1.0f,1.0f);
        this.color = this.colorUnmodified = new Color3B(255,255,255);
        this.quad.tl.color = tempColor;
        this.quad.tr.color = tempColor;
        this.quad.bl.color = tempColor;
        this.quad.br.color = tempColor;

        this.setTextureRect(rect);

        this.useSelfRender();

        this.opacity = 255;
        this.opacityModifiesRGB = true;
        this.blend = new INBlend();

        this.flipX = this.flipY = false;
        this.anchorPoint = new Vertex2F(0.5f, 0.5f);

        this.offsetPosition = new Vertex2F(offset);
        this.honorTransform = HonorParentTransform.HonorParentTransformAll;
        this.hasChildren = false;
        this.animations = new Hashtable<String, INAnimation>();
    }

    /**
     * Creates a sprite from the specified image file path
     * @param String file
     * @throws Exception
     */
    public INSprite(String file) throws Exception
    {
        INTexture2D tex = INTextureCache.addImage(file);
        Rectangle lrect = new Rectangle(0,0,tex.getImageSize().width,tex.getImageSize().height);

        this.dirty = this.recursiveDirty = false;

        this.setTexture(tex);
        this.quad = new QuadTexture3F();
        
        Color4F tempColor = new Color4F(1.0f,1.0f,1.0f,1.0f);
        this.color = this.colorUnmodified = new Color3B(255,255,255);
        this.quad.tl.color = tempColor;
        this.quad.tr.color = tempColor;
        this.quad.bl.color = tempColor;
        this.quad.br.color = tempColor;

        this.setTextureRect(lrect);

        this.useSelfRender();

        this.opacity = 255;
        this.opacityModifiesRGB = true;
        this.blend = new INBlend();

        this.flipX = this.flipY = false;
        this.anchorPoint = new Vertex2F(0.5f, 0.5f);

        this.offsetPosition = new Vertex2F();
        this.honorTransform = HonorParentTransform.HonorParentTransformAll;
        this.hasChildren = false;
        this.animations = new Hashtable<String, INAnimation>();
    }

    /**
     * Creates a sprite with the specified image file path and Rectangle
     * @param String file
     * @param Rectangle rect
     * @throws Exception
     */
    public INSprite(String file, Rectangle rect) throws Exception
    {
        INTexture2D tex = INTextureCache.addImage(file);

        this.dirty = this.recursiveDirty = false;
     
        this.setTexture(tex);
        this.quad = new QuadTexture3F();

        Color4F tempColor = new Color4F(1.0f,1.0f,1.0f,1.0f);
        this.color = this.colorUnmodified = new Color3B(255,255,255);
        this.quad.tl.color = tempColor;
        this.quad.tr.color = tempColor;
        this.quad.bl.color = tempColor;
        this.quad.br.color = tempColor;

        this.setTextureRect(rect);

        this.useSelfRender();

        this.opacity = 255;
        this.opacityModifiesRGB = true;
        this.blend = new INBlend();

        this.flipX = this.flipY = false;
        this.anchorPoint = new Vertex2F(0.5f, 0.5f);

        this.offsetPosition = new Vertex2F();
        this.honorTransform = HonorParentTransform.HonorParentTransformAll;
        this.hasChildren = false;
        this.animations = new Hashtable<String, INAnimation>();
    }

    /**
     * Creates a sprite with the specified image file path, Rectangle,
     * and Point2D offset
     * @param String file
     * @param Rectangle rect
     * @param Point2D offset
     * @throws Exception
     */
    public INSprite(String file, Rectangle rect, Point2D offset) throws Exception
    {
        INTexture2D tex = INTextureCache.addImage(file);

        this.dirty = this.recursiveDirty = false;

        this.setTexture(tex);
        this.quad = new QuadTexture3F();

        Color4F tempColor = new Color4F(1.0f,1.0f,1.0f,1.0f);
        this.color = this.colorUnmodified = new Color3B(255,255,255);
        this.quad.tl.color = tempColor;
        this.quad.tr.color = tempColor;
        this.quad.bl.color = tempColor;
        this.quad.br.color = tempColor;

        this.setTextureRect(rect);

        this.useSelfRender();

        this.opacity = 255;
        this.opacityModifiesRGB = true;
        this.blend = new INBlend();

        this.flipX = this.flipY = false;
        this.anchorPoint = new Vertex2F(0.5f, 0.5f);

        this.offsetPosition = new Vertex2F(offset);
        this.honorTransform = HonorParentTransform.HonorParentTransformAll;
        this.hasChildren = false;
        this.animations = new Hashtable<String, INAnimation>();
    }

    /**
     * Creates a sprite from the specified INImage
     * @param INImage image
     * @throws Exception
     */
    public INSprite(INImage image) throws Exception
    {
        INTexture2D tex = INTextureCache.addImage(image, image.image.toString());
        Rectangle lrect = new Rectangle(0,0,tex.getImageSize().width,tex.getImageSize().height);

        this.dirty = this.recursiveDirty = false;

        this.setTexture(tex);
        this.quad = new QuadTexture3F();
       
        Color4F tempColor = new Color4F(1.0f,1.0f,1.0f,1.0f);
        this.color = this.colorUnmodified = new Color3B(255,255,255);
        this.quad.tl.color = tempColor;
        this.quad.tr.color = tempColor;
        this.quad.bl.color = tempColor;
        this.quad.br.color = tempColor;

        this.setTextureRect(lrect);

        this.useSelfRender();

        this.opacity = 255;
        this.opacityModifiesRGB = true;
        this.blend = new INBlend();

        this.flipX = this.flipY = false;
        this.anchorPoint = new Vertex2F(0.5f, 0.5f);

        this.offsetPosition = new Vertex2F();
        this.honorTransform = HonorParentTransform.HonorParentTransformAll;
        this.hasChildren = false;
        this.animations = new Hashtable<String, INAnimation>();
    }

    /**
     * Creates a sprite with the specified INImage and Rectangle
     * @param INImage image
     * @param Rectangle rect
     * @throws Exception
     */
    public INSprite(INImage image, Rectangle rect) throws Exception
    {
        INTexture2D tex = INTextureCache.addImage(image, image.image.toString());

        this.dirty = this.recursiveDirty = false;
        
        this.setTexture(tex);
        this.quad = new QuadTexture3F();

        Color4F tempColor = new Color4F(1.0f,1.0f,1.0f,1.0f);
        this.color = this.colorUnmodified = new Color3B(255,255,255);
        this.quad.tl.color = tempColor;
        this.quad.tr.color = tempColor;
        this.quad.bl.color = tempColor;
        this.quad.br.color = tempColor;

        this.setTextureRect(rect);

        this.useSelfRender();

        this.opacity = 255;
        this.opacityModifiesRGB = true;
        this.blend = new INBlend();

        this.flipX = this.flipY = false;
        this.anchorPoint = new Vertex2F(0.5f, 0.5f);

        this.offsetPosition = new Vertex2F();
        this.honorTransform = HonorParentTransform.HonorParentTransformAll;
        this.hasChildren = false;
        this.animations = new Hashtable<String, INAnimation>();
    }

    /**
     * Creates a sprite with the specified INImage, Rectangle, and Point2D
     * Offset
     * @param INImage image
     * @param Rectangle rect
     * @param Point2D offset
     * @throws Exception
     */
    public INSprite(INImage image, Rectangle rect, Point2D offset) throws Exception
    {
         INTexture2D tex = INTextureCache.addImage(image, image.image.toString());

        this.dirty = this.recursiveDirty = false;

        this.setTexture(tex);
        this.quad = new QuadTexture3F();

        Color4F tempColor = new Color4F(1.0f,1.0f,1.0f,1.0f);
        this.color = this.colorUnmodified = new Color3B(255,255,255);
        this.quad.tl.color = tempColor;
        this.quad.tr.color = tempColor;
        this.quad.bl.color = tempColor;
        this.quad.br.color = tempColor;

        this.setTextureRect(rect);

        this.useSelfRender();

        this.opacity = 255;
        this.opacityModifiesRGB = true;
        this.blend = new INBlend();

        this.flipX = this.flipY = false;
        this.anchorPoint = new Vertex2F(0.5f, 0.5f);

        this.offsetPosition = new Vertex2F(offset);
        this.honorTransform = HonorParentTransform.HonorParentTransformAll;
        this.hasChildren = false;
        this.animations = new Hashtable<String, INAnimation>();
    }

    /**
     * Creates a sprite from a image File object
     * @param File file
     * @throws Exception
     */
    public INSprite(File file) throws Exception
    {
        INTexture2D tex = INTextureCache.addImage(file, file.getName());
        Rectangle lrect = new Rectangle(0,0,tex.getImageSize().width,tex.getImageSize().height);
        
        this.dirty = this.recursiveDirty = false;

        this.setTexture(tex);
        this.quad = new QuadTexture3F();

        Color4F tempColor = new Color4F(1.0f,1.0f,1.0f,1.0f);
        this.color = this.colorUnmodified = new Color3B(255,255,255);
        this.quad.tl.color = tempColor;
        this.quad.tr.color = tempColor;
        this.quad.bl.color = tempColor;
        this.quad.br.color = tempColor;

        this.setTextureRect(lrect);

        this.useSelfRender();

        this.opacity = 255;
        this.opacityModifiesRGB = true;
        this.blend = new INBlend();

        this.flipX = this.flipY = false;
        this.anchorPoint = new Vertex2F(0.5f, 0.5f);

        this.offsetPosition = new Vertex2F();
        this.honorTransform = HonorParentTransform.HonorParentTransformAll;
        this.hasChildren = false;
        this.animations = new Hashtable<String, INAnimation>();
    }

    /**
     * Creates a sprite from a image File Object, and Rectangle
     * @param File file
     * @param Rectangle rect
     * @throws Exception
     */
    public INSprite(File file, Rectangle rect) throws Exception
    {
        INTexture2D tex = INTextureCache.addImage(file, file.getName());

        this.dirty = this.recursiveDirty = false;

        this.setTexture(tex);
        this.quad = new QuadTexture3F();

        Color4F tempColor = new Color4F(1.0f,1.0f,1.0f,1.0f);
        this.color = this.colorUnmodified = new Color3B(255,255,255);
        this.quad.tl.color = tempColor;
        this.quad.tr.color = tempColor;
        this.quad.bl.color = tempColor;
        this.quad.br.color = tempColor;

        this.setTextureRect(rect);

        this.useSelfRender();

        this.opacity = 255;
        this.opacityModifiesRGB = true;
        this.blend = new INBlend();

        this.flipX = this.flipY = false;
        this.anchorPoint = new Vertex2F(0.5f, 0.5f);

        this.offsetPosition = new Vertex2F();
        this.honorTransform = HonorParentTransform.HonorParentTransformAll;
        this.hasChildren = false;
        this.animations = new Hashtable<String, INAnimation>();
    }

    /**
     * Creates a sprite with a image File Object, Rectangle, and Point2D
     * Offset
     * @param File file
     * @param Rectangle rect
     * @param Point2D offset
     * @throws Exception
     */
    public INSprite(File file, Rectangle rect, Point2D offset) throws Exception
    {
        INTexture2D tex = INTextureCache.addImage(file, file.getName());

        this.dirty = this.recursiveDirty = false;

        this.setTexture(tex);
        this.quad = new QuadTexture3F();

        Color4F tempColor = new Color4F(1.0f,1.0f,1.0f,1.0f);
        this.color = this.colorUnmodified = new Color3B(255,255,255);
        this.quad.tl.color = tempColor;
        this.quad.tr.color = tempColor;
        this.quad.bl.color = tempColor;
        this.quad.br.color = tempColor;

        this.setTextureRect(rect);

        this.useSelfRender();

        this.opacity = 255;
        this.opacityModifiesRGB = true;
        this.blend = new INBlend();

        this.flipX = this.flipY = false;
        this.anchorPoint = new Vertex2F(0.5f, 0.5f);
        this.offsetPosition = new Vertex2F(offset);
        this.honorTransform = HonorParentTransform.HonorParentTransformAll;
        this.hasChildren = false;
        this.animations = new Hashtable<String, INAnimation>();
    }

    /**
     * Tell the sprite to use self rendering instead of
     * a sprite sheet
     */
    public void useSelfRender()
    {
        this.index = -1;
        this.useSpriteSheet = false;
        this.textures = null;
        //Add Spritesheet support
        this.dirty = this.recursiveDirty = false;
        
        float x1 = this.offsetPosition.x;
        float y1 = this.offsetPosition.y;

        float x2 = x1;
        float y2 = y1;

        x2 +=  this.rect.width;
        y2 += this.rect.height;

        this.quad.bl.vertice = new Vertex3F(x1, y1, 0);
        this.quad.br.vertice = new Vertex3F(x2, y1, 0);
        this.quad.tl.vertice = new Vertex3F(x1, y2, 0);
        this.quad.tr.vertice = new Vertex3F(x2, y2, 0);
    }

    /**
     * Sets the texture rect of the sprite
     * @param Rectangle rect
     */
    public void setTextureRect(Rectangle rect)
    {
        this.setTextureRect(rect, rect.getSize());
    }

    /**
     * Sets the texture rect of the Sprite
     * @param Rectangle rect
     * @param Dimension untrimmed
     */
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

    /**
     * Update the texture coordinates based on the Rectangle given
     * @param Rectangle rect
     */
    public void updateTextureCoords(Rectangle rect)
    {
        if(this.texture != null)
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
    }

    /**
     * Updates the transform of the sprite
     * Only used if using SpriteSheet
     */
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
                float radians = (float)INMath.degreesToRadians(this.rotation);
                float c = (float)Math.cos(radians);
                float s = (float)Math.sin(radians);

                matrix = new AffineTransform(c * this.scaleX, s * this.scaleX, -s * this.scaleY,
                                                            c * this.scaleY, this.position.x, this.position.y);

                matrix.translate(-this.anchorPointInPixel.x, -this.anchorPointInPixel.y);
            }
            else
            {
                matrix = new AffineTransform();
                matrix.setToIdentity();

                HonorParentTransform prev = HonorParentTransform.HonorParentTransformAll;

                for(INNode p = this; p != this.spriteSheet; p = p.parent)
                {
                    AffineTransform newMatrix = new AffineTransform();
                    newMatrix.setToIdentity();

                    //2nd translate rotate and scale
                    if(prev == HonorParentTransform.HonorParentTransformTranslate)
                    {
                        newMatrix.translate(p.getPosition().x, p.getPosition().y);
                    }
                    else if(prev == HonorParentTransform.HonorParentTransformRotate)
                    {
                        newMatrix.rotate(INMath.degreesToRadians(p.getRotation()));
                    }
                    else if(prev == HonorParentTransform.HonorParentTransformScale)
                    {
                        newMatrix.scale(p.getScaleX(), p.getScaleY());
                    }

                    //3rd: translate anchor point
                    newMatrix.translate(-p.getAnchorePointInPixel().x, -p.getAnchorePointInPixel().y);

                    //4th: Matrix multiplication
                    matrix.concatenate(newMatrix);

                    INSprite temp = (INSprite)p;
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

            float ax = x1 * cr - y1 * sr + x;
            float ay = x1 * sr + y1 * cr + y;

            float bx = x2 * cr - y1 * sr2 + x;
            float by = x2 * sr + y1 * cr2 + y;

            float cx = x2 * cr - y2 * sr + x;
            float cy = x2 * sr + y2 * cr + y;

            float dx = x1 * cr2 - y2 * sr + x;
            float dy = x1 * sr2 + y2 * cr + y;

            this.quad.bl.vertice = new Vertex3F(ax, ay, this.vertexZ);
            this.quad.br.vertice = new Vertex3F(bx, by, this.vertexZ);
            this.quad.tl.vertice = new Vertex3F(dx, dy, this.vertexZ);
            this.quad.tr.vertice = new Vertex3F(cx, cy, this.vertexZ);

            this.textures.updateQuad(this.quad, this.index);
            this.dirty = this.recursiveDirty = false;
        }
    }

    /**
     * Draws the sprite
     * Only used if self rendering
     */
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

        this.updateColorBuffer();
        this.updateTextureBuffer();
        this.updateVertexBuffer();

        //Color
        GL11.glColorPointer(4, 0, this.colorBuffer);

        //Texture
        GL11.glTexCoordPointer(2, 0, this.textureBuffer);

        //Vertex
        GL11.glVertexPointer(3, 0, this.verticeBuffer);

        GL11.glDrawArrays(GL11.GL_QUADS, 0, 4);

        if(newBlend)
        {
            GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        }
    }

    /**
     * Updates the texture coordinates of the sprites quad
     */
    private void updateTextureBuffer()
    {
        if(this.textureBuffer == null)
        {
            this.textureBuffer = BufferUtils.createFloatBuffer(2*4);

            this.textureBuffer.put(this.quad.bl.coords.x);
            this.textureBuffer.put(this.quad.bl.coords.y);

            this.textureBuffer.put(this.quad.br.coords.x);
            this.textureBuffer.put(this.quad.br.coords.y);

            this.textureBuffer.put(this.quad.tr.coords.x);
            this.textureBuffer.put(this.quad.tr.coords.y);

            this.textureBuffer.put(this.quad.tl.coords.x);
            this.textureBuffer.put(this.quad.tl.coords.y);

            this.textureBuffer.rewind();
        }
        else
        {
            this.textureBuffer.put(0,this.quad.bl.coords.x);
            this.textureBuffer.put(1,this.quad.bl.coords.y);

            this.textureBuffer.put(2,this.quad.br.coords.x);
            this.textureBuffer.put(3,this.quad.br.coords.y);

            this.textureBuffer.put(4,this.quad.tr.coords.x);
            this.textureBuffer.put(5,this.quad.tr.coords.y);

            this.textureBuffer.put(6,this.quad.tl.coords.x);
            this.textureBuffer.put(7,this.quad.tl.coords.y);

            this.textureBuffer.rewind();
        }
    }

    /**
     * Gets the colors from the sprites quad
     * @return FloatBuffer
     */
    private void updateColorBuffer()
    {
        if(this.colorBuffer == null)
        {
            this.colorBuffer = BufferUtils.createFloatBuffer(4*4);

            this.colorBuffer.put(this.quad.bl.color.getRed());
            this.colorBuffer.put(this.quad.bl.color.getGreen());
            this.colorBuffer.put(this.quad.bl.color.getBlue());
            this.colorBuffer.put(this.quad.bl.color.getAlpha());

            this.colorBuffer.put(this.quad.br.color.getRed());
            this.colorBuffer.put(this.quad.br.color.getGreen());
            this.colorBuffer.put(this.quad.br.color.getBlue());
            this.colorBuffer.put(this.quad.br.color.getAlpha());

            this.colorBuffer.put(this.quad.tr.color.getRed());
            this.colorBuffer.put(this.quad.tr.color.getGreen());
            this.colorBuffer.put(this.quad.tr.color.getBlue());
            this.colorBuffer.put(this.quad.tr.color.getAlpha());

            this.colorBuffer.put(this.quad.tl.color.getRed());
            this.colorBuffer.put(this.quad.tl.color.getGreen());
            this.colorBuffer.put(this.quad.tl.color.getBlue());
            this.colorBuffer.put(this.quad.tl.color.getAlpha());

            this.colorBuffer.rewind();
        }
        else
        {
            this.colorBuffer.put(0,this.quad.bl.color.getRed());
            this.colorBuffer.put(1,this.quad.bl.color.getGreen());
            this.colorBuffer.put(2,this.quad.bl.color.getBlue());
            this.colorBuffer.put(3,this.quad.bl.color.getAlpha());

            this.colorBuffer.put(4,this.quad.br.color.getRed());
            this.colorBuffer.put(5,this.quad.br.color.getGreen());
            this.colorBuffer.put(6,this.quad.br.color.getBlue());
            this.colorBuffer.put(7,this.quad.br.color.getAlpha());

            this.colorBuffer.put(8,this.quad.tr.color.getRed());
            this.colorBuffer.put(9,this.quad.tr.color.getGreen());
            this.colorBuffer.put(10,this.quad.tr.color.getBlue());
            this.colorBuffer.put(11,this.quad.tr.color.getAlpha());

            this.colorBuffer.put(12,this.quad.tl.color.getRed());
            this.colorBuffer.put(13,this.quad.tl.color.getGreen());
            this.colorBuffer.put(14,this.quad.tl.color.getBlue());
            this.colorBuffer.put(15,this.quad.tl.color.getAlpha());
            this.colorBuffer.rewind();
        }
    }

    /**
     * Updates the vertices for the sprites quad
     */
    private void updateVertexBuffer()
    {
        if(this.verticeBuffer == null)
        {
            this.verticeBuffer = BufferUtils.createFloatBuffer(3 * 4);

            this.verticeBuffer.put(this.quad.bl.vertice.x);
            this.verticeBuffer.put(this.quad.bl.vertice.y);
            this.verticeBuffer.put(this.quad.bl.vertice.z);

            this.verticeBuffer.put(this.quad.br.vertice.x);
            this.verticeBuffer.put(this.quad.br.vertice.y);
            this.verticeBuffer.put(this.quad.br.vertice.z);

            this.verticeBuffer.put(this.quad.tr.vertice.x);
            this.verticeBuffer.put(this.quad.tr.vertice.y);
            this.verticeBuffer.put(this.quad.tr.vertice.z);

            this.verticeBuffer.put(this.quad.tl.vertice.x);
            this.verticeBuffer.put(this.quad.tl.vertice.y);
            this.verticeBuffer.put(this.quad.tl.vertice.z);

            this.verticeBuffer.rewind();
        }
        else
        {
            this.verticeBuffer.put(0,this.quad.bl.vertice.x);
            this.verticeBuffer.put(1,this.quad.bl.vertice.y);
            this.verticeBuffer.put(2,this.quad.bl.vertice.z);

            this.verticeBuffer.put(3,this.quad.br.vertice.x);
            this.verticeBuffer.put(4,this.quad.br.vertice.y);
            this.verticeBuffer.put(5,this.quad.br.vertice.z);

            this.verticeBuffer.put(6,this.quad.tr.vertice.x);
            this.verticeBuffer.put(7,this.quad.tr.vertice.y);
            this.verticeBuffer.put(8,this.quad.tr.vertice.z);

            this.verticeBuffer.put(9,this.quad.tl.vertice.x);
            this.verticeBuffer.put(10,this.quad.tl.vertice.y);
            this.verticeBuffer.put(11,this.quad.tl.vertice.z);

            this.verticeBuffer.rewind();
        }
    }

    /**
     * addChild override
     * @param INNode node
     * @return INNode
     * @throws Exception
     */
    public INNode addChild(INNode node) throws Exception
    {
        INNode returnNode = super.addChild(node);
        
        if(this.useSpriteSheet)
        {
            if(this.getClass() != node.getClass()) throw new Exception("INSprite only supports INSprites as children when using SpriteSheet");
            INSprite sprite = (INSprite)node;

            if(this.textures.texture.getId() != sprite.texture.getId()) throw new Exception("INSprite is not using the same texture id");

            this.spriteSheet.addChild(node);
        }

        this.hasChildren = true;
        return returnNode;
    }

    /**
     * addChild override
     * @param INNode node
     * @param int z
     * @return INNode
     */
    public INNode addChild(INNode node, int z) throws Exception
    {
         INNode returnNode = super.addChild(node, z);
        
        if(this.useSpriteSheet)
        {
            if(this.getClass() != node.getClass()) throw new Exception("CCSprite only supports CCSprites as children when using SpriteSheet");
            INSprite sprite = (INSprite)node;

            if(this.textures.texture.getId() != sprite.texture.getId()) throw new Exception("CCSprite is not using the same texture id");

            this.spriteSheet.addChild(node, z);
        }

        this.hasChildren = true;
        return returnNode;       
    }

    /**
     * INSprite addChild Override
     * @param INNode node
     * @param int z
     * @param int tag
     * @return INNode
     */
    public INNode addChild(INNode node, int z, int tag) throws Exception
    {
        INNode returnNode = super.addChild(node, z, tag);
        
        if(this.useSpriteSheet)
        {
            if(this.getClass() != node.getClass()) throw new Exception("CCSprite only supports CCSprites as children when using SpriteSheet");
            INSprite sprite = (INSprite)node;

            if(this.textures.texture.getId() != sprite.texture.getId()) throw new Exception("CCSprite is not using the same texture id");

            this.spriteSheet.addChild(node, z, tag);
        }

        this.hasChildren = true;

        return returnNode;
    }

    /**
     * Removes the specified INSprite as a child
     * @param INSprite sprite
     * @param boolean cleanup
     */
    public void removeChild(INSprite sprite, boolean cleanup)
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
        INSprite sprite = (INSprite)this.getChildByTag(tag);
        
        if(this.useSpriteSheet)
           this.spriteSheet.removeChild(sprite, cleanup);
        
        if(this.children.size() > 0) 
            this.hasChildren = true;
        else
            this.hasChildren = false;
    }

    /**
     * Removes all the children associated with the sprite
     * @param boolean cleanup
     */
    public void removeAllChildrenWithCleanup(boolean cleanup)
    {
        if(this.useSpriteSheet)
        {
            this.spriteSheet.removeAllChildrenWithCleanup(cleanup);
        }
        
        super.removeAllChildrenWithCleanup(cleanup);

        this.hasChildren = false;
    }

    /**
     * Sets the dirty recursively sprite update
     * @param boolean doit
     */
    public void setDirtyRecursively(boolean doit)
    {
        this.dirty = this.recursiveDirty = doit;
        if(this.hasChildren)
        {
            for(INNode child : this.children)
            {
                if(child instanceof INSprite)
                {
                    INSprite temp = (INSprite)child;
                    temp.setDirtyRecursively(true);
                }
            }
        }
    }

    /**
     * Sets the sprites position
     * @param Vertex2F point
     */
    public void setPosition(Vertex2F point)
    {
        super.setPosition(point);
        if(this.useSpriteSheet && !this.recursiveDirty)
            this.setDirtyRecursively(true);
    }

    /**
     * Sets the sprites rotation
     * @param float rotation
     */
    public void setRotation(float rotation)
    {
        super.setRotation(rotation);
        if(this.useSpriteSheet && !this.recursiveDirty)
            this.setDirtyRecursively(true);
    }

    /**
     * Sets the x scale of the sprite
     * @param float scale
     */
    public void setScaleX(float scale)
    {
        super.setScaleX(scale);
        if(this.useSpriteSheet && !this.recursiveDirty)
            this.setDirtyRecursively(true);
    }

    /**
     * Sets the y scale of the sprite
     * @param float scale
     */
    public void setScaleY(float scale)
    {
        super.setScaleY(scale);
        if(this.useSpriteSheet && !this.recursiveDirty)
            this.setDirtyRecursively(true);
    }

    /**
     * Sets the x and y scale of the sprite
     * @param float scale
     */
    public void setScale(float scale)
    {
        super.setScale(scale);
        if(this.useSpriteSheet && !this.recursiveDirty)
            this.setDirtyRecursively(true);
    }

    /**
     * Sets the sprites vertex z position
     * @param float z
     */
    public void setVertexZ(float z)
    {
        super.setVertexZ(z);
        if(this.useSpriteSheet && !this.recursiveDirty)
            this.setDirtyRecursively(true);
    }

    /**
     * Sets the anchor point of the sprite
     * @param Vertex2F point
     */
    public void setAnchorPoint(Vertex2F point)
    {
        super.setAnchorPoint(point);
        if(this.useSpriteSheet && !this.recursiveDirty)
            this.setDirtyRecursively(true);
    }

    /**
     * Sets whether the anchor point is relative
     * @param boolean relative
     */
    public void setRelativeAnchorPoint(boolean relative)
    {
        if(this.useSpriteSheet) return;
        
        super.setIsRelativeAnchorePoint(relative);
    }

    /**
     * Sets the sprites visibility
     * @param boolean visible
     */
    public void setVisible(boolean visible)
    {
        if(visible != this.visible)
        {
            super.setVisible(visible);
            if(this.useSpriteSheet && !this.recursiveDirty)
            {
                this.dirty = this.recursiveDirty = true;
                for(INNode child : this.children)
                {
                    child.setVisible(visible);
                }
            }
        }
    }

    /**
     * Flips the sprite along the X direction
     * @param boolean flip
     */
    public void setFlipX(boolean flip)
    {
        if(this.flipX != flip)
        {
            this.flipX = flip;
            this.setTextureRect(this.rect);
        }
    }

    /**
     * Flips the sprite along the Y direction
     * @param boolean flip
     */
    public void setFlipY(boolean flip)
    {
        if(this.flipY != flip)
        {
            this.flipY = flip;
            this.setTextureRect(this.rect);
        }
    }

    /**
     * Sets the display frame of the INSpriteFrame
     * @param INSpriteFrame frame
     * @throws Exception
     */
    public void setDisplayFrame(INSpriteFrame frame) throws Exception
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

        INAnimation anim = this.animations.get(animationName);
        INSpriteFrame frame = anim.getFrames().get(index);

        if(frame == null) throw new Exception("CCSprite.setDisplayFrame - Invalid Frame");
        this.setDisplayFrame(frame);
    }

    /**
     * Checks to see if the specified frame is displayed
     * @param INSpriteFrame frame
     * @return boolean
     */
    public boolean isFrameDisplayed(INSpriteFrame frame)
    {
        return this.currentFrame.equals(frame);
    }

    /**
     * Gets the currently displayed frame
     * @return INSpriteFrame
     */
    public INSpriteFrame getCurrentFrame()
    {
        return this.currentFrame;
    }

    /**
     * Adds an animation to the sprite
     * @param anim
     */
    public void addAnimation(INAnimation anim)
    {
        if(this.animations == null)
            this.initAnimations();
        
        this.animations.put(anim.getName(), anim);

        try
        {
            this.setDisplayFrame(anim.getFrames().get(0));
        } catch (Exception e)
        {
            System.out.println(e);
            e.printStackTrace();
        }
    }

    /**
     * Gets the animation by name
     * @param String name
     * @return INAnimation
     */
    public INAnimation getAnimation(String name)
    {
        return this.animations.get(name);
    }

    /**
     * Initializes the holder for the sprites animations
     */
    private void initAnimations()
    {
        this.animations = new Hashtable<String, INAnimation>(2,2);
    }

    /**
     * RGBA Protocol
     * Update the color
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

    /**
     * Gets ths sprite's quad
     * The quad is a clone and any editing done to it will not
     * affect the sprite
     * @return QuadTexture3F
     */
    public QuadTexture3F getQuad() { return this.quad.clone(); }


    /**
     * Returns the current dirty status of the sprite
     * @return boolean
     */
    public boolean isDirty() { return this.dirty; }

    /**
     * Gets the sprite's rectangle
     * @return Rectangle
     */
    public Rectangle getRect() { return (Rectangle)this.rect.clone(); }

    /**
     * Returns the current flip x status
     * @return boolean
     */
    public boolean getFlipX() { return this.flipX; }

    /**
     * Returns the current flip y status
     * @return boolean
     */
    public boolean getFlipY() { return this.flipY; }

    /**
     * Returns the color or unmodified color of the sprite
     * The color object returned is a clone and any editing
     * will not affect the sprite's color
     * @return Color3B
     */
    public Color3B getColor() { if(this.opacityModifiesRGB) return this.colorUnmodified.clone(); else return this.color.clone(); }

    /**
     * Returns the sprite's sprite sheet status of whether it uses
     * one or not
     * @return boolean
     */
    public boolean usesSpriteSheet() { return this.useSpriteSheet; }

    /**
     * Sets the use of the sprite sheet for the sprite
     * @param INSpriteSheet sheet
     */
    protected void setUseSpriteSheet(INSpriteSheet sheet)
    {
        this.useSpriteSheet = true;
        this.spriteSheet = sheet;
        this.textures = this.spriteSheet.getTextureAtlas();

        try
        {
            this.spriteSheet.addChild(this);
        } catch (Exception e)
        {
            System.out.println(e);
            e.printStackTrace();
        }
    }

    /**
     * Returns the status of the current honor parent transform status
     * @return HonorParentTransform
     */
    public HonorParentTransform honorsParentTransform() { return this.honorTransform; }

    /**
     * Sets the honorParentTransform status
     * @param HonorParentTransform transform
     */
    public void setHonorParentTransform(HonorParentTransform transform)
    {
        this.honorTransform = transform;
    }

    /**
     * Sets the current atlas index
     * @param int index
     */
    public void setAtlasIndex(int index)
    {
        this.index = index;
    }

    /**
     * Returns whether the opacity of the sprite modifies the RGB value
     * @return boolean
     */
    public boolean doesOpacityModifyRGB()
    {
        return this.opacityModifiesRGB;
    }

    /**
     * Gets the opacity of the sprite
     * Ranges from 0.0 to 1.0
     * @return float
     */
    public float getOpacity()
    {
        return (this.opacity/255.0f);
    }

    /**
     * Sets the opacity level
     * Ranges from 0.0 to 1.0
     * @param float level
     */
    public void setOpacity(float level)
    {
        if(level > 1.0f) level = 1.0f;
        if(level < 0.0f) level = 0.0f;

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

    /**
     * Sets the color overlay of the sprite
     * @param Color3B color
     */
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

    /**
     * Sets whether the opacity should modify the RGB value of the sprite
     * @param boolean modify
     */
    public void setOpacityModifyRGB(boolean modify)
    {
        this.opacityModifiesRGB = modify;
    }

    /**
     * Sets the specified texture to use for the sprite
     * @param INTexture2D tex
     * @throws Exception
     */
    public void setTexture(INTexture2D tex) throws Exception
    {
        if(this.useSpriteSheet || tex == null) throw new Exception("Texture cannot be set when using a spritesheet. Texture cannot be a null value");
        this.texture = tex;
    }

    /**
     * Gets the current INTexture2D of the sprite
     * The texture returned is a shallow copy and any modifications made
     * will affect the sprites texture
     * @return INTexture2D
     */
    public INTexture2D getTexture()
    {
        return this.texture;
    }

    /**
     * Sets the blend function to use for the sprite
     * @param INBlend blend
     */
    public void setBlendFunction(INBlend blend)
    {
        this.blend = blend;
    }

    /**
     * Gets the current blend function used by the sprite
     * @return INBlend
     */
    public INBlend getBlendFunction()
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
                INSprite sprite = (INSprite) obj;

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

    /**
     * Finalize override
     */
    public void finalize()
    {
        this.removeAllChildrenWithCleanup(true);
    }

    /**
     * Determines if the sprites Quad is offscreen
     * @param QuadTexture3F quad
     * @return boolean
     */
    protected boolean isQuadOffScreen(QuadTexture3F quad)
    {
        Dimension screen = INDirector.getScreenSize();

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
