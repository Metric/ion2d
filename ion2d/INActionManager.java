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

import ion2d.support.*;
import ion2d.support.INTypes.*;
import java.util.*;

public class INActionManager
{
    protected static INActionManager instance;

    protected Hashtable<INNode, ActionElement> targets;

    private INActionManager()
    {
        try
        {
            INScheduler.scheduleTimer(new INTimer(new INSelector(this,this.getClass().getMethod("tick", float.class))));
        }
        catch (Exception e)
        {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }

        this.targets = new Hashtable<INNode, ActionElement>(10,10);
    }

    /**
     * Checks to see if the singleton instance has been created
     * If it hasn't then it creates it
     */
    protected static void checkInstance()
    {
        if(instance == null)
        {
            instance = new INActionManager();
        }
    }

    /**
     * The timer calls this method when the timer ticks
     * @param float time
     */
    public static void tick(float time)
    {
        checkInstance();

        Enumeration<INNode> keys = instance.targets.keys();

        while(keys.hasMoreElements())
        {
            INNode target = keys.nextElement();
            ActionElement element = instance.targets.get(target);

            if(!element.paused)
            {
                for(int i = 0; i < element.actions.size(); i++)
                {
                    INAction action = element.actions.get(i);
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
     * @param INAction action
     * @param INNode target
     * @param boolean paused
     */
    public static void addAction(INAction action, INNode target, boolean paused)
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

        action.startWithTarget(new INSelector(target, null));
    }

    /**
     * Removes all the actions currently stored in the manager
     */
    public static void removeAllActions()
    {
        checkInstance();

        Enumeration<INNode> keys = instance.targets.keys();

        FindAction:
        while(keys.hasMoreElements())
        {
            ActionElement element = instance.targets.get(keys.nextElement());

            for(INAction action : element.actions)
            {
               action.stop(); 
            }
        }

        instance.targets.clear();
    }

    /**
     * Removes all the actions associated with the target
     * @param INNode target
     */
    public static void removeAllActions(INNode target)
    {
        checkInstance();

        if(instance.targets.containsKey(target))
        {
            //Before we completely remove all the actions for the target
            //We need to stop each action.
            ActionElement element = instance.targets.get(target);

            for(INAction action : element.actions)
            {
                action.stop();
            }

            instance.targets.remove(target);
        }
    }

    /**
     * Removes the specified action from the manager
     * @param INAction action
     */
    public static void removeAction(INAction action)
    {
        checkInstance();

        Enumeration<INNode> keys = instance.targets.keys();

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
     * @param INAction action
     * @param INNode target
     */
    public static void removeAction(INAction action, INNode target)
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
     * @param INNode target
     */
    public static void removeAction(int tag, INNode target)
    {
        checkInstance();

        if(instance.targets.containsKey(target))
        {
            ActionElement element = instance.targets.get(target);

            FindAction:
            for(INAction action : element.actions)
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
     * @param INNode target
     * @return INAction
     */
    public static INAction getAction(int tag, INNode target)
    {
        checkInstance();

        INAction found = null;

        if(instance.targets.containsKey(target))
        {
            ActionElement element = instance.targets.get(target);

            FindAction:
            for(INAction action : element.actions)
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
     * @param INNode target
     * @return int
     */
    public static int totalRunningActions(INNode target)
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
     * @param INNode target
     */
    public static void pauseAllActions(INNode target)
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
     * @param INNode target
     */
    public static void resumeAllActions(INNode target)
    {
        checkInstance();

        if(instance.targets.containsKey(target))
        {
            ActionElement element = instance.targets.get(target);

            element.paused = false;
        }
    }

    /**
     * Finalize override
     */
    public void finalize()
    {
        removeAllActions();
    }

    /**
     * It is easier to just make this an inner class so we can
     * Directly access the protected variables
     */
    protected static class ActionElement
    {
        protected ArrayList<INAction> actions;;
        protected INAction currentAction;
        protected boolean paused;

        /**
         * Default constructor
         */
        public ActionElement()
        {
            this(null, true);
        }

        /**
         * Secondary constructor
         * @param INAction action
         * @param boolean paused
         */
        public ActionElement(INAction action, boolean paused)
        {
            this.actions = new ArrayList<INAction>(10);
            this.currentAction = null;

            if(action != null)
                this.actions.add(action);

            this.paused = paused;
        }
    }
}
