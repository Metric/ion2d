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
import cocos2d.support.CCTypes.*;
import cocos2d.support.*;
import java.awt.*;

public class CCFollow extends CCAction
{
    private CCNode target;
    private boolean boundarySet;
    private Rectangle worldBoundary;

    public CCFollow()
    {
        this.target = null;
        this.worldBoundary = null;
        this.boundarySet = false;
    }

    public CCFollow(CCNode follow)
    {
        if(target == null) throw new NullPointerException();
        this.target = follow;
        this.boundarySet = false;
    }

    public CCFollow(CCNode follow, Rectangle worldBoundary)
    {
        if(target == null) throw new NullPointerException();
        this.target = follow;
        this.boundarySet = true;
        this.worldBoundary = worldBoundary;
    }

    public CCFollow(CCFollow original)
    {
        if(original == null) throw new NullPointerException();
        this.target = original.target;
        this.boundarySet = original.boundarySet;
        this.worldBoundary = original.worldBoundary;
        this.selector = original.selector;
        this.tag = original.tag;
    }

    public void setFollowBoundry(Rectangle rect) { this.worldBoundary = rect; this.boundarySet = true; }

    public CCFollow clone()
    {
        return new CCFollow(this);
    }

    public void step(float time)
    {
        if(this.selector == null || this.target == null) return;

        if(this.selector.target instanceof CCNode)
        {
            CCNode tempTarget = (CCNode)this.selector.target;
            Vertex2F tempPoint = new Vertex2F();

            if(tempTarget.getPosition().x + tempTarget.getContentSize().width > this.target.getPosition().x )
            {
                tempPoint.x = tempTarget.getPosition().x - 1.0f;
            }
            else if(tempTarget.getPosition().x - tempTarget.getContentSize().width < this.target.getPosition().x)
            {
                tempPoint.x = tempTarget.getPosition().x + 1.0f;
            }

            if(tempTarget.getPosition().y + tempTarget.getContentSize().height > this.target.getPosition().y)
            {
                tempPoint.y = tempTarget.getPosition().y - 1.0f;
            }
            else if(tempTarget.getPosition().y - tempTarget.getContentSize().height < this.target.getPosition().y)
            {
                tempPoint.y = tempTarget.getPosition().y + 1.0f;
            }

            //Check to see if the movement is within the specified world boundaries
            if(this.boundarySet)
            {
                tempPoint.x = CCMath.clamp(tempPoint.x, (float)this.worldBoundary.getMinX(), (float)this.worldBoundary.getWidth());
                tempPoint.y = CCMath.clamp(tempPoint.y, (float)this.worldBoundary.getMinY(), (float)this.worldBoundary.getHeight());
            }

            tempTarget.setPosition(tempPoint);
        }
    }

    public boolean isDone()
    {
        if(this.target == null) return true;

        return !this.target.isRunning();
    }

    public void stop()
    {
        super.stop();
    }
}
