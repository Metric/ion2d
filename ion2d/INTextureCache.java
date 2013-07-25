/**
 * ion2d
 *
 * Copyright (C) 2010 Vantage Technic
 *
 * instance program is free software; you can redistribute it and/or modify
 * it under the terms of the 'ion2d' license.
 *
 * You will find a copy of instance license within the ion2d
 * distribution inside the "LICENSE" file.
 */

package ion2d;

import java.util.*;
import java.net.*;
import java.io.*;
import ion2d.support.*;
import java.lang.ref.WeakReference;
import org.lwjgl.opengl.*;

public class INTextureCache
{
    private Hashtable<String, WeakReference<INTexture2D>> textures;

    private static INTextureCache instance;

    private INTextureCache()
    {
        this.textures = new Hashtable<String, WeakReference<INTexture2D>>(10,10);
    }

    /**
     * Checks to see if the singleton is created if not
     * then the singleton is created
     */
    protected static void checkForInstance()
    {
        if(instance  == null)
        {
            instance = new INTextureCache();
        }
    }

    /**
     * Loads the image file into texture based on the path
     * @param String filePath
     * @return INTexture2D
     */
    public static INTexture2D addImage(String filePath)
    {
        //Check to make sure an instance is created for the singleton
        checkForInstance();

        INTexture2D tex = null;

        removeUnusedTextures();

        if(instance.textures.containsKey(filePath))
        {
            tex = instance.textures.get(filePath).get();
            return tex;
        }
        else
        {
            try
            {
                tex = new INTexture2D(filePath);
                instance.textures.put(filePath, new WeakReference<INTexture2D>(tex));
                
                return tex;

            } catch (Exception e)
            {
                System.out.println(e);
                e.printStackTrace();
                return null;
            }
        }
    }

    /**
     * Loads the image file into texture based on the url path
     * @param String filePath
     * @return INTexture2D
     */
    public static INTexture2D addImage(URL filePath, String key)
    {
        //Check to make sure an instance is created for the singleton
        checkForInstance();

        INTexture2D tex = null;

        removeUnusedTextures();

        if(instance.textures.containsKey(key))
        {
            tex = instance.textures.get(key).get();
            return tex;
        }
        else
        {
            try
            {
                tex = new INTexture2D(filePath);
                instance.textures.put(key, new WeakReference<INTexture2D>(tex));

                return tex;

            } catch (Exception e)
            {
                System.out.println(e.getMessage());
                e.printStackTrace();
                return null;
            }
        }
    }

    /**
     * Loads the image into a texture based on the INImage
     * @param String filePath
     * @return INTexture2D
     */
    public static INTexture2D addImage(INImage image, String key)
    {
        //Check to make sure an instance is created for the singleton
        checkForInstance();

         INTexture2D tex = null;

         removeUnusedTextures();

        if(instance.textures.containsKey(key))
        {
            tex = instance.textures.get(key).get();
            return tex;
        }
        else
        {
            try
            {
                tex = new INTexture2D(image);
                instance.textures.put(key, new WeakReference<INTexture2D>(tex));

                return tex;

            } catch (Exception e)
            {
                System.out.println(e.getMessage());
                e.printStackTrace();
                return null;
            }
        }
    }

    /**
     * Loads the image file into a texture
     * @param String filePath
     * @return INTexture2D
     */
    public static INTexture2D addImage(File imageFile, String key)
    {
        //Check to make sure an instance is created for the singleton
        checkForInstance();

        INTexture2D tex = null;

        removeUnusedTextures();

        if(instance.textures.containsKey(key))
        {
            tex = instance.textures.get(key).get();
            return tex;
        }
        else
        {
            try
            {
                tex = new INTexture2D(imageFile);
                instance.textures.put(key, new WeakReference<INTexture2D>(tex));

                return tex;

            } catch (Exception e)
            {
                System.out.println(e.getMessage());
                e.printStackTrace();
                return null;
            }
        }
    }

    /**
     * Removes all the textures in the cache
     */
    public static void removeAllTextures()
    {
        //Check to make sure an instance is created for the singleton
        checkForInstance();

        instance.textures.clear();
    }

    /**
     * Removes only unused textures in the cache
     */
    public static void removeUnusedTextures()
    {
        //Check to make sure an instance is created for the singleton
        checkForInstance();

        Iterator textureIterator = instance.textures.keySet().iterator();

        while(textureIterator.hasNext())
        {
            String key = (String)textureIterator.next();

            if(instance.textures.get(key).get() == null)
            {
                textureIterator.remove();
            }
        }
    }

    /**
     * Removes the specified texture from the cache
     * @param INTexture2D texture
     */
    public static void removeTexture(INTexture2D texture)
    {
        //Check to make sure an instance is created for the singleton
        checkForInstance();

        Iterator textureIterator = instance.textures.keySet().iterator();

        FindTexture:
        while(textureIterator.hasNext())
        {
            String key = (String)textureIterator.next();

            if(instance.textures.get(key).get().equals(texture))
            {
                instance.textures.remove(key);

                break FindTexture;
            }
        }
    }

    /**
     * Removes a texture from the cache based on its key
     * @param String key
     */
    public static void removeTexture(String key)
    {
        //Check to make sure an instance is created for the singleton
        checkForInstance();

        if(instance.textures.containsKey(key))
        {
            instance.textures.remove(key);
        }
    }

    /**
     * Gets a texture from the cache based on its key
     * @param String key
     * @return INTexture2D
     */
    public static INTexture2D getTexture(String key)
    {
        //Check to make sure an instance is created for the singleton
        checkForInstance();


        if(instance.textures.containsKey(key))
        {
            return instance.textures.get(key).get();
        }
        else
        {
            return null;
        }
    }

    /**
     * Finalize Override
     */
    public void finalize()
    {
        removeAllTextures();
    }
}
