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

package cocos2d;

import org.lwjgl.opengl.*;
import java.nio.*;

public class CCGrabber
{

    private IntBuffer bufferObject;
    private IntBuffer oldBufferObject;

    public CCGrabber()
    {
        this.bufferObject = IntBuffer.allocate(1);
        this.oldBufferObject = IntBuffer.allocate(1);
        
        GL30.glGenFramebuffers(this.bufferObject);
    }

    public CCGrabber(CCGrabber original)
    {
        if(original == null) throw new NullPointerException();
        
        this.bufferObject = original.bufferObject.duplicate();
        this.oldBufferObject = original.oldBufferObject.duplicate();
    }

    public CCGrabber clone()
    {
        return new CCGrabber(this);
    }

    public void grab(CCTexture2D texture) throws Exception
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

    public void beforeRender()
    {
        GL11.glGetInteger(GL30.GL_FRAMEBUFFER_BINDING, this.oldBufferObject);
        GL30.glBindFramebuffer(GL30.GL_FRAMEBUFFER, this.bufferObject.get(0));

        GL11.glClearColor(0, 0, 0, 0);
        GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
    }

    public void afterRender()
    {
        GL30.glBindFramebuffer(GL30.GL_FRAMEBUFFER, this.oldBufferObject.get(0));
    }
}
