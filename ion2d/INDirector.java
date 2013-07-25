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

import ion2d.events.*;
import ion2d.support.*;
import ion2d.support.INTypes.*;
import java.util.Vector;
import org.lwjgl.opengl.*;
import org.lwjgl.util.glu.*;
import java.awt.*;

/**
 * The Director Singleton
 */
public class INDirector implements Runnable
{
    protected DepthBufferFormat depthBufferFormat;

    protected boolean displayFPS;
    protected int frames;

    protected float accumulation;
    protected float frameRate;
    protected long lastUpdate;
    protected float deltaTime;
    
    protected boolean nextDeltaTimeZero;

    protected boolean isPaused;
    protected boolean isRunning;
    
    protected static float DEFAULT_FPS = 60.0f;

    protected static INDirector instance;

    protected ProjectionFormat currentProjection;

    protected static final String VERSION = "0.1.0";

    protected INScene runningScene;
    protected INScene nextScene;

    protected Vector<INScene> sceneStack;

    protected INLabelMap fps;

    protected String resources;

    protected INDirector()
    {
        this.depthBufferFormat = DepthBufferFormat.DepthBufferNone;
        this.currentProjection = ProjectionFormat.DirectorProjection2D;
        this.displayFPS = false;
        this.isPaused = false;
        this.frames = 0;
        this.accumulation = 0;
        this.frameRate = 0;
        this.runningScene = null;
        this.nextScene = null;
        this.sceneStack = new Vector<INScene>(10);
        this.isRunning = false;
        this.nextDeltaTimeZero = true;

        this.resources = this.getClass().getClassLoader().getResource("ion2d/resources").toString();
    }

    public final void run()
    {
        while(this.isRunning)
        {
            if(!this.isPaused)
            {
                this.update();
            }
            else
            {
                try
                {
                    Thread.sleep(100);
                } catch (Exception e)
                {
                    System.out.println(e.getMessage());
                    e.printStackTrace();
                }
            }

            isClosing();
        }
    }

    public static void setFullScreen(boolean fullscreen)
    {
        if(Display.isCreated())
            INDisplay.setFullScreen(fullscreen);
    }

    protected void update()
    {
        if(Display.isActive())
        {

            calculateDeltaTime();

            if(!this.isPaused)
            {
                //INScheduler.tick(this.deltaTime);
            }

            GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);

            if(this.nextScene != null)
            {
                setNextScene();
            }

            //INKeyboard.update();
            //INMouse.update();

            GL11.glPushMatrix();

            this.enableDefaultGLStates();

            this.runningScene.visit();

            if(this.displayFPS)
            {
                this.renderFPS();
            }
            
            this.disableDefaultGLStates();

            GL11.glPopMatrix();

            System.gc();
        }

