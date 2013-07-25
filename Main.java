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

import ion2d.*;
import ion2d.support.*;

/**
 * Test Casing in Main
 */
public class Main
{
    public static void main(String[] args)
    {
        Main myMain = new Main();

        INDirector.createWindow("Test Sprite 1", 1280, 720, 70, false);
        INDirector.setShowFPS(true);
        INDirector.runWithScene(new SpriteTest());
    }

    public Main()
    {

    }
}
