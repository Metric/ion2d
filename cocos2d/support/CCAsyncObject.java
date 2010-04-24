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

public class CCAsyncObject
{
    public CCSelector selector;
    public Object data;

    public CCAsyncObject()
    {
        this.selector = null;
        this.data = null;
    }

    public CCAsyncObject(CCSelector callback, Object data)
    {
        this.data = data;
        this.selector = callback;
    }
}
