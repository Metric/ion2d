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

import cocos2d.support.*;
import cocos2d.support.CCTypes.*;
import java.awt.*;
import java.awt.geom.*;
import java.util.Vector;
import java.util.Hashtable;
import java.util.Enumeration;
import java.util.Comparator;
import java.util.Collections;
import org.lwjgl.opengl.*;

public class CCNode implements Comparator<CCNode> {
    protected float rotation;

    protected float scaleX;
    protected float scaleY;

    protected Vertex2F position;
    protected boolean visible;

    protected Vertex2F anchorPointInPixel;
    protected Vertex2F anchorPoint;

    protected boolean isRelativeAnchorePoint;
    protected Dimension contentSize;

    protected AffineTransform transform, inverse;
    protected float vertexZ;

    protected int zOrder;

    protected CCCamera camera;
    protected CCGridBase grid;

    protected Vector<CCNode> children;
    protected CCNode parent;

    protected int tag;

    protected Object userData;

    protected Hashtable<CCSelector, CCTimer> scheduleSelectors;
    protected boolean isRunning;

    protected boolean isTransformDirty;
    protected boolean isInverseDirty;

    protected Vertex2F offsetPosition;

    public CCNode()
    {
        this.isRunning = false;
        
        this.rotation = 0.0f;
        this.scaleX = this.scaleY = 1.0f;
        this.position = new Vertex2F();
        this.anchorPoint = new Vertex2F();
        this.anchorPointInPixel = new Vertex2F();
        this.contentSize = new Dimension(0,0);

        this.offsetPosition = new Vertex2F();
        this.isRelativeAnchorePoint = true;
        this.isTransformDirty = this.isInverseDirty = true;

        this.vertexZ = 0;
        this.visible = true;
        this.tag = -1;
        this.zOrder = 0;

        this.children = new Vector<CCNode>();
        this.scheduleSelectors = new Hashtable<CCSelector, CCTimer>();

        this.userData = null;
    }

    public void dispose()
    {
        this.stopAllActions();
        this.scheduleSelectors.clear();
        this.scheduleSelectors = null;
        this.parent = null;
        this.removeAllChildrenWithCleanup(true);
    }

    public void finalize()
    {
        this.dispose();
    }

    /**
     * Add a child to the node. Returns itself to allow for chaining
     * @param CCNode node
     */
    public CCNode addChild(CCNode node) throws Exception
    {
        if(node.parent == null) node.parent = this;

        this.children.add(node);

        Collections.sort(this.children, this);

        if(this.isRunning)
        {
            node.onEnter();
        }

        return this;
    }

    /**
     * Returns the current index of the child in the Vector
     * @param CCNode child
     * @return in
     */
    public int getIndexForChild(CCNode child)
    {
        return this.children.indexOf(child);
    }

    /**
     * Adds a child to the node and sets the z order of the child
     * @param CCNode node
     * @param int z
     * @return CCNode
     */
    public CCNode addChild(CCNode node, int z) throws Exception
    {
        if(node.parent == null) node.parent = this;
        node.setZOrder(z);
        this.children.add(node);

        Collections.sort(this.children, this);

        if(this.isRunning)
        {
            node.onEnter();
        }

        return this;
    }

    /**
     * Adds a child to the node, sets the zorder and tag of the child
     * @param CCNode node
     * @param int z
     * @param int tag
     * @return CCNode
     */
    public CCNode addChild(CCNode node, int z, int tag) throws Exception
    {
        node.setZOrder(z);
        node.setTag(tag);
        if(node.parent == null) node.parent = this;

        this.children.add(node);
        Collections.sort(this.children, this);

        if(this.isRunning)
        {
            node.onEnter();
        }

        return this;
    }

    /**
     * Gets the child node by tag
     * @param int tag
     * @return CCNode
     */
    public CCNode getChildByTag(int tag)
    {
        CCNode found = null;
        
        for(CCNode child : this.children)
        {
            if(child.tag == tag)
            {
                return child;
            }
        }
        
        return found;
    }

