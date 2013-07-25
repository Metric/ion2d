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

package ion2d.support;

import java.awt.geom.*;
import java.awt.Color;
import org.lwjgl.opengl.*;

/**
 * Holds Various Class Definitions for Different Types of Structures
 */
public class INTypes
{
    public enum HonorParentTransform
    {
        HonorParentTransformTranslate,
        HonorParentTransformRotate,
        HonorParentTransformScale,
        HonorParentTransformAll
    }

    /**
     * Currently not used, because all images are converted to either
     * RGB or RGBA
     */
    public enum PixelFormat {
        PixelFormatRGB565, 
        PixelFormatRGBA8888,
        PixelFormatAutomatic,
        PixelFormatA8,
        PixelFormatRGBA4444,
        PixelFormatRGB5A1
    }

    public enum ProjectionFormat {
        DirectorProjection3D,
        DirectorProjection2D        
    }

    public enum DepthBufferFormat {
        DepthBufferNone,
        DepthBuffer16,
        DepthBuffer24
    }

    public static float FLT_EPSILON = 1.19209290e-07f;

    /**
     * Color struct for RGB
     * Only takes integers
     */
    public static class Color3B
    {
        private int r;
        private int g;
        private int b;

        /**
         * Default constructor
         */
        public Color3B()
        {
            this.r = 0;
            this.g = 0;
            this.b = 0;
        }

        /**
         * Secondary constructor
         * @param int red
         * @param int green
         * @param int blue
         */
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

        /**
         * Copy constructor
         * @param Color3B original
         */
        public Color3B(Color3B original)
        {
            if(original == null) throw new NullPointerException();

            this.r = original.r;
            this.g = original.g;
            this.b = original.b;
        }

        /**
         * Clone override
         * @return Color3B
         */
        public Color3B clone()
        {
            return new Color3B(this);
        }

        /**
         * Gets the red value
         * @return int
         */
        public int getRed() { return this.r; }

        /**
         * Gets the green value
         * @return int
         */
        public int getGreen() { return this.g; }

        /**
         * Gets the blue value
         * @return int
         */
        public int getBlue() { return this.b; }

        /**
         * Sets the red value
         * @param int red
         */
        public void setRed(int red)
        {
            if(red > 255) red = 255;
            if(red < 0 ) red = 0;

            this.r = red;
        }

        /**
         * Sets the green value
         * @param int green
         */
        public void setGreen(int green)
        {
            if(green > 255) green = 255;
            if(green < 0) green = 0;

            this.g = green;
        }

        /**
         * Sets the blue value
         * @param int blue
         */
        public void setBlue(int blue)
        {
            if(blue > 255) blue = 255;
            if(blue < 0) blue = 0;

            this.b = blue;
        }

        /**
         * Returns a regular java color struct
         * @return Color
         */
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

    /**
     * Color struct for RGBA
     * Only takes integer values
     */
    public static class Color4B
    {
        private int r;
        private int g;
        private int b;
        private int a;

        /**
         * Default constructor
         */
        public Color4B()
        {
            this.r = 0;
            this.g = 0;
            this.b = 0;
            this.a = 255;
        }

        /**
         * Secondary constructor
         * @param int red
         * @param int green
         * @param int blue
         * @param int alpha
         */
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

        /**
         * Copy constructor
         * @param Color4B original
         */
        public Color4B(Color4B original)
        {
            if(original == null) throw new NullPointerException();

            this.r = original.r;
            this.g = original.g;
            this.b = original.b;
            this.a = original.a;
        }

        /**
         * Clone override
         * @return Color4B
         */
        public Color4B clone()
        {
            return new Color4B(this);
        }

        /**
         * Gets the red value
         * @return int
         */
        public int getRed() { return this.r; }

        /**
         * Gets the green value
         * @return int
         */
        public int getGreen() { return this.g; }

        /**
         * Gets the blue value
         * @return int
         */
        public int getBlue() { return this.b; }

        /**
         * Gets the alpha value
         * @return int
         */
        public int getAlpha() { return this.a; }

