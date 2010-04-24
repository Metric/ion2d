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

public class CCFiniteAction extends CCAction
{
    protected float duration;

    /**
     * Override me
     * @return CCFiniteAction
     */
    public CCFiniteAction reverse() throws Exception
    {
        throw new Exception("Reverse Action Not Implemented");
    }

    public float getDuration()
    {
        return this.duration;
    }
}
