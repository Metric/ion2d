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

import org.lwjgl.Sys;
import org.lwjgl.opengl.*;

/**
 * Creates a GLDisplay for a frame
 * Or creates the frame and GLDisplay
 */
public class GLDisplay
{
    private static final int DEFAULT_WIDTH = 640;
    private static final int DEFAULT_HEIGHT = 480;

    private static GLDisplay instance;

    private static final int IGNORE = -1;

    private boolean fullscreen;

    private static int FRAMERATE = 60;

    private GLDisplay()
    {

    }

    /**
     * Secondary Constructor for GLDisplay
     * @param String title
     * @param int width
     * @param int height
     * @param boolean fullscreen
     * @param GLCapabilities capability
     */
    private GLDisplay(String title, int width, int height, boolean fullscreen)
    {
        try
        {
            Display.setTitle(title);
            Display.setFullscreen(fullscreen);
            Display.setVSyncEnabled(true);
            Display.setDisplayMode(findDisplayMode(Display.getAvailableDisplayModes(), width, height, 24, 60));
            Display.create();

        } catch (Exception e)
        {
            Sys.alert("Display Error", "Failed to Create Display");
        }

        this.fullscreen = fullscreen;
    }

    /**
     * Creates a GLDisplay with the specified title
     * Defaults to 640x480 window
     * @param String title
     * @return
     */
    public static void createGLDisplay(String title)
    {
        if(instance == null)
        {
            instance = new GLDisplay(title, DEFAULT_WIDTH, DEFAULT_HEIGHT, false);
        }
    }

    /**
     * Creates a GLDisplay with the specified title, width, height, and fullscreen option
     * @param String title
     * @param int width
     * @param int height
     * @param boolean fullscreen
     */
    public static void createGLDisplay(String title, int width, int height, boolean fullscreen)
    {
        if(instance == null)
        {
            instance = new GLDisplay(title, width, height, fullscreen);
        }
    }

    /**
     * Updates the display
     */
    public static void update()
    {
        Display.update();

        if(Display.isActive())
        {
            Display.sync(FRAMERATE);
        }
        else
        {
            try
            {
                Thread.sleep(100);
            } catch (Exception e)
            {
            }
        }
    }


    /**
     * Finds the requested display mode based on the specified parameters
     * @param DisplayMode[] displayModes
     * @param int width
     * @param int height
     * @param int depth
     * @param int refreshRate
     * @return DisplayMode
     */
    private static DisplayMode findDisplayMode(DisplayMode[] displayModes, int width, int height, int depth, int refreshRate)
    {
        DisplayMode displayMode = findDisplayModeInternal(displayModes, width, height, depth, refreshRate);

        if(displayMode == null)
            displayMode = findDisplayModeInternal(displayModes, width, height, IGNORE, IGNORE);

        if(displayMode == null)
            displayMode = findDisplayModeInternal(displayModes, width, IGNORE, IGNORE, IGNORE);

        if(displayMode == null)
            displayMode = findDisplayModeInternal(displayModes, IGNORE, IGNORE, IGNORE, IGNORE);

        return displayMode;
    }

    /**
     * Finds the final display mode to use out of the list of modes
     * @param DisplayMode[] displayModes
     * @param int width
     * @param int height
     * @param int depth
     * @param int refreshRate
     * @return
     */
    private static DisplayMode findDisplayModeInternal(DisplayMode[] displayModes, int width, int height, int depth, int refreshRate)
    {
        DisplayMode displayModeToUse = null;

        for ( int i = 0; i < displayModes.length; i++ )
        {
            DisplayMode displayMode = displayModes[i];
            if ((width == IGNORE || displayMode.getWidth() == width) &&
                    (height == IGNORE || displayMode.getHeight() == height) &&
                    (refreshRate == IGNORE || displayMode.getFrequency() == refreshRate) &&
                    (depth == IGNORE || displayMode.getBitsPerPixel() == depth))
                displayModeToUse = displayMode;
        }

        return displayModeToUse;
    }

    public static String getTitle()
    {
        return Display.getTitle();
    }

    /**
     * Fullscreen setter
     * Automatically stops the openGL and restarts it
     */
    public static void setFullScreen(boolean fullscreen)
    {
        try
        {
            instance.fullscreen = fullscreen;
            Display.setFullscreen(instance.fullscreen);
        } catch (Exception e)
        {
            Sys.alert("Display Switch Error", "Failed to Switch Fullscreen Mode");
        }
    }

    public static void destroy()
    {
        Display.destroy();
    }

    //Setters
    public static void setFrameRate(int rate)
    {
        FRAMERATE = rate;
    }

    //Getters
    public static int getWidth()
    {
        DisplayMode mode = Display.getDisplayMode();

        return mode.getWidth();
    }

    public static int getHeight()
    {
        DisplayMode mode = Display.getDisplayMode();

        return mode.getHeight();
    }
}