    /**
     * Removes the specified child node
     * @param CCNode node
     * @param boolean cleanup
     */
    public void removeChild(CCNode node, boolean cleanup)
    {
        if(node == null)
            return;

        if(this.children.contains(node))
        {
            this.detachChild(node, cleanup);
        }
    }

    /**
     * Removes a child by the specified tag
     * @param int tag
     * @param boolean cleanup
     */
    public void removeChildByTag(int tag, boolean cleanup)
    {
        CCNode node = this.getChildByTag(tag);

        if(node != null)
        {
            this.detachChild(node, cleanup);
        }
    }

    /**
     * Removes all child nodes
     * @param cleanup
     */
    public void removeAllChildrenWithCleanup(boolean cleanup)
    {
        for(CCNode node : this.children)
        {
            if(this.isRunning)
            {
                node.onExit();
            }

            if(cleanup)
            {
                node.dispose();
            }
        }

        this.children.clear();
    }

    /**
     * Fully removes the child from the node
     * @param CCNode child
     * @param boolean cleanup
     */
    private void detachChild(CCNode child, boolean cleanup)
    {
        if(this.isRunning)
        {
            child.onExit();
        }
        
        if(cleanup)
        {
            child.dispose();
        }
        
        this.children.remove(child);
    }

    /**
     * Performs the draw routine for the node
     */
    protected void draw()
    {
        //Override Me
        //Only draw your items in this method
    }

    /**
     * Visits the node and renders it
     */
    public void visit()
    {
        if(!this.visible)
            return;

        GL11.glPushMatrix();

        if(this.grid != null && this.grid.getActive())
        {
            this.grid.beforeDraw();
            this.transformAncestors();
        }

        this.transform();

        for(CCNode node : this.children)
        {
            if(node.zOrder < 0)
                node.visit();
            else
                break;
        }

        this.draw();

        for(CCNode node : this.children)
        {
            if(node.zOrder >= 0)
                node.visit();
        }

        if(this.grid != null && this.grid.getActive())
        {
            this.grid.afterDraw(this);
        }

        GL11.glPopMatrix();
    }

    /**
     * Transforms all the parent nodes of the child node
     */
    public void transformAncestors()
    {
        if(this.parent != null)
        {
            this.parent.transformAncestors();
            this.parent.transform();
        }
    }

    /**
     * Applies the transformation for the current node
     */
    public void transform()
    {
       GL11.glMultMatrix(CCMath.AffineToGL(this.nodeToParentTransform()));

       if(this.vertexZ >= 0 || this.vertexZ <= 0)
       {
           GL11.glTranslatef(0, 0, this.vertexZ);
       }

       if(this.camera != null && !(this.grid != null && this.grid.getActive()))
       {
           boolean translate = (this.anchorPointInPixel.x != 0.0f || this.anchorPointInPixel.y != 0.0f);

           if(translate)
           {
                GL11.glTranslatef(this.anchorPointInPixel.x, this.anchorPointInPixel.y, 0);
           }

           this.camera.locate();

           if(translate)
           {
               GL11.glTranslatef(-this.anchorPointInPixel.x, -this.anchorPointInPixel.y, 0);
           }
       }
    }

    /**
     * On Enter when the node loads
     */
    public void onEnter()
    {
        for(CCNode node : this.children)
        {
            node.onEnter();
        }

        this.activateTimers();

        this.isRunning = true;
    }

    /**
     * On Enter when the node loads after transition
     */
    public void onEnterTransitionDidFinish()
    {
        for(CCNode node : this.children)
        {
            node.onEnterTransitionDidFinish();
        }
    }

    /**
     * On Exit when the node is unloaded from the scene
     */
    public void onExit()
    {
         this.deactivateTimers();

         this.isRunning = false;

         for(CCNode node : this.children)
         {
             node.onExit();
         }
     }