        /**
         * Sets the red value
         * @param int red
         */
        public void setRed(int red)
        {
            if(red > 255) red = 255;
            if(red < 0 ) red = 0;

            this.r = red;
        }

        /**
         * Sets the green value
         * @param int green
         */
        public void setGreen(int green)
        {
            if(green > 255) green = 255;
            if(green < 0) green = 0;

            this.g = green;
        }

        /**
         * Sets the blue value
         * @param int blue
         */
        public void setBlue(int blue)
        {
            if(blue > 255) blue = 255;
            if(blue < 0) blue = 0;

            this.b = blue;
        }

        /**
         * Sets the alpha value
         * @param int alpha
         */
        public void setAlpha(int alpha)
        {
            if(alpha > 255) alpha = 255;
            if(alpha < 0) alpha = 0;

            this.a = alpha;
        }

        /**
         * Returns a standard java Color object with RGBA
         * @return Color
         */
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

    /**
     * Color struct for RGBA in float values
     */
    public static class Color4F
    {
        private float r;
        private float g;
        private float b;
        private float a;

        /**
         * Default Constructor
         */
        public Color4F()
        {
            this.r = 0;
            this.g = 0;
            this.b = 0;
            this.a = 1.0f;
        }

        /**
         * Secondary Constructor
         * @param float red
         * @param float green
         * @param float blue
         * @param float alpha
         */
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

        /**
         * Copy constructor
         * @param Color4F original
         */
        public Color4F(Color4F original)
        {
            if(original == null) throw new NullPointerException();

            this.r = original.r;
            this.g = original.g;
            this.b = original.b;
            this.a = original.a;
        }

        /**
         * Clone Override
         * @return Color4F
         */
        public Color4F clone()
        {
            return new Color4F(this);
        }

        /**
         * Gets the red value
         * @return float
         */
        public float getRed() { return this.r; }

        /**
         * Gets the green value
         * @return float
         */
        public float getGreen() { return this.g; }

        /**
         * Gets the blue value
         * @return float
         */
        public float getBlue() { return this.b; }

        /**
         * Gets the alpha value
         * @return float
         */
        public float getAlpha() { return this.a; }

        /**
         * Sets the red value ranging from 0.0 to 1.0
         * @param float red
         */
        public void setRed(float red)
        {
            if(red > 1) red = 1.0f;
            if(red < 0 ) red = 0;

            this.r = red;
        }

        /**
         * Sets the green value ranging from 0.0 to 1.0
         * @param float green
         */
        public void setGreen(float green)
        {
            if(green > 1) green = 1.0f;
            if(green < 0) green = 0;

            this.g = green;
        }

        /**
         * Sets the blue value ranging from 0.0 to 1.0
         * @param float blue
         */
        public void setBlue(float blue)
        {
            if(blue > 1) blue = 1.0f;
            if(blue < 0) blue = 0;

            this.b = blue;
        }

        /**
         * Sets the alpha vlue ranging from 0.0 to 1.0
         * @param float alpha
         */
        public void setAlpha(float alpha)
        {
            if(alpha > 1) alpha = 1.0f;
            if(alpha < 0) alpha = 0;

            this.a = alpha;
        }

        /**
         * Converts to a standard Color object with RGBA
         * @return Color
         */
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

    /**
     * Various Predefined Colors
     */
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

    /**
     * X,Y,Z Point Structure with float values
     */
    public static class Vertex3F
    {
        public float x;
        public float y;
        public float z;

        /**
         * Default constructor
         */
        public Vertex3F()
        {
            this.x = 0.0f;
            this.y = 0.0f;
            this.z = 0.0f;
        }

        /**
         * Seconday constructor
         * @param Point2D point
         */
        public Vertex3F(Point2D point)
        {
            this.x = (float)point.getX();
            this.y = (float)point.getY();
            this.z = 0.0f;
        }

