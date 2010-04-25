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

package cocos2d.support;

import java.awt.geom.*;
import java.awt.Color;
import org.lwjgl.opengl.*;

//Holds Various Class Definitions for Different Types of Structures
//They are all static classes
public class CCTypes
{
    public enum HonorParentTransform
    {
        CCHonorParentTransformTranslate,
        CCHonorParentTransformRotate,
        CCHonorParentTransformScale,
        CCHonorParentTransformAll
    }

    public enum PixelFormat {
        CCPixelFormatRGB565, 
        CCPixelFormatRGBA8888,
        CCPixelFormatAutomatic,
        CCPixelFormatA8,
        CCPixelFormatRGBA4444,
        CCPixelFormatRGB5A1
    }

    public enum ProjectionFormat {
        CCDirectorProjection3D,
        CCDirectorProjection2D        
    }

    public static float FLT_EPSILON = 1.19209290e-07f;

   //Color Structures
    public static class Color3B
    {
        private int r;
        private int g;
        private int b;

        public Color3B()
        {
            this.r = 0;
            this.g = 0;
            this.b = 0;
        }

        public Color3B(int red, int green, int blue)
        {
            if(red > 255) red = 255;
            if(green > 255) green = 255;
            if(blue > 255) blue = 255;
            
            if(red < 0) red = 0;
            if(green < 0) green = 0;
            if(blue < 0) blue = 0;

            this.r = red;
            this.g = green;
            this.b = blue;
        }

        public Color3B(Color3B original)
        {
            if(original == null) throw new NullPointerException();

            this.r = original.r;
            this.g = original.g;
            this.b = original.b;
        }

        public Color3B clone()
        {
            return new Color3B(this);
        }

        public int getRed() { return this.r; }
        public int getGreen() { return this.g; }
        public int getBlue() { return this.b; }

        public void setRed(int red)
        {
            if(red > 255) red = 255;
            if(red < 0 ) red = 0;

            this.r = red;
        }

        public void setGreen(int green)
        {
            if(green > 255) green = 255;
            if(green < 0) green = 0;

            this.g = green;
        }

        public void setBlue(int blue)
        {
            if(blue > 255) blue = 255;
            if(blue < 0) blue = 0;

            this.b = blue;
        }

        public Color toColor()
        {
            return new Color(this.r, this.g, this.b);
        }

