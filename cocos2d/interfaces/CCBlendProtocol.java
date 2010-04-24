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

package cocos2d.interfaces;

import cocos2d.support.CCTypes.*;

public interface CCBlendProtocol
{
    void setBlendFunction(CCBlend blend);

    CCBlend getBlendFunction();
}