        /**
         * Third Constructor
         * @param float x
         * @param float y
         * @param float z
         */
        public Vertex3F(float x, float y, float z)
        {
            this.x = x;
            this.y = y;
            this.z = z;
        }

        /**
         * Copy Constructor
         * @param Vertex3F original
         */
        public Vertex3F(Vertex3F original)
        {
            if(original == null) throw new NullPointerException();

            this.x = original.x;
            this.y = original.y;
            this.z = original.z;
        }

        /**
         * Converts the Vertex3F to a Point2D Float object
         * The Z-value is not included in the Point2D object
         * @return Point2D
         */
        public Point2D toPoint2D()
        {
            return new Point2D.Float(this.x, this.y);
        }

        /**
         * Clone override
         * @return Vertex3F
         */
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

    /**
     * X,Y Point Struct with float values
     */
    public static class Vertex2F
    {
        public float x;
        public float y;

        /**
         * Default constructor
         */
        public Vertex2F()
        {
            this.x = 0.0f;
            this.y = 0.0f;
        }

        /**
         * Secondary Constructor
         * @param float x
         * @param float y
         */
        public Vertex2F(float x, float y)
        {
            this.x = x;
            this.y = y;
        }

        /**
         * Third Constructor
         * @param Point2D point
         */
        public Vertex2F(Point2D point)
        {
            this.x = (float)point.getX();
            this.y = (float)point.getY();
        }

        /**
         * Copy Constructor
         * @param Vertex2F original
         */
        public Vertex2F(Vertex2F original)
        {
            if(original == null) throw new NullPointerException();

            this.x = original.x;
            this.y = original.y;
        }

        /**
         * Convertes the Vertex2F to a Point2D Float object
         * @return Point2D
         */
        public Point2D toPoint2D()
        {
            return new Point2D.Float(this.x, this.y);
        }

        /**
         * Clone override
         * @return Vertex2F
         */
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

    /**
     * PointSprite Struct containing Vertex2F, Color4F and size
     */
    public static class PointSprite
    {
        public Vertex2F position;
        public Color4F color;
        public float size;

        /**
         * Default Constructor
         */
        public PointSprite()
        {
            this.position = new Vertex2F();
            this.color = new Color4F();
            this.size = 0.0f;
        }

        /**
         * Secondary Constructor
         * @param Vertex2F position
         * @param Color4F color
         * @param float size
         */
        public PointSprite(Vertex2F position, Color4F color, float size)
        {
            this.position = position;
            this.color = color;
            this.size = size;
        }

        /**
         * Copy Constructor
         * @param PointSprite original
         */
        public PointSprite(PointSprite original)
        {
            if(original == null) throw new NullPointerException();

            this.position = original.position.clone();
            this.color = original.color.clone();
            this.size = original.size;
        }

        /**
         * Clone Override
         * @return PointSprite
         */
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

    /**
     * Quad structure containing Vertex2F points for all four points
     */
    public static class Quad2 
    {
        public Vertex2F tl;
        public Vertex2F tr;
        public Vertex2F bl;
        public Vertex2F br;

        /**
         * Default Constructor
         */
        public Quad2()
        {
            this.tl = new Vertex2F();
            this.tr = new Vertex2F();
            this.bl = new Vertex2F();
            this.br = new Vertex2F();
        }

        /**
         * Secondary Constructor
         * @param Vertex2F tl
         * @param Vertex2F tr
         * @param Vertex2F bl
         * @param Vertex2F br
         */
        public Quad2(Vertex2F tl, Vertex2F tr, Vertex2F bl, Vertex2F br)
        {
            this.tl = tl;
            this.tr = tr;
            this.bl = bl;
            this.br = br;
        }

        /**
         * Copy Constructor
         * @param Quad2 original
         */
        public Quad2(Quad2 original)
        {
            if(original == null) throw new NullPointerException();

            this.tl = original.tl.clone();
            this.tr = original.tr.clone();
            this.bl = original.bl.clone();
            this.br = original.br.clone();
        }

        /**
         * Clone Override
         * @return Quad2
         */
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

