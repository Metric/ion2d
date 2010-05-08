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

import cocos2d.support.*;
import cocos2d.support.CCTypes.*;
import java.util.*;

public class CCActionManager
{
    protected static CCActionManager instance;

    protected Hashtable<CCNode, ActionElement> targets;

    private CCActionManager()
    {
        try
        {
            CCScheduler.scheduleTimer(new CCTimer(new CCSelector(this,this.getClass().getMethod("tick", float.class))));
        }
        catch (Exception e)
        {
            System.out.println(e.getMessage());
        }

        this.targets = new Hashtable<CCNode, ActionElement>(10,10);
    }

    protected static void checkInstance()
    {
        if(instance == null)
        {
            instance = new CCActionManager();
        }
    }

    /**
     * The timer calls this method when the timer ticks
     * @param float time
     */
    public void tick(float time)
    {
        checkInstance();

        Enumeration<CCNode> keys = instance.targets.keys();

        while(keys.hasMoreElements())
        {
            CCNode target = keys.nextElement();
            ActionElement element = instance.targets.get(target);

            if(!element.paused)
            {
                for(CCAction action : element.actions)
                {
                    action.step(time);

                    if(action.isDone())
                    {
                        removeAction(action, target);
                    }
                }
            }

            if(element.actions.size() == 0)
            {
                instance.targets.remove(target);
            }
        }
    }

    /**
     * Adds an action to the queue if unpaused
     * The action will be run against the specified target
     * @param CCAction action
     * @param CCNode target
     * @param boolean paused
     */
    public static void addAction(CCAction action, CCNode target, boolean paused)
    {
        checkInstance();

        if(instance.targets.containsKey(target))
        {
            ActionElement element = instance.targets.get(target);

            element.actions.add(action);
        }
        else
        {
            ActionElement element = new ActionElement(action, paused);
            instance.targets.put(target, element);
        }

        action.startWithTarget(new CCSelector(target, null));
    }

    /**
     * Removes all the actions currently stored in the manager
     */
    public static void removeAllActions()
    {
        checkInstance();

        Enumeration<CCNode> keys = instance.targets.keys();

        FindAction:
        while(keys.hasMoreElements())
        {
            ActionElement element = instance.targets.get(keys.nextElement());

            for(CCAction action : element.actions)
            {
               action.stop(); 
            }
        }

        instance.targets.clear();
    }

    /**
     * Removes all the actions associated with the target
     * @param CCNode target
     */
    public static void removeAllActions(CCNode target)
    {
        checkInstance();

        if(instance.targets.containsKey(target))
        {
            //Before we completely remove all the actions for the target
            //We need to stop each action.
            ActionElement element = instance.targets.get(target);

            for(CCAction action : element.actions)
            {
                action.stop();
            }

            instance.targets.remove(target);
        }
    }

    /**
     * Removes the specified action from the manager
     * @param CCAction action
     */
    public static void removeAction(CCAction action)
    {
        checkInstance();

        Enumeration<CCNode> keys = instance.targets.keys();

        FindAction:
        while(keys.hasMoreElements())
        {
            ActionElement element = instance.targets.get(keys.nextElement());

            if(element.actions.contains(action))
            {
                action.stop();
                element.actions.remove(action);
                break FindAction;
            }
        }
    }

    /**
     * Removes the specified action from the target
     * @param CCAction action
     * @param CCNode target
     */
    public static void removeAction(CCAction action, CCNode target)
    {
        checkInstance();
        
        if(instance.targets.containsKey(target))
        {
            ActionElement element = instance.targets.get(target);
            
            if(element.actions.contains(action))
            {
                action.stop();
                element.actions.remove(action);
            }
        }
    }

    /**
     * Removes the specified action by tag from the target
     * @param int tag
     * @param CCNode target
     */
    public static void removeAction(int tag, CCNode target)
    {
        checkInstance();

        if(instance.targets.containsKey(target))
        {
            ActionElement element = instance.targets.get(target);

            FindAction:
            for(CCAction action : element.actions)
            {
                if(action.getTag() == tag)
                {
                    action.stop();
                    element.actions.remove(action);
                    break FindAction;
                }
            }
        }
    }

    /**
     * Gets the action by tag from the target
     * @param int tag
     * @param CCNode target
     * @return CCAction
     */
    public static CCAction getAction(int tag, CCNode target)
    {
        checkInstance();

        CCAction found = null;

        if(instance.targets.containsKey(target))
        {
            ActionElement element = instance.targets.get(target);

            FindAction:
            for(CCAction action : element.actions)
            {
                if(action.getTag() == tag)
                {
                    found = action;
                    break FindAction;
                }
            }
        }

        return found;
    }

    /**
     * Gets the total number of running actions for the specified target
     * @param CCNode target
     * @return int
     */
    public static int totalRunningActions(CCNode target)
    {
        checkInstance();

        int totalActions = 0;

        if(instance.targets.containsKey(target))
        {
            ActionElement element = instance.targets.get(target);

            totalActions = element.actions.size();
        }

        return totalActions;
    }

    /**
     * Pause all actions for the specified target
     * @param CCNode target
     */
    public static void pauseAllActions(CCNode target)
    {
        checkInstance();

        if(instance.targets.containsKey(target))
        {
            ActionElement element = instance.targets.get(target);

            element.paused = true;
        }
    }

    /**
     * Resumes all actions for the specified target
     * @param CCNode target
     */
    public static void resumeAllActions(CCNode target)
    {
        checkInstance();

        if(instance.targets.containsKey(target))
        {
            ActionElement element = instance.targets.get(target);

            element.paused = false;
        }
    }

    /**
     * It is easier to just make this an inner class so we can
     * Directly access the protected variables
     */
    protected static class ActionElement
    {
        protected Vector<CCAction> actions;;
        protected CCAction currentAction;
        protected boolean paused;

        public ActionElement()
        {
            this(null, true);
        }

        public ActionElement(CCAction action, boolean paused)
        {
            this.actions = new Vector<CCAction>(10);
            this.currentAction = null;

            if(action != null)
                this.actions.add(action);

            this.paused = paused;
        }
    }
}
