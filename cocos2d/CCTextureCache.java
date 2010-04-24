/**
 * cocos2d for Java
 *
 * Copyright (C) 2010 Vantage Technic
 *
 * instance program is free software; you can redistribute it and/or modify
 * it under the terms of the 'cocos2d for Java' license.
 *
 * You will find a copy of instance license within the cocos2d for Java
 * distribution inside the "LICENSE" file.
 */

package cocos2d;

import java.util.*;
import java.net.*;
import java.io.*;
import cocos2d.support.*;
import java.lang.ref.WeakReference;

public class CCTextureCache
{
    private Hashtable<String, WeakReference<CCTexture2D>> textures;

    private static CCTextureCache instance;

    private CCTextureCache()
    {
        this.textures = new Hashtable<String, WeakReference<CCTexture2D>>(10,10);
    }

    private static void checkForInstance()
    {
        if(instance  == null)
        {
            instance = new CCTextureCache();
        }
    }

    /**
     * Loads the image file into texture based on the path
     * @param String filePath
     * @return CCTexture2D
     */
    public static CCTexture2D addImage(String filePath)
    {
        //Check to make sure an instance is created for the singleton
        checkForInstance();

        CCTexture2D tex = null;

        if(instance.textures.containsKey(filePath))
        {
            tex = instance.textures.get(filePath).get();
            return tex;
        }
        else
        {
            try
            {
                tex = new CCTexture2D(filePath);
                instance.textures.put(filePath, new WeakReference<CCTexture2D>(tex));
                
                return tex;

            } catch (Exception e)
            {
                System.out.println(e.getMessage());
                return null;
            }
        }
    }

    /**
     * Loads the image file into texture based on the url path
     * @param String filePath
     * @return CCTexture2D
     */
    public static CCTexture2D addImage(URL filePath, String key)
    {
        //Check to make sure an instance is created for the singleton
        checkForInstance();

        CCTexture2D tex = null;

        if(instance.textures.containsKey(key))
        {
            tex = instance.textures.get(key).get();
            return tex;
        }
        else
        {
            try
            {
                tex = new CCTexture2D(filePath);
                instance.textures.put(key, new WeakReference<CCTexture2D>(tex));

                return tex;

            } catch (Exception e)
            {
                System.out.println(e.getMessage());
                return null;
            }
        }
    }

    /**
     * Loads the image into a texture based on the CCImage
     * @param String filePath
     * @return CCTexture2D
     */
    public static synchronized CCTexture2D addImage(CCImage image, String key)
    {
        //Check to make sure an instance is created for the singleton
        checkForInstance();

         CCTexture2D tex = null;

        if(instance.textures.containsKey(key))
        {
            tex = instance.textures.get(key).get();
            return tex;
        }
        else
        {
            try
            {
                tex = new CCTexture2D(image);
                instance.textures.put(key, new WeakReference<CCTexture2D>(tex));

                return tex;

            } catch (Exception e)
            {
                System.out.println(e.getMessage());
                return null;
            }
        }
    }

    /**
     * Loads the image file into a texture
     * @param String filePath
     * @return CCTexture2D
     */
    public static CCTexture2D addImage(File imageFile, String key)
    {
        //Check to make sure an instance is created for the singleton
        checkForInstance();

        CCTexture2D tex = null;

        if(instance.textures.containsKey(key))
        {
            tex = instance.textures.get(key).get();
            return tex;
        }
        else
        {
            try
            {
                tex = new CCTexture2D(imageFile);
                instance.textures.put(key, new WeakReference<CCTexture2D>(tex));

                return tex;

            } catch (Exception e)
            {
                System.out.println(e.getMessage());
                return null;
            }
        }
    }

    /**
     * Loads the image file into texture in another thread
     * @param String filePath
     * @param CCSelector callback
     * @return CCTexture2D
     */
    public synchronized static void addImageAsync(String filePath, CCSelector callback)
    {
        //Check to make sure an instance is created for the singleton
        checkForInstance();

        try
        {
            CCImage image = new CCImage(filePath);
            CCAsyncObject asyncObject = new CCAsyncObject(callback, image);
            Thread async = new Thread(new AsyncTexture(asyncObject, filePath));
            async.start();
        } catch (Exception e)
        {
            System.out.println(e);
        }
    }

    public synchronized static void addImageAsync(URL filePath, String key, CCSelector callback)
    {
        //Check to make sure an instance is created for the singleton
        checkForInstance();

        try
        {
            CCImage image = new CCImage(filePath);
            CCAsyncObject asyncObject = new CCAsyncObject(callback, image);
            Thread async = new Thread(new AsyncTexture(asyncObject, key));
            async.start();
        } catch (Exception e)
        {
            System.out.println(e);
        }
    }

    public synchronized static void addImageAsync(CCImage image, String key, CCSelector callback)
    {
        //Check to make sure an instance is created for the singleton
        checkForInstance();

        try
        {
            CCAsyncObject asyncObject = new CCAsyncObject(callback, image);
            Thread async = new Thread(new AsyncTexture(asyncObject, key));
            async.start();
        } catch (Exception e)
        {
            System.out.println(e);
        }
    }

    public synchronized static void addImageAsync(File imageFile, String key, CCSelector callback)
    {
        //Check to make sure an instance is created for the singleton
        checkForInstance();

        try
        {
            CCImage image = new CCImage(imageFile);
            CCAsyncObject asyncObject = new CCAsyncObject(callback, image);
            Thread async = new Thread(new AsyncTexture(asyncObject, key));
            async.start();
        } catch (Exception e)
        {
            System.out.println(e);
        }
    }

    public static void removeAllTextures()
    {
        //Check to make sure an instance is created for the singleton
        checkForInstance();

        instance.textures.clear();
    }

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
                instance.textures.remove(key);
            }
        }
    }

    public static void removeTexture(CCTexture2D texture)
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

    public static void removeTexture(String key)
    {
        //Check to make sure an instance is created for the singleton
        checkForInstance();

        if(instance.textures.containsKey(key))
        {
            instance.textures.remove(key);
        }
    }

    public static CCTexture2D getTexture(String key)
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

    private static class AsyncTexture implements Runnable
    {
        private CCAsyncObject texture;
        private String key;

        public AsyncTexture(CCAsyncObject asyncObject, String key)
        {
            synchronized(this)
            {
                this.texture = asyncObject;
                this.key = key;
            }
        }

        public void run()
        {
            synchronized(this)
            {
                CCTexture2D loadedTexture = addImage((CCImage)this.texture.data, this.key);

                try
                {
                    this.texture.selector.selector.invoke(this.texture.selector.target, loadedTexture);
                } catch (Exception e)
                {
                }
            }
        }
    }

    public void finalize()
    {
        removeAllTextures();
    }
}