    /**
     * Quad containing Vertex3F for all four points
     */
    public static class Quad3
    {
        public Vertex3F tl;
        public Vertex3F tr;
        public Vertex3F bl;
        public Vertex3F br;

        /**
         * Default constructor
         */
        public Quad3()
        {
            this.tl = new Vertex3F();
            this.tr = new Vertex3F();
            this.bl = new Vertex3F();
            this.br = new Vertex3F();
        }

        /**
         * Secondary Constructor
         * @param Vertex3F tl
         * @param Vertex3F tr
         * @param Vertex3F bl
         * @param Vertex3F br
         */
        public Quad3(Vertex3F tl, Vertex3F tr, Vertex3F bl, Vertex3F br)
        {
            this.tl = tl;
            this.tr = tr;
            this.bl = bl;
            this.br = br;
        }

        /**
         * Copy Constructor
         * @param Quad3 original
         */
        public Quad3(Quad3 original)
        {
            if(original == null) throw new NullPointerException();

            this.tl = original.tl.clone();
            this.tr = original.tr.clone();
            this.bl = original.bl.clone();
            this.br = original.br.clone();
        }

        /**
         * Clone Override
         * @return Quad3
         */
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

    /**
     * Grid Size Struct
     */
    public static class GridSize
    {
        public int x;
        public int y;

        /**
         * Default Constructor
         */
        public GridSize()
        {
            this.x = 0;
            this.y = 0;
        }

        /**
         * Secondary Constructor
         * @param int x
         * @param int y
         */
        public GridSize(int x, int y)
        {
            this.x = x;
            this.y = y;
        }

        /**
         * Copy Constructor
         * @param GridSize original
         */
        public GridSize(GridSize original)
        {
            if(original == null) throw new NullPointerException();

            this.x = original.x;
            this.y = original.y;
        }

        /**
         * Clone Override
         * @return GridSize
         */
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

    /**
     * Vertex2F point with texture coords and color
     */
    public static class VertexTexture2F 
    {
        public Vertex2F vertice;
        public Color4F color;
        public Vertex2F coords;

        /**
         * Default constructor
         */
        public VertexTexture2F()
        {
            this.vertice = new Vertex2F();
            this.color = new Color4F();
            this.coords = new Vertex2F();
        }

        /**
         * Secondary Constructor
         * @param Vertex2F vertice
         * @param Color4F color
         * @param Vertex2F coords
         */
        public VertexTexture2F(Vertex2F vertice, Color4F color, Vertex2F coords)
        {
            this.vertice = vertice;
            this.color = color;
            this.coords = coords;
        }

        /**
         * Copy Constructor
         * @param VertexTexture2F original
         */
        public VertexTexture2F(VertexTexture2F original)
        {
            if(original == null) throw new NullPointerException();

            this.vertice = original.vertice.clone();
            this.color = original.color.clone();
            this.coords = original.coords.clone();
        }

        /**
         * Clone Override
         * @return VertexTexture2F
         */
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

    /**
     * Vertex3F with texture coords and color
     */
    public static class VertexTexture3F 
    {
        public Vertex3F vertice;
        public Color4F color;
        public Vertex2F coords;

        /**
         * Default constructor
         */
        public VertexTexture3F()
        {
            this.vertice = new Vertex3F();
            this.color = new Color4F();
            this.coords = new Vertex2F();
        }

        /**
         * Secondary Constructor
         * @param Vertex3F vertice
         * @param Color4F color
         * @param Vertex2F coords
         */
        public VertexTexture3F(Vertex3F vertice, Color4F color, Vertex2F coords)
        {
            this.vertice = vertice;
            this.color = color;
            this.coords = coords;
        }

        /**
         * Copy Constructor
         * @param VertexTexture3F original
         */
        public VertexTexture3F(VertexTexture3F original)
        {
            if(original == null) throw new NullPointerException();

            this.vertice = original.vertice.clone();
            this.color = original.color.clone();
            this.coords = original.coords.clone();
        }

