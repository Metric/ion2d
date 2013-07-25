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

package ion2d.support;

import java.lang.reflect.Method;

public class INSelector {
    public Method selector;
    public Object target;

    /**
     * Default constructor
     * Everything is set to null
     */
    public INSelector()
    {
        this(null,null);
    }

    /**
     * Secondary Constructor allowing setting of Method and Object to target
     * @param Object target
     * @param Method selector
     */
    public INSelector(Object target, Method selector)
    {
        this.target = target;
        this.selector = selector;
    }

    /**
     * Copy Constructor
     * @param INSelector original
     */
    public INSelector(INSelector original)
    {
        if(original == null) throw new NullPointerException();

        this.target = original.target;
        this.selector = original.selector;
    }

    /**
     * Clone override
     * @return INSelector
     */
    public INSelector clone()
    {
        return new INSelector(this);
    }
}
