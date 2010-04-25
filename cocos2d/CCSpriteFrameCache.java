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

import java.util.*;
import java.lang.ref.*;

public class CCSpriteFrameCache
{
    private Hashtable<String, WeakReference<CCSpriteFrame>> spriteFrames;

    private static CCSpriteFrameCache instance;

    private CCSpriteFrameCache()
    {
        this.spriteFrames = new Hashtable<String, WeakReference<CCSpriteFrame>>(10,10);
    }

    private static void checkForInstance()
    {
        if(instance  == null)
        {
            instance = new CCSpriteFrameCache();
        }
    }

    public static void addSpriteFrame(CCSpriteFrame frame, String name)
    {
        checkForInstance();

        instance.spriteFrames.put(name, new WeakReference(frame));
    }

    public static void removeAllSpriteFrames()
    {
        checkForInstance();

        instance.spriteFrames.clear();
    }

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

    public static void removeSpriteFrame(CCSpriteFrame frame)
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
    
    public static void removeSpriteFrame(String name)
    {
        checkForInstance();

        if(instance.spriteFrames.containsKey(name))
        {
            instance.spriteFrames.remove(name);
        }
    }

    public static CCSpriteFrame getSpriteFrame(String name)
    {
        checkForInstance();

        CCSpriteFrame frame = null;

        if(instance.spriteFrames.containsKey(name))
        {
            frame = instance.spriteFrames.get(name).get();
        }

        return frame;
    }
}
