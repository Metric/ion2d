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

import java.util.*;
import java.lang.ref.*;

public class INSpriteFrameCache
{
    private Hashtable<String, WeakReference<INSpriteFrame>> spriteFrames;

    private static INSpriteFrameCache instance;

    private INSpriteFrameCache()
    {
        this.spriteFrames = new Hashtable<String, WeakReference<INSpriteFrame>>(10,10);
    }

    /**
     * Checks to see if the singleton has been created
     * If not then the singleton is created
     */
    protected static void checkForInstance()
    {
        if(instance  == null)
        {
            instance = new INSpriteFrameCache();
        }
    }

    /**
     * Add a sprite frame to the cache
     * @param INSpriteFrame frame
     * @param String name
     */
    public static void addSpriteFrame(INSpriteFrame frame, String name)
    {
        checkForInstance();

        instance.spriteFrames.put(name, new WeakReference(frame));
    }

    /**
     * Remove all sprite frames from the cache
     */
    public static void removeAllSpriteFrames()
    {
        checkForInstance();

        instance.spriteFrames.clear();
    }

    /**
     * Remove only unused sprite frames from the cache
     */
    public static void removeUnusedSpriteFrames()
    {
        //Check to make sure an instance is created for the singleton
        checkForInstance();

        Iterator spriteFrameIterator = instance.spriteFrames.keySet().iterator();

        while(spriteFrameIterator.hasNext())
        {
            String key = (String)spriteFrameIterator.next();

            if(instance.spriteFrames.get(key) == null)
            {
                instance.spriteFrames.remove(key);
            }
        }
    }

    /**
     * Removes the specified sprite frame from the cache
     * @param INSpriteFrame frame
     */
    public static void removeSpriteFrame(INSpriteFrame frame)
    {
        //Check to make sure an instance is created for the singleton
        checkForInstance();

        Iterator spriteFrameIterator = instance.spriteFrames.keySet().iterator();

        FindTexture:
        while(spriteFrameIterator.hasNext())
        {
            String key = (String)spriteFrameIterator.next();

            if(instance.spriteFrames.get(key).get().equals(frame))
            {
                instance.spriteFrames.remove(key);

                break FindTexture;
            }
        }
    }

    /**
     * Removes the sprite frame based on the given name
     * @param String name
     */
    public static void removeSpriteFrame(String name)
    {
        checkForInstance();

        if(instance.spriteFrames.containsKey(name))
        {
            instance.spriteFrames.remove(name);
        }
    }

    /**
     * Gets the sprite frame based on name
     * @param String name
     * @return INSpriteFrame
     */
    public static INSpriteFrame getSpriteFrame(String name)
    {
        checkForInstance();

        INSpriteFrame frame = null;

        if(instance.spriteFrames.containsKey(name))
        {
            frame = instance.spriteFrames.get(name).get();
        }

        return frame;
    }

    /**
     * Finalize override
     */
    public void finalize()
    {
        removeAllSpriteFrames();
    }
}