        /**
         * Clone Override
         * @return VertexTexture3F
         */
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

    /**
     * Quad with Vertex3F, Texture, and color per point
     */
    public static class QuadTexture3F
    {
        public VertexTexture3F tl;
        public VertexTexture3F tr;
        public VertexTexture3F bl;
        public VertexTexture3F br;

        /**
         * Default constructor
         */
        public QuadTexture3F()
        {
            this.tl = new VertexTexture3F();
            this.tr = new VertexTexture3F();
            this.bl = new VertexTexture3F();
            this.br = new VertexTexture3F();
        }

        /**
         * Secondary Constructor
         * @param VertexTexture3F tl
         * @param VertexTexture3F tr
         * @param VertexTexture3F bl
         * @param VertexTexture3F br
         */
        public QuadTexture3F(VertexTexture3F tl, VertexTexture3F tr, VertexTexture3F bl, VertexTexture3F br)
        {
            this.tl = tl;
            this.tr = tr;
            this.bl = bl;
            this.br = br;
        }

        /**
         * Copy Constructor
         * @param QuadTexture3F original
         */
        public QuadTexture3F(QuadTexture3F original)
        {
            if(original == null) throw new NullPointerException();

            this.tl = original.tl.clone();
            this.tr = original.tr.clone();
            this.bl = original.bl.clone();
            this.br = original.br.clone();
        }

        /**
         * Clone Override
         * @return QuadTexture3F
         */
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

    /**
     * Quad with Vertex2F, Texture, and Color per point
     */
    public static class QuadTexture2F
    {
        public VertexTexture2F tl;
        public VertexTexture2F tr;
        public VertexTexture2F bl;
        public VertexTexture2F br;

        /**
         * Default constructor
         */
        public QuadTexture2F()
        {
            this.tl = new VertexTexture2F();
            this.tr = new VertexTexture2F();
            this.bl = new VertexTexture2F();
            this.br = new VertexTexture2F();
        }

        /**
         * Secondary Constructor
         * @param VertexTexture2F tl
         * @param VertexTexture2F tr
         * @param VertexTexture2F bl
         * @param VertexTexture2F br
         */
        public QuadTexture2F(VertexTexture2F tl, VertexTexture2F tr, VertexTexture2F bl, VertexTexture2F br)
        {
            this.tl = tl;
            this.tr = tr;
            this.bl = bl;
            this.br = br;
        }

        /**
         * Copy Constructor
         * @param QuadTexture2F original
         */
        public QuadTexture2F(QuadTexture2F original)
        {
            if(original == null) throw new NullPointerException();

            this.tl = original.tl.clone();
            this.tr = original.tr.clone();
            this.bl = original.bl.clone();
            this.br = original.br.clone();
        }

        /**
         * Clone Override
         * @return QuadTexture2F
         */
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

    public static final int BLEND_SRC = GL11.GL_SRC_ALPHA;
    public static final int BLEND_DST = GL11.GL_ONE_MINUS_SRC_ALPHA;

    /**
     * INBlend Struct
     * All Source and Destination are based on OpenGL Blending Constants
     */
    public static class INBlend
    {
        public int source;
        public int destination;

        /**
         * Default Constructor
         */
        public INBlend()
        {
            this.source = GL11.GL_SRC_ALPHA;
            this.destination = GL11.GL_ONE_MINUS_SRC_ALPHA;
        }

        /**
         * Secondary Constructor
         * @param int source
         * @param int destination
         */
        public INBlend(int source, int destination)
        {
            this.source = source;
            this.destination = destination;
        }

        /**
         * Copy Constructor
         * @param INBlend original
         */
        public INBlend(INBlend original)
        {
            if(original == null) throw new NullPointerException();
            
            this.source = original.source;
            this.destination = original.destination;
        }

        /**
         * Clone Override
         * @return INBlend
         */
        public INBlend clone()
        {
            return new INBlend(this);
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
                    INBlend blend = (INBlend) obj;

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

    private INTypes()
    {
        //Do Nothing
    }
}
