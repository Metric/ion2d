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
import java.nio.FloatBuffer;

public class CCMath {
    private CCMath() {
        //Do Nothing
    }

    public static double degreesToRadians(float angle)
    {
        return ((angle / 180.0f) * Math.PI);
    }

    /**
     * Converts an 3x3 AffineTransform into a 4x4 openGL FloatBuffer Matrix
     * @param AffineTransform t
     * @return FloatBuffer
     */
    public static FloatBuffer AffineToGL(AffineTransform t)
    {
        FloatBuffer buffer = FloatBuffer.allocate(16);

        double[] matrix = new double[6];

        t.getMatrix(matrix);

        buffer.put(0, (float)matrix[0]);
        buffer.put(1, (float)matrix[1]);
        buffer.put(4, (float)matrix[2]);
        buffer.put(5, (float)matrix[3]);
        buffer.put(12, (float)matrix[4]);
        buffer.put(13, (float)matrix[5]);

        buffer.put(2, 0.0f); buffer.put(3, 0.0f); buffer.put(6, 0.0f);
        buffer.put(7, 0.0f); buffer.put(8, 0.0f); buffer.put(9, 0.0f);
        buffer.put(11, 0.0f); buffer.put(14, 0.0f);

        buffer.put(10, 1.0f); buffer.put(15, 1.0f);
        buffer.rewind();

        return buffer;
    }


    /**
     * Takes a 4x4 Matrix from OpenGl and translates to a 3x3 affine Matrix
     * @param float[] glMatrix
     * @return AffineTransform
     */
    public static AffineTransform GLToAffine(float[] glMatrix)
    {
        try
        {
            float[] affine = new float[6];
            affine[0] = glMatrix[0];
            affine[1] = glMatrix[1];
            affine[2] = glMatrix[4];
            affine[3] = glMatrix[5];
            affine[4] = glMatrix[12];
            affine[5] = glMatrix[13];

            AffineTransform newTransform = new AffineTransform(affine);
            return newTransform;
        } catch (Exception e)
        {
            AffineTransform identity = new AffineTransform();
            identity.setToIdentity();

            return identity;
        }
    }

    /**
     * Subtract the two points
     * @param Point2D point1
     * @param Point2D point2
     * @return Point2D
     */
    public static Point2D pointSubtract(Point2D point1, Point2D point2)
    {
        return new Point2D.Float((float)point1.getX() - (float)point2.getX(), (float)point1.getY() - (float)point2.getY());
    }

    /**
     * Add the two points
     * @param Point2D point1
     * @param Point2D point2
     * @return Point2D
     */
    public static Point2D pointAdd(Point2D point1, Point2D point2)
    {
        return new Point2D.Float((float)point1.getX() + (float)point2.getX(), (float)point1.getY() + (float)point2.getY());
    }

    /**
     * Multiply the point by the specified amount
     * @param Point2D point1
     * @param float amount
     * @return Point2D
     */
    public static Point2D pointMultiply(Point2D point1, float amount)
    {
        return new Point2D.Float((float)point1.getX() * amount, (float)point1.getY() * amount);
    }

    /**
     * Gets the center point of the two points
     * @param Point2D point1
     * @param Point2D point2
     * @return Point2D
     */
    public static Point2D pointCenter(Point2D point1, Point2D point2)
    {
        return pointMultiply(pointAdd(point1, point2), 0.5f);
    }

    /**
     * Multiplies two points together
     */
    public static Point2D pointDot(Point2D point1, Point2D point2)
    {
        return new Point2D.Float((float)point1.getX() * (float)point2.getX(), (float)point1.getY() * (float)point2.getY());
    }

    /**
     * Clamps the range of the values
     * @param float x
     * @param float y
     * @param float z
     * @return float
     */
    public static float clamp(float x, float y, float z)
    {
        return Math.min(Math.max(x, y), z);
    }

    /**
     * Determines if a number is a power of 2
     */
    public static boolean isPowerOfTwo(int number)
    {
         double logNbase2 =  Math.log(number)/Math.log(2);
	 int logNbase2Integer = (int) (Math.floor(logNbase2));

	 if(logNbase2-logNbase2Integer==0)
            return true;
	 else
            return false;
    }

    public static boolean isPowerOfTwo(float number)
    {
        double logNbase2 = Math.log(number)/Math.log(2);
        float logNbase2Float = (float) (Math.floor(logNbase2));

        if(logNbase2 - logNbase2Float == 0)
            return true;
        else
            return false;
    }

    /**
     * Checks to see if something is the power of 2
     * @param number
     * @return
     */
    public static boolean isPowerOfTwo(double number)
    {
        double logNbase2 = Math.log(number)/Math.log(2);
        double logNbase2Float = Math.floor(logNbase2);

        if(logNbase2 - logNbase2Float == 0)
            return true;
        else
            return false;
    }

    public static int getPowerOfTwo(int number)
    {
        int pow = 2;

        while(pow < number)
        {
            pow *= 2;
        }

        return pow;
    }

    public static int getPowerOfTwo(float number)
    {
        int pow = 2;

        while(pow < number)
        {
            pow *= 2;
        }

        return pow;
    }

    public static int getPowerOfTwo(double number)
    {
        int pow = 2;

        while(pow < number)
        {
            pow *= 2;
        }

        return pow;
    }
}