        /**
         * Equals Method override //So much easier to validate with Iterator ;]
         * @param Object obj
         * @return bool
         */
        public boolean equals ( Object obj )
        {
            if ( this == obj ) return true;

            if ((obj != null) && (getClass() == obj.getClass()))
            {
                    Color3B color = (Color3B) obj;

                    if(color.b != this.b || color.g != this.g || color.r != this.r)
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

    public static class Color4B
    {
        private int r;
        private int g;
        private int b;
        private int a;

        public Color4B()
        {
            this.r = 0;
            this.g = 0;
            this.b = 0;
            this.a = 255;
        }

        public Color4B(int red, int green, int blue, int alpha)
        {
            if(red > 255) red = 255;
            if(green > 255) green = 255;
            if(blue > 255) blue = 255;
            if(alpha > 255) alpha = 255;

            if(red < 0) red = 0;
            if(green < 0) green = 0;
            if(blue < 0) blue = 0;
            if(alpha < 0) alpha = 0;

            this.r = red;
            this.g = green;
            this.b = blue;
            this.a = alpha;
        }

        public Color4B(Color4B original)
        {
            if(original == null) throw new NullPointerException();

            this.r = original.r;
            this.g = original.g;
            this.b = original.b;
            this.a = original.a;
        }

        public Color4B clone()
        {
            return new Color4B(this);
        }

        public int getRed() { return this.r; }
        public int getGreen() { return this.g; }
        public int getBlue() { return this.b; }
        public int getAlpha() { return this.a; }

        public void setRed(int red)
        {
            if(red > 255) red = 255;
            if(red < 0 ) red = 0;

            this.r = red;
        }

        public void setGreen(int green)
        {
            if(green > 255) green = 255;
            if(green < 0) green = 0;

            this.g = green;
        }

        public void setBlue(int blue)
        {
            if(blue > 255) blue = 255;
            if(blue < 0) blue = 0;

            this.b = blue;
        }

        public void setAlpha(int alpha)
        {
            if(alpha > 255) alpha = 255;
            if(alpha < 0) alpha = 0;

            this.a = alpha;
        }

        public Color toColor()
        {
            return new Color(this.r, this.g, this.b, this.a);
        }

        /**
         * Equals Method override //So much easier to validate with Iterator ;]
         * @param Object obj
         * @return bool
         */
        public boolean equals ( Object obj )
        {
            if ( this == obj ) return true;

            if ((obj != null) && (getClass() == obj.getClass()))
            {
                    Color4B color = (Color4B) obj;

                    if(color.b != this.b || color.g != this.g || color.r != this.r || color.a != this.a)
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

    public static class Color4F
    {
        private float r;
        private float g;
        private float b;
        private float a;

        public Color4F()
        {
            this.r = 0;
            this.g = 0;
            this.b = 0;
            this.a = 1.0f;
        }

        public Color4F(float red, float green, float blue, float alpha)
        {
            if(red > 1) red = 1.0f;
            if(green > 1) green = 1.0f;
            if(blue > 1) blue = 1.0f;
            if(alpha > 1) alpha = 1.0f;

            if(red < 0) red = 0;
            if(green < 0) green = 0;
            if(blue < 0) blue = 0;
            if(alpha < 0) alpha = 0;

            this.r = red;
            this.g = green;
            this.b = blue;
            this.a = alpha;
        }

        public Color4F(Color4F original)
        {
            if(original == null) throw new NullPointerException();

            this.r = original.r;
            this.g = original.g;
            this.b = original.b;
            this.a = original.a;
        }

        public Color4F clone()
        {
            return new Color4F(this);
        }

        public float getRed() { return this.r; }
        public float getGreen() { return this.g; }
        public float getBlue() { return this.b; }
        public float getAlpha() { return this.a; }

        public void setRed(float red)
        {
            if(red > 1) red = 1.0f;
            if(red < 0 ) red = 0;

            this.r = red;
        }

        public void setGreen(float green)
        {
            if(green > 1) green = 1.0f;
            if(green < 0) green = 0;

            this.g = green;
        }

        public void setBlue(float blue)
        {
            if(blue > 1) blue = 1.0f;
            if(blue < 0) blue = 0;

            this.b = blue;
        }

        public void setAlpha(float alpha)
        {
            if(alpha > 1) alpha = 1.0f;
            if(alpha < 0) alpha = 0;

            this.a = alpha;
        }

        public Color toColor()
        {
            return new Color(this.r, this.g, this.b, this.a);
        }

        /**
         * Equals Method override //So much easier to validate with Iterator ;]
         * @param Object obj
         * @return bool
         */
        public boolean equals ( Object obj )
        {
            if ( this == obj ) return true;

            if ((obj != null) && (getClass() == obj.getClass()))
            {
                    Color4F color = (Color4F) obj;

                    if(color.b != this.b || color.g != this.g || color.r != this.r || color.a != this.a)
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

    public static final Color3B WHITE = new Color3B(255,255,255);
    public static final Color3B YELLOW = new Color3B(255,255,0);
    public static final Color3B BLUE = new Color3B(0,0,255);
    public static final Color3B GREEN = new Color3B(0,255,0);
    public static final Color3B RED = new Color3B(255,0,0);
    public static final Color3B BLACK = new Color3B(0,0,0);
    public static final Color3B ORANGE = new Color3B(255,127,0);
    public static final Color3B GRAY = new Color3B(166,166,166);

    /**
     * Returns a Color4F from a Color3B. Alpha will be 1
     * @param Color3B c
     * @return Color4F
     */
    public static Color4F color4FFromColor3B(Color3B c)
    {
        return new Color4F(c.getRed()/255.0f, c.getGreen()/255.0f, c.getBlue()/255.0f, 1.0f);
    }

    /**
     * Returns a Color4F from a Color4B.
     * @param Color4B c
     * @return Color4F
     */
    public static Color4F color4FFromColor4B(Color4B c)
    {
        return new Color4F(c.getRed()/255.0f, c.getGreen()/255.0f, c.getBlue()/255.0f, c.getAlpha()/255.0f);
    }

    //Points
    public static class Vertex3F
    {
        public float x;
        public float y;
        public float z;

        public Vertex3F()
        {
            this.x = 0.0f;
            this.y = 0.0f;
            this.z = 0.0f;
        }

        public Vertex3F(Point2D point)
        {
            this.x = (float)point.getX();
            this.y = (float)point.getY();
            this.z = 0.0f;
        }

        public Vertex3F(float x, float y, float z)
        {
            this.x = x;
            this.y = y;
            this.z = z;
        }

        public Vertex3F(Vertex3F original)
        {
            if(original == null) throw new NullPointerException();

            this.x = original.x;
            this.y = original.y;
            this.z = original.z;
        }

        public Point2D toPoint2D()
        {
            return new Point2D.Float(this.x, this.y);
        }

        public Vertex3F clone()
        {
            return new Vertex3F(this);
        }

        /**
         * Equals Method override //So much easier to validate with Iterator ;]
         * @param Object obj
         * @return bool
         */
        public boolean equals ( Object obj )
        {
            if ( this == obj ) return true;

            if ((obj != null) && (getClass() == obj.getClass()))
            {
                    Vertex3F vertice = (Vertex3F) obj;

                    if(vertice.x != this.x || vertice.y != this.y || vertice.z != this.z)
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

    public static class Vertex2F
    {
        public float x;
        public float y;

        public Vertex2F()
        {
            this.x = 0.0f;
            this.y = 0.0f;
        }

        public Vertex2F(float x, float y)
        {
            this.x = x;
            this.y = y;
        }

        public Vertex2F(Point2D point)
        {
            this.x = (float)point.getX();
            this.y = (float)point.getY();
        }

        public Vertex2F(Vertex2F original)
        {
            if(original == null) throw new NullPointerException();

            this.x = original.x;
            this.y = original.y;
        }

        public Point2D toPoint2D()
        {
            return new Point2D.Float(this.x, this.y);
        }

        public Vertex2F clone()
        {
            return new Vertex2F(this);
        }

        /**
         * Equals Method override //So much easier to validate with Iterator ;]
         * @param Object obj
         * @return bool
         */
        public boolean equals ( Object obj )
        {
            if ( this == obj ) return true;

            if ((obj != null) && (getClass() == obj.getClass()))
            {
                    Vertex2F vertice = (Vertex2F) obj;

                    if(vertice.x != this.x || vertice.y != this.y)
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

    /**
     * Converts a Vertex2F to a Vertex3F. The z will be set to 0.0.
     * @param Vertex2F v
     * @return Vertex3F
     */
    public static Vertex3F vertex3FFromVertex2F(Vertex2F v)
    {
        return new Vertex3F(v.x, v.y, 0.0f);
    }
    
    public static class PointSprite
    {
        public Vertex2F position;
        public Color4F color;
        public float size;
        
        public PointSprite()
        {
            this.position = new Vertex2F();
            this.color = new Color4F();
            this.size = 0.0f;
        }
        
        public PointSprite(Vertex2F position, Color4F color, float size)
        {
            this.position = position;
            this.color = color;
            this.size = size;
        }

        public PointSprite(PointSprite original)
        {
            if(original == null) throw new NullPointerException();

            this.position = original.position.clone();
            this.color = original.color.clone();
            this.size = original.size;
        }

        public PointSprite clone()
        {
            return new PointSprite(this);
        }

        /**
         * Equals Method override //So much easier to validate with Iterator ;]
         * @param Object obj
         * @return bool
         */
        public boolean equals ( Object obj )
        {
            if ( this == obj ) return true;

            if ((obj != null) && (getClass() == obj.getClass()))
            {
                    PointSprite sprite = (PointSprite) obj;

                    if(!this.position.equals(sprite.position) || !this.color.equals(sprite.color) || this.size != sprite.size)
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

    //Quad Classes
    public static class Quad2 
    {
        public Vertex2F tl;
        public Vertex2F tr;
        public Vertex2F bl;
        public Vertex2F br;
        
        public Quad2()
        {
            this.tl = new Vertex2F();
            this.tr = new Vertex2F();
            this.bl = new Vertex2F();
            this.br = new Vertex2F();
        }
        
        public Quad2(Vertex2F tl, Vertex2F tr, Vertex2F bl, Vertex2F br)
        {
            this.tl = tl;
            this.tr = tr;
            this.bl = bl;
            this.br = br;
        }

        public Quad2(Quad2 original)
        {
            if(original == null) throw new NullPointerException();

            this.tl = original.tl.clone();
            this.tr = original.tr.clone();
            this.bl = original.bl.clone();
            this.br = original.br.clone();
        }

        public Quad2 clone()
        {
            return new Quad2(this);
        }

        /**
         * Equals Method override //So much easier to validate with Iterator ;]
         * @param Object obj
         * @return bool
         */
        public boolean equals ( Object obj )
        {
            if ( this == obj ) return true;

            if ((obj != null) && (getClass() == obj.getClass()))
            {
                    Quad2 quad = (Quad2) obj;

                    if(!this.tl.equals(quad.tl) || !this.tr.equals(quad.tr) || !this.bl.equals(quad.bl) || !this.br.equals(quad.br))
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

    public static class Quad3
    {
        public Vertex3F tl;
        public Vertex3F tr;
        public Vertex3F bl;
        public Vertex3F br;
        
        public Quad3()
        {
            this.tl = new Vertex3F();
            this.tr = new Vertex3F();
            this.bl = new Vertex3F();
            this.br = new Vertex3F();
        }
        
        public Quad3(Vertex3F tl, Vertex3F tr, Vertex3F bl, Vertex3F br)
        {
            this.tl = tl;
            this.tr = tr;
            this.bl = bl;
            this.br = br;
        }

        public Quad3(Quad3 original)
        {
            if(original == null) throw new NullPointerException();

            this.tl = original.tl.clone();
            this.tr = original.tr.clone();
            this.bl = original.bl.clone();
            this.br = original.br.clone();
        }

        public Quad3 clone()
        {
            return new Quad3(this);
        }

        /**
         * Equals Method override //So much easier to validate with Iterator ;]
         * @param Object obj
         * @return bool
         */
        public boolean equals ( Object obj )
        {
            if ( this == obj ) return true;

            if ((obj != null) && (getClass() == obj.getClass()))
            {
                    Quad3 quad = (Quad3) obj;

                    if(!this.tl.equals(quad.tl) || !this.tr.equals(quad.tr) || !this.bl.equals(quad.bl) || !this.br.equals(quad.br))
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

    //Grid Class
    public static class GridSize
    {
        public int x;
        public int y;
        
        public GridSize()
        {
            this.x = 0;
            this.y = 0;
        }
        
        public GridSize(int x, int y)
        {
            this.x = x;
            this.y = y;
        }

        public GridSize(GridSize original)
        {
            if(original == null) throw new NullPointerException();

            this.x = original.x;
            this.y = original.y;
        }

        public GridSize clone()
        {
            return new GridSize(this);
        }

        /**
         * Equals Method override //So much easier to validate with Iterator ;]
         * @param Object obj
         * @return bool
         */
        public boolean equals ( Object obj )
        {
            if ( this == obj ) return true;

            if ((obj != null) && (getClass() == obj.getClass()))
            {
                    GridSize grid = (GridSize) obj;

                    if(this.x != grid.x || this.y != grid.y)
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

    //Points with Color and Texture
    public static class VertexTexture2F 
    {
        public Vertex2F vertice;
        public Color4F color;
        public Vertex2F coords;
        
        public VertexTexture2F()
        {
            this.vertice = new Vertex2F();
            this.color = new Color4F();
            this.coords = new Vertex2F();
        }
        
        public VertexTexture2F(Vertex2F vertice, Color4F color, Vertex2F coords)
        {
            this.vertice = vertice;
            this.color = color;
            this.coords = coords;
        }

        public VertexTexture2F(VertexTexture2F original)
        {
            if(original == null) throw new NullPointerException();

            this.vertice = original.vertice.clone();
            this.color = original.color.clone();
            this.coords = original.coords.clone();
        }

        public VertexTexture2F clone()
        {
            return new VertexTexture2F(this);
        }

/**
         * Equals Method override //So much easier to validate with Iterator ;]
         * @param Object obj
         * @return bool
         */
        public boolean equals ( Object obj )
        {
            if ( this == obj ) return true;

            if ((obj != null) && (getClass() == obj.getClass()))
            {
                    VertexTexture2F vtex = (VertexTexture2F) obj;

                    if(!this.color.equals(vtex.color) || !this.coords.equals(vtex.coords) || !this.vertice.equals(vtex.vertice))
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

    public static class VertexTexture3F 
    {
        public Vertex3F vertice;
        public Color4F color;
        public Vertex2F coords;
        
        public VertexTexture3F()
        {
            this.vertice = new Vertex3F();
            this.color = new Color4F();
            this.coords = new Vertex2F();
        }
        
        public VertexTexture3F(Vertex3F vertice, Color4F color, Vertex2F coords)
        {
            this.vertice = vertice;
            this.color = color;
            this.coords = coords;
        }

        public VertexTexture3F(VertexTexture3F original)
        {
            if(original == null) throw new NullPointerException();

            this.vertice = original.vertice.clone();
            this.color = original.color.clone();
            this.coords = original.coords.clone();
        }

        public VertexTexture3F clone()
        {
            return new VertexTexture3F(this);
        }

/**
         * Equals Method override //So much easier to validate with Iterator ;]
         * @param Object obj
         * @return bool
         */
        public boolean equals ( Object obj )
        {
            if ( this == obj ) return true;

            if ((obj != null) && (getClass() == obj.getClass()))
            {
                    VertexTexture3F vtex = (VertexTexture3F) obj;

                    if(!this.color.equals(vtex.color) || !this.coords.equals(vtex.coords) || !this.vertice.equals(vtex.vertice))
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

    //Points with Color and Texture
    public static class VertexTexture2B 
    {
        public Vertex2F vertice;
        public Color4B color;
        public Vertex2F coords;
        
        public VertexTexture2B()
        {
            this.vertice = new Vertex2F();
            this.color = new Color4B();
            this.coords = new Vertex2F();
        }
        
        public VertexTexture2B(Vertex2F vertice, Color4B color, Vertex2F coords)
        {
            this.vertice = vertice;
            this.color = color;
            this.coords = coords;
        }

        public VertexTexture2B(VertexTexture2B original)
        {
            if(original == null) throw new NullPointerException();

            this.vertice = original.vertice.clone();
            this.color = original.color.clone();
            this.coords = original.coords.clone();
        }

        public VertexTexture2B clone()
        {
            return new VertexTexture2B(this);
        }

        /**
         * Equals Method override //So much easier to validate with Iterator ;]
         * @param Object obj
         * @return bool
         */
        public boolean equals ( Object obj )
        {
            if ( this == obj ) return true;

            if ((obj != null) && (getClass() == obj.getClass()))
            {
                    VertexTexture2B vtex = (VertexTexture2B) obj;

                    if(!this.color.equals(vtex.color) || !this.coords.equals(vtex.coords) || !this.vertice.equals(vtex.vertice))
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

    //Quads with Color and Texture

    public static class QuadTexture3F
    {
        public VertexTexture3F tl;
        public VertexTexture3F tr;
        public VertexTexture3F bl;
        public VertexTexture3F br;

        public QuadTexture3F()
        {
            this.tl = new VertexTexture3F();
            this.tr = new VertexTexture3F();
            this.bl = new VertexTexture3F();
            this.br = new VertexTexture3F();
        }

        public QuadTexture3F(VertexTexture3F tl, VertexTexture3F tr, VertexTexture3F bl, VertexTexture3F br)
        {
            this.tl = tl;
            this.tr = tr;
            this.bl = bl;
            this.br = br;
        }

        public QuadTexture3F(QuadTexture3F original)
        {
            if(original == null) throw new NullPointerException();

            this.tl = original.tl.clone();
            this.tr = original.tr.clone();
            this.bl = original.bl.clone();
            this.br = original.br.clone();
        }

        public QuadTexture3F clone()
        {
            return new QuadTexture3F(this);
        }

/**
         * Equals Method override //So much easier to validate with Iterator ;]
         * @param Object obj
         * @return bool
         */
        public boolean equals ( Object obj )
        {
            if ( this == obj ) return true;

            if ((obj != null) && (getClass() == obj.getClass()))
            {
                    QuadTexture3F quad = (QuadTexture3F) obj;

                    if(!this.tl.equals(quad.tl) || !this.tr.equals(quad.tr) || !this.bl.equals(quad.bl) || !this.br.equals(quad.br))
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

    public static class QuadTexture2F
    {
        public VertexTexture2F tl;
        public VertexTexture2F tr;
        public VertexTexture2F bl;
        public VertexTexture2F br;

        public QuadTexture2F()
        {
            this.tl = new VertexTexture2F();
            this.tr = new VertexTexture2F();
            this.bl = new VertexTexture2F();
            this.br = new VertexTexture2F();
        }

        public QuadTexture2F(VertexTexture2F tl, VertexTexture2F tr, VertexTexture2F bl, VertexTexture2F br)
        {
            this.tl = tl;
            this.tr = tr;
            this.bl = bl;
            this.br = br;
        }

        public QuadTexture2F(QuadTexture2F original)
        {
            if(original == null) throw new NullPointerException();

            this.tl = original.tl.clone();
            this.tr = original.tr.clone();
            this.bl = original.bl.clone();
            this.br = original.br.clone();
        }

        public QuadTexture2F clone()
        {
            return new QuadTexture2F(this);
        }

/**
         * Equals Method override //So much easier to validate with Iterator ;]
         * @param Object obj
         * @return bool
         */
        public boolean equals ( Object obj )
        {
            if ( this == obj ) return true;

            if ((obj != null) && (getClass() == obj.getClass()))
            {
                    QuadTexture2F quad = (QuadTexture2F) obj;

                    if(!this.tl.equals(quad.tl) || !this.tr.equals(quad.tr) || !this.bl.equals(quad.bl) || !this.br.equals(quad.br))
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

    public static class QuadTexture2B
    {
        public VertexTexture2B tl;
        public VertexTexture2B tr;
        public VertexTexture2B bl;
        public VertexTexture2B br;

        public QuadTexture2B()
        {
            this.tl = new VertexTexture2B();
            this.tr = new VertexTexture2B();
            this.bl = new VertexTexture2B();
            this.br = new VertexTexture2B();
        }

        public QuadTexture2B(VertexTexture2B tl, VertexTexture2B tr, VertexTexture2B bl, VertexTexture2B br)
        {
            this.tl = tl;
            this.tr = tr;
            this.bl = bl;
            this.br = br;
        }

        public QuadTexture2B(QuadTexture2B original)
        {
            if(original == null) throw new NullPointerException();

            this.tl = original.tl.clone();
            this.tr = original.tr.clone();
            this.bl = original.bl.clone();
            this.br = original.br.clone();
        }

        public QuadTexture2B clone()
        {
            return new QuadTexture2B(this);
        }

        /**
         * Equals Method override //So much easier to validate with Iterator ;]
         * @param Object obj
         * @return bool
         */
        public boolean equals ( Object obj )
        {
            if ( this == obj ) return true;

            if ((obj != null) && (getClass() == obj.getClass()))
            {
                    QuadTexture2B quad = (QuadTexture2B) obj;

                    if(!this.tl.equals(quad.tl) || !this.tr.equals(quad.tr) || !this.bl.equals(quad.bl) || !this.br.equals(quad.br))
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

    public static class CCBlend 
    {
        public int source;
        public int destination;
        
        public CCBlend()
        {
            this.source = GL11.GL_SRC_ALPHA;
            this.destination = GL11.GL_ONE_MINUS_SRC_ALPHA;
        }
        
        public CCBlend(int source, int destination)
        {
            this.source = source;
            this.destination = destination;
        }
        
        public CCBlend(CCBlend original)
        {
            if(original == null) throw new NullPointerException();
            
            this.source = original.source;
            this.destination = original.destination;
        }
        
        public CCBlend clone()
        {
            return new CCBlend(this);
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
                    CCBlend blend = (CCBlend) obj;

                    if(this.destination != blend.destination || this.source != blend.source)
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

    public static class CCGridSize
    {
        public int x;
        public int y;
        
        public CCGridSize()
        {
            this(0,0);
        }
        
        public CCGridSize(int x, int y)
        {
            this.x = x;
            this.y = y;
        }

        public CCGridSize(CCGridSize original)
        {
            if(original == null) throw new NullPointerException();

            this.x = original.x;
            this.y = original.y;
        }

        public CCGridSize clone()
        {
            return new CCGridSize(this);
        }
    }

    private CCTypes()
    {
        //Do Nothing
    }
}
