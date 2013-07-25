/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author aaklick
 */

import ion2d.support.*;
import org.lwjgl.opengl.*;
import java.nio.*;
import org.lwjgl.BufferUtils;

public class TestCase2
{
    public static void main(String[] args)
    {
        INDisplay.createGLDisplay("TestCase", 800, 600, INTypes.DepthBufferFormat.DepthBufferNone, false);

        setAlphaBlending(true);
        setDepthTest(true);

        GL11.glMatrixMode(GL11.GL_PROJECTION);
        GL11.glLoadIdentity();
        GL11.glOrtho(0, INDisplay.getWidth(), 0, INDisplay.getHeight(), -1, 1);
        GL11.glMatrixMode(GL11.GL_MODELVIEW);
        GL11.glLoadIdentity();
        GL11.glViewport(0, 0, INDisplay.getWidth(), INDisplay.getHeight());

        while(!Display.isCloseRequested())
        {
            if(Display.isActive())
            {
                GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);

                GL11.glPushMatrix();

                enableDefaultGLStates();

                float[] vertices = new float[3*4];
                vertices[0] = 32.0f;
                vertices[1] = 32.0f;
                vertices[2] = 0.0f;
                vertices[3] = 118.0f;
                vertices[4] = 32.0f;
                vertices[5] = 0.0f;
                vertices[6] = 118.0f;
                vertices[7] = 118.0f;
                vertices[8] = 0.0f;
                vertices[9] = 32.0f;
                vertices[10] = 118.0f;
                vertices[11] = 0.0f;

                float[] colors = new float[4*4];
                colors[0] = 0.5f;
                colors[1] = 1.0f;
                colors[2] = 0.5f;
                colors[3] = 1.0f;
                colors[4] = 0.5f;
                colors[5] = 1.0f;
                colors[6] = 0.5f;
                colors[7] = 1.0f;
                colors[8] = 0.5f;
                colors[9] = 1.0f;
                colors[10] = 0.5f;
                colors[11] = 1.0f;
                colors[12] = 0.5f;
                colors[13] = 1.0f;
                colors[14] = 0.5f;
                colors[15] = 1.0f;

                FloatBuffer verticeBuffer = BufferUtils.createFloatBuffer(3*4);
                verticeBuffer.put(vertices);
                verticeBuffer.rewind();

                FloatBuffer colorBuffer = BufferUtils.createFloatBuffer(4*4);
                colorBuffer.put(colors);
                colorBuffer.rewind();

                GL11.glColorPointer(4, 0, colorBuffer);
                GL11.glVertexPointer(3, 0, verticeBuffer);
                GL11.glDrawArrays(GL11.GL_QUADS, 0, 4);

                disableDefaultGLStates();

                GL11.glPopMatrix();

                System.gc();
            }

            INDisplay.update();
        }

        Display.destroy();
    }

    protected static void enableDefaultGLStates()
    {
        if(Display.isCreated() == false) return;
        GL11.glEnableClientState(GL11.GL_VERTEX_ARRAY);
        GL11.glEnableClientState(GL11.GL_COLOR_ARRAY);
        //GL11.glEnableClientState(GL11.GL_TEXTURE_COORD_ARRAY);
        GL11.glEnable(GL11.GL_TEXTURE_2D);
    }

    protected static void disableDefaultGLStates()
    {
        if(Display.isCreated() == false) return;
        GL11.glDisableClientState(GL11.GL_VERTEX_ARRAY);
        GL11.glDisableClientState(GL11.GL_COLOR_ARRAY);
        //GL11.glDisableClientState(GL11.GL_TEXTURE_COORD_ARRAY);
        GL11.glDisable(GL11.GL_TEXTURE_2D);
    }

       public static void setAlphaBlending(boolean blending)
    {
        if(Display.isCreated() == false) return;

        if(blending)
        {
            GL11.glEnable(GL11.GL_BLEND);
            GL11.glBlendFunc(INTypes.BLEND_SRC, INTypes.BLEND_DST);
        }
        else
        {
            GL11.glDisable(GL11.GL_BLEND);
        }
    }

    public static void setDepthTest(boolean depth)
    {
        if(Display.isCreated() == false) return;

        if(depth)
        {
            GL11.glClearDepth(1.0f);
            GL11.glEnable(GL11.GL_DEPTH_TEST);
            GL11.glDepthFunc(GL11.GL_LEQUAL);
            GL11.glHint(GL11.GL_PERSPECTIVE_CORRECTION_HINT, GL11.GL_NICEST);
        }
        else
        {
            GL11.glDisable(GL11.GL_DEPTH_TEST);
        }
    }

}
