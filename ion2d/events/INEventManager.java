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

package ion2d.events;
import java.util.*;
import java.util.concurrent.*;
import java.lang.reflect.*;

public class INEventManager
{
    private static final INEventManager instance = new INEventManager();

    protected Vector<INEventListener> listeners;

    private INEventManager()
    {
        this.listeners = new Vector<INEventListener>(10);
    }

    public static final void addListener(INEventListener listener)
    {
        if(!instance.listeners.contains(listener))
        {
            instance.listeners.add(listener);
        }
    }

    public static final void removeListener(INEventListener listener)
    {
        if(instance.listeners.contains(listener))
        {
            instance.listeners.remove(listener);
        }
    }

    public static final void removeAllListeners()
    {
        instance.listeners.clear();
    }

    public static final void dispatch(INEvent event)
    {
        Dispatcher poll = new Dispatcher(event);

        poll.run();
    }

    protected static class Dispatcher implements Runnable
    {

        protected INEvent event;

        public Dispatcher(INEvent event)
        {
            this.event = event;
        }

        public final void run()
        {
            this.dispatch();
        }

        protected  final void dispatch()
        {
            int totalListeners = instance.listeners.size();

            DISPATCH:
            for(int i=0; i < totalListeners; i++)
            {
                INEventListener listener = instance.listeners.get(i);

                if(listener.event.equals(this.event.event))
                {
                    if(IDispatchEvent(listener, this.event))
                    {
                        if(this.event.bubbles == false)
                        {
                            break DISPATCH;
                        }
                    }
                }
            }
        }

        protected final boolean IDispatchEvent(INEventListener listener, INEvent event)
        {
            if(listener.target != null && listener.methodName != null)
            {
                try
                {
                    Method method = listener.target.getClass().getDeclaredMethod(listener.methodName, event.getClass());

                    method.invoke(listener.target, event);

                    return true;
                } catch (Exception e)
                {
                    System.out.println(e.getMessage());
                    e.printStackTrace();

                    return false;
                }
            }

            return false;
        }
    }
}