     /**
      * Add on the action manager code for the node
      */

     /**
      * schedule a timer to run
      * @param CCSelector selector
      */
     public void schedule(CCSelector selector)
     {
         this.schedule(selector, 0);
     }

     /**
      * Schedule a timer to active at the specified interval
      * @param CCSelector selector
      * @param float interval
      */
     public void schedule(CCSelector selector, float interval)
     {
        if(selector != null)
        {
            if(this.scheduleSelectors.containsKey(selector))
                return;

            CCTimer timer = new CCTimer(selector, interval);

            if(this.isRunning)
                CCScheduler.getInstance().scheduleTimer(timer);

            this.scheduleSelectors.put(selector, timer);
        }
     }

     /**
      * Unschedules a timer based on selector
      * @param selector
      */
     public void unschedule(CCSelector selector)
     {
         if(selector == null) return;

         CCTimer timer = null;

         if(this.scheduleSelectors.contains(selector))
         {
             timer = this.scheduleSelectors.get(selector);
             this.scheduleSelectors.remove(selector);
         }

         if(this.isRunning && timer != null)
         {
             CCScheduler.getInstance().unscheduleTimer(timer);
         }
     }

     /**
      * Activates the available timers
      */
     private void activateTimers()
     {
         for(Enumeration e = this.scheduleSelectors.keys(); e.hasMoreElements();)
         {
             CCScheduler.getInstance().scheduleTimer((CCTimer)e.nextElement());
         }

         //Add Actions resume code in here
     }

     /**
      * Deactivates the timers on this node
      */
     private void deactivateTimers()
     {
         for(Enumeration e = this.scheduleSelectors.keys(); e.hasMoreElements();)
         {
             CCScheduler.getInstance().unscheduleTimer((CCTimer)e.nextElement());
         }         
         
         //Add action pause code in here
     }

    /**
     * Compares 2 nodes z order to see which is the smallest or largest
     * Of the 2
     * @param CCNode node1
     * @param CCNode node2
     * @return int
     */
    public int compare(CCNode node1, CCNode node2)
    {
        if(node1.zOrder > node2.zOrder)
        {
            return 1;
        }
        else if(node1.zOrder == node2.zOrder)
        {
            return 0;
        }
        else
        {
            return -1;
        }
    }

    //Getters and Setters

    public CCCamera getCamera()
    {
        if(this.camera == null)
            this.camera = new CCCamera();

        return this.camera;
    }

    public int getZOrder() { return this.zOrder; }
    public float getVertexZ() { return this.vertexZ; }
    public float getRotation() { return this.rotation; }
    public float getScaleX() { return this.scaleX; }
    public float getScaleY() { return this.scaleY; }
    public Vertex2F getPosition() { return this.position; }

    public boolean isVisible() { return this.visible; }
    public boolean isRunning() { return this.isRunning; }
    public boolean isRelativeAnchorPoint() { return this.isRelativeAnchorePoint; }

    public int getTag() { return this.tag; }
    public Object getData() { return this.userData; }

    public Vertex2F getAnchorPoint() { return this.anchorPoint; }
    public Vertex2F getAnchorePointInPixel() { return this.anchorPointInPixel; }
    public Dimension getContentSize() { return (Dimension)this.contentSize.clone(); }
    public CCNode getParent() { return this.parent; }

    public void setScale(float amount) { this.scaleX = this.scaleY = amount; this.isTransformDirty = this.isInverseDirty = true; }
    public void setScaleX(float amount) { this.scaleX = amount; this.isTransformDirty = this.isInverseDirty = true; }
    public void setScaleY(float amount) { this.scaleY = amount; this.isTransformDirty = this.isInverseDirty = true; }

