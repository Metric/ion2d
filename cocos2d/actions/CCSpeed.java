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
package cocos2d.actions;

import cocos2d.*;

public class CCSpeed extends CCAction
{
    private CCIntervalAction other;
    private float speed;

    public CCSpeed()
    {
        this.other = null;
    }

    public CCSpeed(CCIntervalAction action, float rate)
    {
        this.other = action;
        this.speed = rate;
    }

    public CCSpeed(CCSpeed original)
    {
        if(original == null) throw new NullPointerException();

        this.other = original.other;
        this.speed = original.speed;
        this.selector = original.selector;
        this.tag = original.tag;
    }

    public CCSpeed clone()
    {
        return new CCSpeed(this);
    }

    public void step(float time)
    {
        if(this.other == null) return;

        this.other.step(time * this.speed);
    }

    public boolean isDone()
    {
        if(this.other == null) return true;

        return this.other.isDone();
    }

    public CCSpeed reverse() throws Exception
    {
        if(this.other == null) return null;

        return new CCSpeed(this.other.reverse(), this.speed);
    }
}
