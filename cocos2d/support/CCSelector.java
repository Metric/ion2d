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

package cocos2d.support;

import java.lang.reflect.Method;

public class CCSelector {
    public Method selector;
    public Object target;

    public CCSelector()
    {
        this(null,null);
    }

    public CCSelector(Object target, Method selector)
    {
        this.target = target;
        this.selector = selector;
    }

    public CCSelector(CCSelector original)
    {
        if(original == null) throw new NullPointerException();

        this.target = original.target;
        this.selector = original.selector;
    }

    public CCSelector clone()
    {
        return new CCSelector(this);
    }
}