    public void setZOrder(int z)
    {
        this.zOrder = z;

        if(this.parent != null)
        {
            Collections.sort(this.parent.children, this.parent);
        }

        //Check to see if the parent is a sprite sheet and if so
        //Reorder the sprite sheet Atlas
        if(this.parent.getClass() == CCSpriteSheet.class)
        {
            CCSpriteSheet sheet = (CCSpriteSheet)this.parent;
            sheet.reorderAtlas();
        }
    }
    public void setVertexZ(float z) { this.vertexZ = z; }
    public void setRotation(float rotation) { this.rotation = rotation; this.isTransformDirty = this.isInverseDirty = true; }
    public void setPosition(Vertex2F point) { this.position = point; this.isTransformDirty = this.isInverseDirty = true; }
    public void setVisible(boolean visible) { this.visible = visible; }

    public void setIsRelativeAnchorePoint(boolean relative)
    {
        this.isRelativeAnchorePoint = relative;
        this.isTransformDirty = this.isInverseDirty = true;
    }

    public void setGrid(CCGridBase grid)
    {
        this.grid = grid;
    }

    public void setData(Object data) { this.userData = data; }
    public void setParent(CCNode parent) { this.parent = parent; }

    public void setAnchorPoint(Vertex2F point)
    {
        if(!this.anchorPoint.equals(point))
        {
            this.anchorPoint = point;
            this.anchorPointInPixel = new Vertex2F(this.contentSize.width * this.anchorPoint.x, this.contentSize.height * this.anchorPoint.y);
            this.isTransformDirty = this.isInverseDirty = true;
        }
    }

    public void setContentSize(Dimension size)
    {
        if(!this.contentSize.equals(size))
        {
            this.contentSize = size;
            this.anchorPointInPixel = new Vertex2F(size.width * this.anchorPoint.x, size.height * this.anchorPoint.y);
            this.isTransformDirty = this.isInverseDirty = true;
        }
    }

    public void setTag(int tag) { this.tag = tag; }

    /**
     * Gets the bounding box of the node
     * @return Rectangle
     */
    public Rectangle getBoundingBox()
    {
        Rectangle rect = new Rectangle(0,0,this.contentSize.width, this.contentSize.height);
        return (Rectangle)this.nodeToParentTransform().createTransformedShape(rect);
    }

    /**
     * Gets the nodeToParent AffineTransform
     * @return AffineTransform
     */
    public AffineTransform nodeToParentTransform()
    {
        if(this.isTransformDirty)
        {
            this.transform.setToIdentity();

            if(!this.isRelativeAnchorePoint)
                this.transform.translate(this.anchorPointInPixel.x, this.anchorPointInPixel.y);

            this.transform.translate(this.position.x, this.position.y);
            this.transform.rotate(-CCMath.degreesToRadians(this.rotation));
            this.transform.scale(this.scaleX, this.scaleY);
            this.transform.translate(-this.anchorPointInPixel.x, -this.anchorPointInPixel.y);

            this.isTransformDirty = false;
        }

        return this.transform;
    }

    /**
     * Gets the parentToNode AffineTransform
     * @return AffineTransform
     */
    public AffineTransform parentToNodeTransform()
    {
        try
        {
            if(this.isInverseDirty)
            {
                this.inverse = this.nodeToParentTransform().createInverse();
                this.isInverseDirty = false;
            }
        }
        catch (Exception e)
        {
            System.out.println(e.getMessage());
        }

        return this.inverse;
    }

    /**
     * Gets the node to world transform affine
     * @return AffineTransform
     */
    public AffineTransform nodeToWorldTransform()
    {
        AffineTransform t = this.nodeToParentTransform();

        for(CCNode parent = this.parent; parent != null; parent = parent.parent)
        {
            t.concatenate(parent.nodeToParentTransform());
        }

        return t;
    }

    /**
     * Gets the world to Node transform affine
     * @return AffineTransform
     */
    public AffineTransform worldToNodeTransform()
    {
        try
        {
            return this.nodeToWorldTransform().createInverse();
        } catch (Exception e)
        {
            System.out.println(e.getMessage());
        }

        return new AffineTransform();
    }

