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

package ion2d;

import org.lwjgl.opengl.*;
import java.nio.*;
import org.lwjgl.BufferUtils;

public class INGrabber
{

    private IntBuffer bufferObject;
    private IntBuffer oldBufferObject;

    /**
     * Default constructor
     */
    public INGrabber()
    {
        this.bufferObject = BufferUtils.createIntBuffer(1);
        this.oldBufferObject = BufferUtils.createIntBuffer(1);
        
        GL30.glGenFramebuffers(this.bufferObject);
    }

    /**
     * Copy Constructor
     * @param INGrabber original
     */
    public INGrabber(INGrabber original)
    {
        if(original == null) throw new NullPointerException();
        
        this.bufferObject = original.bufferObject.duplicate();
        this.oldBufferObject = original.oldBufferObject.duplicate();
    }

    /**
     * Clone Override
     * @return INGrabber
     */
    public INGrabber clone()
    {
        return new INGrabber(this);
    }

    /**
     * Grabs an image of the display and attachs it to the specified texture
     * @param INTexture2D texture
     * @throws Exception
     */
    public void grab(INTexture2D texture) throws Exception
    {
        GL11.glGetInteger(GL30.GL_FRAMEBUFFER_BINDING, this.oldBufferObject);

        GL30.glBindFramebuffer(GL30.GL_FRAMEBUFFER, this.bufferObject.get(0));

        GL30.glFramebufferTexture2D(GL30.GL_FRAMEBUFFER, GL30.GL_COLOR_ATTACHMENT0, GL11.GL_TEXTURE_2D, texture.getId(), 0);

        int status = GL30.glCheckFramebufferStatus(GL30.GL_FRAMEBUFFER);

        if(status != GL30.GL_FRAMEBUFFER_COMPLETE)
        {
            throw new Exception("Frame Grabber could not attach texture to framebuffer");
        }

        GL30.glBindFramebuffer(GL30.GL_FRAMEBUFFER, this.oldBufferObject.get(0));
    }

    /**
     * Before Rendering of grabbing the screen
     */
    public void beforeRender()
    {
        GL11.glGetInteger(GL30.GL_FRAMEBUFFER_BINDING, this.oldBufferObject);
        GL30.glBindFramebuffer(GL30.GL_FRAMEBUFFER, this.bufferObject.get(0));

        GL11.glClearColor(0, 0, 0, 0);
        GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
    }

    /**
     * After Rendering of grabbing the screen
     */
    public void afterRender()
    {
        GL30.glBindFramebuffer(GL30.GL_FRAMEBUFFER, this.oldBufferObject.get(0));
    }

    public void finalize()
    {
        GL15.glDeleteBuffers(this.bufferObject);
    }
}
