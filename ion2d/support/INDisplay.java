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

import org.lwjgl.Sys;
import org.lwjgl.opengl.*;
import ion2d.support.INTypes.*;

/**
 * Creates a INDisplay for a frame
 * Or creates the frame and INDisplay
 */
public class INDisplay
{
    private static final int DEFAULT_WIDTH = 800;
    private static final int DEFAULT_HEIGHT = 600;

    private static INDisplay instance;

    private static final int IGNORE = -1;

    private boolean fullscreen;

    private static int FRAMERATE = 60;

    private INDisplay()
    {
        this("Default OpenGL Window", DEFAULT_WIDTH, DEFAULT_HEIGHT, 24, false);
    }

    /**
     * Secondary Constructor for INDisplay
     * @param String title
     * @param int width
     * @param int height
     * @param boolean fullscreen
     * @param GLCapabilities capability
     */
    private INDisplay(String title, int width, int height, int depth, boolean fullscreen)
    {
        try
        {
            Display.setTitle(title);
            Display.setFullscreen(fullscreen);
            Display.setVSyncEnabled(false);
            Display.setDisplayMode(findDisplayMode(Display.getAvailableDisplayModes(), width, height, depth, 60));
            Display.create();
        } catch (Exception e)
        {
            Sys.alert("Display Error", "Failed to Create Display");
            e.printStackTrace();
        }

        this.fullscreen = fullscreen;
    }

    /**
     * Creates a INDisplay with the specified title
     * Defaults to 640x480 window
     * @param String title
     * @return
     */
    public static void createGLDisplay(String title)
    {
        if(instance == null)
        {
            instance = new INDisplay(title, DEFAULT_WIDTH, DEFAULT_HEIGHT, 24, false);
        }
    }

    /**
     * Creates a INDisplay with the specified title, width, height, and fullscreen option
     * @param String title
     * @param int width
     * @param int height
     * @param boolean fullscreen
     */
    public static void createGLDisplay(String title, int width, int height, DepthBufferFormat depth, boolean fullscreen)
    {
        if(instance == null)
        {
            int realDepth = 0;
            switch(depth)
            {
                case DepthBufferNone:
                    realDepth = IGNORE;
                    break;
                case DepthBuffer16:
                    realDepth = 16;
                    break;
                case DepthBuffer24:
                    realDepth = 24;
                    break;
            }

            instance = new INDisplay(title, width, height, realDepth, fullscreen);
        }
    }

    /**
     * Updates the display
     */
    public static void update()
    {
        instance.updateDisplay();
    }

    private void updateDisplay()
    {
        Display.update();

        if(Display.isActive())
        {
            Display.sync(FRAMERATE);
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

    /**
     * Gets the title of the display
     * @return String
     */
    public static String getTitle()
    {
        return Display.getTitle();
    }

    /**
     * Fullscreen setter
     * @param boolean fullscreen
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
            e.printStackTrace();
        }
    }

    /**
     * Destroys the display
     */
    public static void destroy()
    {
        Display.destroy();
    }

    /**
     * Set the frame rate
     * @param int rate
     */
    public static void setFrameRate(int rate)
    {
        FRAMERATE = rate;
    }

    /**
     * Gets the display width
     * @return int
     */
    public static int getWidth()
    {
        DisplayMode mode = Display.getDisplayMode();

        return mode.getWidth();
    }

    /**
     * Gets the display height
     * @return int
     */
    public static int getHeight()
    {
        DisplayMode mode = Display.getDisplayMode();

        return mode.getHeight();
    }
}