    public Point2D convertToNodeSpace(Point2D point)
    {
        return this.worldToNodeTransform().transform(point, point);
    }

    public Point2D convertToNodeSpace(Vertex2F point)
    {
        Point2D point2f = point.toPoint2D();

        return this.worldToNodeTransform().transform(point2f, point2f);
    }

    public Point2D convertToNodeSpace(Vertex3F point)
    {
        Point2D point3f = point.toPoint2D();

        return this.worldToNodeTransform().transform(point3f, point3f);
    }

    public Point2D convertToWorldSpace(Point2D point)
    {
        return this.nodeToWorldTransform().transform(point, point);
    }

    public Point2D convertToWorldSpace(Vertex2F point)
    {
        Point2D point2f = point.toPoint2D();
        
        return this.nodeToWorldTransform().transform(point2f, point2f);
    }

    public Point2D convertToWorldSpace(Vertex3F point)
    {
        Point2D point3f = point.toPoint2D();

        return this.nodeToWorldTransform().transform(point3f, point3f);
    }

    public Point2D convertToNodeSpaceAR(Point2D point)
    {
        point = this.convertToNodeSpace(point);
        return CCMath.pointSubtract(point, this.anchorPointInPixel.toPoint2D());
    }

    public Point2D convertToNodeSpaceAR(Vertex2F point)
    {
        Point2D point2f = this.convertToNodeSpace(point);
        return CCMath.pointSubtract(point2f, this.anchorPointInPixel.toPoint2D());
    }

    public Point2D convertToNodeSpaceAR(Vertex3F point)
    {
        Point2D point3f = this.convertToNodeSpace(point);

        return CCMath.pointSubtract(point3f, this.anchorPointInPixel.toPoint2D());
    }

    public Point2D convertToWorldSpaceAR(Point2D point)
    {
        point = CCMath.pointAdd(point, this.anchorPointInPixel.toPoint2D());
        return this.convertToWorldSpace(point);
    }

    public Point2D convertToWorldSpaceAR(Vertex2F point)
    {
        Point2D point2f = this.convertToNodeSpace(point);
        point2f = CCMath.pointAdd(point2f, this.anchorPointInPixel.toPoint2D());
        return this.convertToWorldSpace(point2f);
    }

    public Point2D convertToWorldSpaceAR(Vertex3F point)
    {
        Point2D point3f = this.convertToNodeSpace(point);
        point3f = CCMath.pointAdd(point3f, this.anchorPointInPixel.toPoint2D());
        return this.convertToWorldSpace(point3f);
    }

    public Point2D convertToWindowSpace(Point2D point)
    {
        point = this.convertToWorldSpace(point);
        return CCDirector.convertToUI(point);
    }

    /**
     * Equals Method override
     * @param Object obj
     * @return bool
     */
    public boolean equals ( Object obj )
    {
        if ( this == obj ) return true;

        if ((obj != null) && (getClass() == obj.getClass()))
        {
                CCNode node = (CCNode) obj;

                if(this.rotation != node.rotation || this.scaleX != node.scaleX
                   || this.scaleY != node.scaleY || !this.position.equals(node.position)
                   || this.visible != node.visible || !this.anchorPointInPixel.equals(node.anchorPointInPixel)
                   || !this.anchorPoint.equals(node.anchorPoint) || this.isRelativeAnchorePoint != node.isRelativeAnchorePoint
                   || !this.contentSize.equals(node.contentSize) || !this.transform.equals(node.transform)
                   || this.vertexZ != node.vertexZ || this.zOrder != node.zOrder || this.tag != node.tag
                   || !this.userData.equals(node.userData) || this.isRunning != node.isRunning
                   || this.isTransformDirty != node.isTransformDirty || this.isInverseDirty != node.isInverseDirty
                   || !this.offsetPosition.equals(node.offsetPosition))
                {
                    return false;
                }

                return true;
        }
        else
        {
                return false;
        }
    }
}