        INDisplay.update();
    }

    protected static void isClosing()
    {
        if(Display.isCloseRequested())
        {
            dispose();
        }
    }

    public static void checkInstance()
    {
        if(instance == null)
        {
            instance = new INDirector();
        }
    }

    protected void renderFPS()
    {
        if(this.fps == null)
        {
            try
            {   
                this.fps = new INLabelMap(getResourceFolder() + "/numbers.png", "0", 16,22,'.');
                this.fps.setAnchorPoint(new Vertex2F(0,0));
                this.fps.setPosition(new Vertex2F(0,0));
            } catch (Exception e)
            {
                System.out.println(e.getMessage());
                e.printStackTrace();
            }
        }

        this.frames++;
        this.accumulation += this.deltaTime;

        if(this.accumulation > 0.5f)
        {
            this.frameRate = (int)Math.round(this.frames / this.accumulation);
            this.frames = 0;
            this.accumulation = 0;

            try
            {
                this.fps.setText(Float.toString(this.frameRate));
            } catch (Exception e)
            {
                System.out.println(e.getMessage());
                e.printStackTrace();
            }
        }

        this.fps.visit();
    }

    public static Dimension getScreenSize()
    {
        checkInstance();

        Dimension screen = new Dimension(INDisplay.getWidth(), INDisplay.getHeight());

        return screen;
    }

    protected void initGL()
    {
        setAlphaBlending(true);
        setDepthTest(true);
        setProjection(ProjectionFormat.DirectorProjection2D);

        GL11.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
    }

    public static void setAlphaBlending(boolean blending)
    {
        if(Display.isCreated() == false) return;

        if(blending)
        {
            GL11.glEnable(GL11.GL_BLEND);
            GL11.glBlendFunc(INTypes.BLEND_SRC, INTypes.BLEND_DST);
        }
        else
        {
            GL11.glDisable(GL11.GL_BLEND);
        }
    }

    public static void setDepthTest(boolean depth)
    {
        if(Display.isCreated() == false) return;

        if(depth)
        {
            GL11.glClearDepth(1.0f);
            GL11.glEnable(GL11.GL_DEPTH_TEST);
            GL11.glDepthFunc(GL11.GL_LEQUAL);
            GL11.glHint(GL11.GL_PERSPECTIVE_CORRECTION_HINT, GL11.GL_NICEST);
        }
        else
        {
            GL11.glDisable(GL11.GL_DEPTH_TEST);
        }
    }

    public static void setProjection(ProjectionFormat format)
    {
        if(Display.isCreated() == false) return;

        Dimension screen = getScreenSize();

        switch(format)
        {
            case DirectorProjection2D:
                GL11.glMatrixMode(GL11.GL_PROJECTION);
                GL11.glLoadIdentity();
                GL11.glOrtho(0, screen.width, 0, screen.height, -1000, 1000);
                GL11.glMatrixMode(GL11.GL_MODELVIEW);
                GL11.glLoadIdentity();
                break;
            case DirectorProjection3D:
                GL11.glViewport(0, 0, screen.width, screen.height);
                GL11.glMatrixMode(GL11.GL_PROJECTION);
                GL11.glLoadIdentity();
                float aspect = (screen.width * 1.0f) / (screen.height * 1.0f);
                GLU.gluPerspective(55, aspect , 0, screen.height + screen.width);
                GL11.glMatrixMode(GL11.GL_MODELVIEW);
                GL11.glLoadIdentity();
                GLU.gluLookAt(screen.width / 2, screen.height / 2, getZEye(),
                        screen.width / 2, screen.height / 2, 0.0f, 0.0f,
                        1.0f, 0.0f);
                break;
        }

        instance.currentProjection = format;
    }

    public static final float getZEye()
    {
        Dimension screen = getScreenSize();
        float aspect = (screen.width * 1.0f) / (screen.height * 1.0f);
        return screen.height - (55 / 2) - aspect; 
    }

    public static void setDepthBufferFormat(DepthBufferFormat format)
    {
        checkInstance();

        if(Display.isCreated() == false)
            instance.depthBufferFormat = format;
    }

    public static ProjectionFormat getProjection()
    {
        checkInstance();

        return instance.currentProjection;
    }
    
    protected static void calculateDeltaTime()
    {
        checkInstance();

        long now = System.currentTimeMillis();

        if(instance.nextDeltaTimeZero)
        {
            instance.deltaTime = 0;
            instance.nextDeltaTimeZero = false;
        }
        else
        {
            instance.deltaTime = (float)(now - instance.lastUpdate) / 1000.0f;
        }

        instance.lastUpdate = now;
    }

    protected void setNextScene()
    {
        checkInstance();

        if(this.runningScene != null)
        {
            this.runningScene.onExit();
            this.runningScene.dispose();
        }
        
        this.runningScene = null;
        this.runningScene = instance.nextScene;
        this.nextScene = null;

        this.runningScene.onEnter();
        this.runningScene.onEnterTransitionDidFinish();

        System.gc();
    }

    public static void runWithScene(INScene scene)
    {
        if(scene == null) throw new NullPointerException();
     
        checkInstance();

        pushScene(scene);
        startAnimation();
    }
    
    public static void replaceScene(INScene scene)
    {
        if(scene == null) throw new NullPointerException();

        checkInstance();

        int stackSize = instance.sceneStack.size();
        
        instance.sceneStack.set(stackSize - 1, scene);
        instance.nextScene = scene;
    }

    public static void pushScene(INScene scene)
    {
        if(scene == null) throw new NullPointerException();

        checkInstance();

        instance.sceneStack.add(scene);
        instance.nextScene = scene;
    }

    public static void popScene()
    {
        checkInstance();

        if(instance.runningScene == null) throw new NullPointerException("A running scene is required to pop a scene");
        
        instance.sceneStack.remove(instance.sceneStack.lastElement());
        int stackSize = instance.sceneStack.size();

        if(stackSize == 0)
        {
            dispose();
        }
        else
        {
            instance.nextScene = instance.sceneStack.lastElement();
        }
    }

    public static void pauseDirector()
    {
        checkInstance();

        if(instance.isPaused)
            return;

        instance.isPaused = true;
        instance.nextDeltaTimeZero = true;
    }

    public static void resumeDirector()
    {
        checkInstance();

        if(!instance.isPaused)
            return;

        instance.isPaused = false;
        instance.deltaTime = 0;
    }

    public static void startAnimation()
    {
        checkInstance();

        if(instance.isRunning == false)
        {
            instance.isRunning = true;
            instance.run();
        }
    }

    public static void stopAnimation()
    {
        checkInstance();
        
        instance.isRunning = false;
        instance.nextDeltaTimeZero = true;
    }

    public static void setShowFPS(boolean show)
    {
        checkInstance();
        instance.displayFPS = show;
    }

    public static void dispose()
    {
        checkInstance();

        if(instance.runningScene != null)
        {
            instance.runningScene.onExit();
            instance.runningScene.dispose();
        }
        
        instance.runningScene = null;
        instance.nextScene = null;
        
        instance.sceneStack.clear();
        
        stopAnimation();
        INSpriteFrameCache.removeAllSpriteFrames();
        INScheduler.unscheduleAllTimers();
        INActionManager.removeAllActions();
        INTextureCache.removeAllTextures();
        INEventManager.removeAllListeners();
        
        INDisplay.destroy();
        System.exit(0);
    }

    protected void enableDefaultGLStates()
    {
        if(Display.isCreated() == false) return;
        GL11.glEnableClientState(GL11.GL_VERTEX_ARRAY);
        GL11.glEnableClientState(GL11.GL_COLOR_ARRAY);
        GL11.glEnableClientState(GL11.GL_TEXTURE_COORD_ARRAY);
        GL11.glEnable(GL11.GL_TEXTURE_2D);
    }

    protected void disableDefaultGLStates()
    {
        if(Display.isCreated() == false) return;
        GL11.glDisableClientState(GL11.GL_VERTEX_ARRAY);
        GL11.glDisableClientState(GL11.GL_COLOR_ARRAY);
        GL11.glDisableClientState(GL11.GL_TEXTURE_COORD_ARRAY);
        GL11.glDisable(GL11.GL_TEXTURE_2D);
    }

    public static void createWindow(String title, int width, int height, boolean fullscreen)
    {
        checkInstance();

        if(Display.isCreated() == false)
        {
            instance.create(title, width, height, fullscreen);
        }
    }

    public static void createWindow(String title, int width, int height, int FPS, boolean fullscreen)
    {
        checkInstance();

        if(Display.isCreated() == false)
        {
            instance.create(title, width, height, FPS, fullscreen);
        }
    }

    private void create(String title, int width, int height, boolean fullscreen)
    {
        INDisplay.createGLDisplay(title, width, height, instance.depthBufferFormat, fullscreen);
        this.initGL();
        this.enableDefaultGLStates();
    }

    private void create(String title, int width, int height, int FPS, boolean fullscreen)
    {
        INDisplay.setFrameRate(FPS);
        DEFAULT_FPS = FPS;
        INDisplay.createGLDisplay(title, width, height, instance.depthBufferFormat, fullscreen);
        this.initGL();
        this.enableDefaultGLStates();
    }

    public static String getVersion()
    {
        return VERSION;
    }

    public static String getResourceFolder()
    {
        checkInstance();

        return instance.resources;
    }
}