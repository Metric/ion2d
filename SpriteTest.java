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

import ion2d.events.*;
import ion2d.*;
import ion2d.support.INTypes.*;
import ion2d.actions.*;
import java.awt.*;
import java.awt.geom.*;

public class SpriteTest extends INScene {

    private INAnimation animation;
    private INSprite sprite;
    private INSpriteSheet spriteSheet;
    private INAnimate animate;
    private INMoveTo move;

    public SpriteTest()
    {
        try
        {
            spriteSheet = new INSpriteSheet(this.getClass().getClassLoader().getResource("bignibbles.png").toString(), 12);
            sprite = new INSprite(spriteSheet, new Rectangle(0,0, 81,81));

            animation = new INAnimation("nibbletest", 0.1f);
            animation.addFrameWithTexture(spriteSheet.getTexture(), new Rectangle(0, 0, 81, 81));
            animation.addFrameWithTexture(spriteSheet.getTexture(), new Rectangle(81, 0, 81, 81));
            animation.addFrameWithTexture(spriteSheet.getTexture(), new Rectangle(162, 0, 81, 81));
            animation.addFrameWithTexture(spriteSheet.getTexture(), new Rectangle(243, 0, 81, 81));
            animation.addFrameWithTexture(spriteSheet.getTexture(), new Rectangle(324, 0, 81, 81));
            animation.addFrameWithTexture(spriteSheet.getTexture(), new Rectangle(405, 0, 81, 81));
            animation.addFrameWithTexture(spriteSheet.getTexture(), new Rectangle(0, 81, 81, 81));
            animation.addFrameWithTexture(spriteSheet.getTexture(), new Rectangle(81, 81, 81, 81));
            animation.addFrameWithTexture(spriteSheet.getTexture(), new Rectangle(162, 81, 81, 81));
            animation.addFrameWithTexture(spriteSheet.getTexture(), new Rectangle(243, 81, 81, 81));
            animation.addFrameWithTexture(spriteSheet.getTexture(), new Rectangle(324, 81, 81, 81));
            animation.addFrameWithTexture(spriteSheet.getTexture(), new Rectangle(405, 81, 81, 81));

            sprite.addAnimation(animation);
            animate = new INAnimate(1.0f, animation, true);
            sprite.setAnchorPoint(new Vertex2F());
            sprite.setPosition(new Vertex2F());

            this.move = new INMoveTo(1.0f, new Point2D.Float(0.0f,0.0f));
            sprite.runAction(animate);
            sprite.runAction(this.move);
            INEventManager.addListener(new INEventListener(this, INMouseEvent.MOUSE_DOWN, "onMouseDown"));
            INEventManager.addListener(new INEventListener(this, INMouseEvent.MOUSE_UP, "onMouseUp"));
            INEventManager.addListener(new INEventListener(this, INKeyboardEvent.KEY_UP, "onKeyUp"));

            /*INMouse.addListener(new INMouseListener(sprite) {
                public synchronized void onMouseDown(int button, int x, int y)
                {
                    if(button == INMouse.LEFT_BUTTON)
                    {
                        try
                        {
                            if(animate.isDone())
                                sprite.runAction(animate);
                        } catch (Exception e)
                        {
                            System.out.println(e.getMessage());
                            e.printStackTrace();
                        }

                        System.out.println("MouseX: " + x + " MouseY: " + y);
                    }
                }

                public synchronized void onMouseUp(int button, int x, int y)
                {
                    if(button == INMouse.RIGHT_BUTTON)
                    {
                        try
                        {
                            if(animate.isDone())
                                sprite.runAction(animate);
                        } catch (Exception e)
                        {
                            System.out.println(e.getMessage());
                            e.printStackTrace();
                        }
                    }
                }
            });

            INKeyboard.addListener(new INKeyboardListener() {
                public synchronized void onKeyUp(int key)
                {
                    if(key == INKey.ESCAPE)
                    {
                        INDirector.dispose();
                    }
                    else if(key == INKey.F1)
                    {
                        INDirector.setFullScreen(true);
                    }
                    else if(key == INKey.F2)
                    {
                        INDirector.setFullScreen(false);
                    }
                }
            });*/

            this.addChild(spriteSheet);
            INLabelMap multiline = new INLabelMap(INDirector.getResourceFolder() + "/numbers.png", "0123\n456\n789", 16,22, '.');
            multiline.setPosition(new Vertex2F(200,200));
            multiline.setColor(new Color3B(255,0,0));
            this.addChild(multiline);

        } catch (Exception e)
        {
            System.out.println(e);
            e.printStackTrace();
        }
    }

    public synchronized final void onKeyUp(INKeyboardEvent event)
    {
        if(event.key == INKey.ESCAPE)
        {
            INDirector.setProjection(ProjectionFormat.DirectorProjection3D);
        }
    }

    public synchronized final void onMouseUp(INMouseEvent event)
    {
        if(event.button == INMouse.RIGHT_BUTTON)
        {
            if(this.move.isDone())
            {
                try
                {
                    this.move = new INMoveTo(1.0f, new Point2D.Float(event.x2, event.y2));
                    this.sprite.runAction(this.move);
                } catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
        }
    }

    public synchronized final void onMouseDown(INMouseEvent event)
    {
        if(event.button == INMouse.LEFT_BUTTON)
        {
            if(this.sprite.isInNode(event.x2, event.y2))
            {
                try
                {
                    if(this.animate.isDone())
                        this.sprite.runAction(animate);
                } catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
        }
    }

    public void draw()
    {
        /*GL11.glDisableClientState(GL11.GL_COLOR_ARRAY);
        GL11.glDisableClientState(GL11.GL_TEXTURE_COORD_ARRAY);
        float[] vertices = new float[3*4];
        vertices[0] = 0.0f;
        vertices[1] = 0.0f;
        vertices[2] = 0.0f;
        vertices[3] = 32.0f;
        vertices[4] = 0.0f;
        vertices[5] = 0.0f;
        vertices[6] = 32.0f;
        vertices[7] = 32.0f;
        vertices[8] = 0.0f;
        vertices[9] = 0.0f;
        vertices[10] = 32.0f;
        vertices[11] = 0.0f;

        FloatBuffer verticeBuffer = BufferUtils.createFloatBuffer(3*4);
        verticeBuffer.put(vertices);
        verticeBuffer.rewind();

        GL11.glVertexPointer(3, 0, verticeBuffer);
        GL11.glDrawArrays(GL11.GL_QUADS, 0, 4);
        GL11.glEnableClientState(GL11.GL_COLOR_ARRAY);
        GL11.glEnableClientState(GL11.GL_TEXTURE_COORD_ARRAY);*/
    }
}
