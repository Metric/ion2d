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

import cocos2d.support.GLDisplay;
import javax.media.opengl.GL;
import javax.media.opengl.GLAutoDrawable;
import javax.media.opengl.GLEventListener;
import javax.media.opengl.glu.GLU;
import cocos2d.support.*;
import cocos2d.support.CCTypes.*;
import java.util.Vector;

/**
 * The Director Singleton
 */
public class CCDirector implements GLEventListener
{
    private GLDisplay glDisplay;
    
    private double animationInterval;
    private double oldAnimationInterval;
    
    private PixelFormat pixelFormat;
    private DepthBufferFormat depthBufferFormat;
    
    private boolean displayFPS;
    private int frames;

    private float accumulation;
    private float frameRate;

    private boolean isPaused;

    private boolean sendCleanup;
    

    private static CCDirector instance;

    public enum DepthBufferFormat {
        CCDepthBufferNone,
        CCDepthBuffer16,
        CCDepthBuffer24
    }
    
    public enum DirectorProjection {
        CCDirectorProjection2D,
        CCDirectorProjection3D,
        CCDirectorProjectionCustom
    }

    private CCDirector()
    {

    }

    public CCDirector getInstance()
    {
        if(instance == null)
        {
            instance = new CCDirector();
        }

        return instance;
    }
}