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

import ion2d.support.*;
import ion2d.support.INTypes.*;
import java.awt.*;
import java.awt.geom.*;
import java.util.Vector;
import java.util.Hashtable;
import java.util.Enumeration;
import java.util.Comparator;
import java.util.Collections;
import org.lwjgl.opengl.*;

public class INNode implements Comparator<INNode> {
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

    protected INCamera camera;
    protected INGridBase grid;

    protected Vector<INNode> children;
    protected INNode parent;

    protected int tag;

    protected Object userData;

    protected Hashtable<INSelector, INTimer> scheduleSelectors;
    protected boolean isRunning;

    protected boolean isTransformDirty;
    protected boolean isInverseDirty;

    protected Vertex2F offsetPosition;

    /**
     * Default Constructor
     */
    public INNode()
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

        this.transform = new AffineTransform();
        this.inverse = new AffineTransform();

        this.vertexZ = 0;
        this.visible = true;
        this.tag = -1;
        this.zOrder = 0;

        this.children = new Vector<INNode>();
        this.scheduleSelectors = new Hashtable<INSelector, INTimer>();

        this.userData = new Object();
    }

    /**
     * Copy Constructor
     * @param INNode original
     */
    public INNode(INNode original)
    {
        if(original == null) throw new NullPointerException();
        
        this.rotation = original.rotation;
        this.anchorPoint = original.anchorPoint.clone();
        this.anchorPointInPixel = original.anchorPointInPixel.clone();
        this.camera = original.camera.clone();

        this.children = (Vector<INNode>)original.children.clone();
        this.contentSize = (Dimension)original.contentSize.clone();

        if(original.grid != null) this.grid = original.grid.clone();

        this.scaleX = original.scaleX;
        this.scaleY = original.scaleY;
        this.vertexZ = original.vertexZ;
        this.visible = original.visible;
        this.zOrder = original.zOrder;

        this.scheduleSelectors = (Hashtable<INSelector, INTimer>)original.scheduleSelectors.clone();

        this.transform = (AffineTransform)original.transform.clone();
        this.inverse = (AffineTransform)original.inverse.clone();

        this.offsetPosition = original.offsetPosition.clone();
        this.position = original.position.clone();

        this.userData = original.userData;
    }

    /**
     * Disposes the node and removes all children from the node
     * The parent is set to null
     * All actions are stopped on the node
     */
    public void dispose()
    {
        this.stopAllActions();
        this.scheduleSelectors.clear();
        this.scheduleSelectors = null;
        this.parent = null;
        this.removeAllChildrenWithCleanup(true);
    }

    /**
     * Finalize override
     */
    public void finalize()
    {
        this.dispose();
    }

    /**
     * Add a child to the node. Returns itself to allow for chaining
     * @param INNode node
     * @return INNode
     */
    public INNode addChild(INNode node) throws Exception
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
     * @param INNode child
     * @return int
     */
    public int getIndexForChild(INNode child)
    {
        return this.children.indexOf(child);
    }

    /**
     * Adds a child to the node and sets the z order of the child
     * @param INNode node
     * @param int z
     * @return INNode
     */
    public INNode addChild(INNode node, int z) throws Exception
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
     * @param INNode node
     * @param int z
     * @param int tag
     * @return INNode
     */
    public INNode addChild(INNode node, int z, int tag) throws Exception
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
     * @return INNode
     */
    public INNode getChildByTag(int tag)
    {
        INNode found = null;
        
        for(INNode child : this.children)
        {
            if(child.tag == tag)
            {
                return child;
            }
        }
        
        return found;
    }

    /**
     * Gets the child at the specified index
     * @param int index
     * @return INNode
     */
    public INNode getChild(int index)
    {
        return this.children.get(index);
    }

    /**
     * Gets the total number of children that the node has
     * @return int
     */
    public int getTotalChildren()
    {
        return this.children.size();
    }

    /**
     * Removes the specified child node
     * @param INNode node
     * @param boolean cleanup
     */
    public void removeChild(INNode node, boolean cleanup)
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
        INNode node = this.getChildByTag(tag);

        if(node != null)
        {
            this.detachChild(node, cleanup);
        }
    }

    /**
     * Removes all child nodes
     * @param boolean cleanup
     */
    public void removeAllChildrenWithCleanup(boolean cleanup)
    {
        for(INNode node : this.children)
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
     * @param INNode child
     * @param boolean cleanup
     */
    private void detachChild(INNode child, boolean cleanup)
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
     * This must be overrided in subclasses
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

        for(INNode node : this.children)
        {
            if(node.zOrder < 0)
                node.visit();
        }

        this.draw();

        for(INNode node : this.children)
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
       GL11.glMultMatrix(INMath.AffineToGL(this.nodeToParentTransform()));

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
       else
       {
           if(this.isRelativeAnchorePoint && (this.anchorPointInPixel.x != 0 || this.anchorPointInPixel.y != 0))
           {
               GL11.glTranslatef(-this.anchorPointInPixel.x, -this.anchorPointInPixel.y, 0);
           }
           
           if(this.anchorPointInPixel.x != 0 || this.anchorPointInPixel.y != 0)
           {
               GL11.glTranslatef(this.position.x + this.anchorPointInPixel.x, this.position.y + this.anchorPointInPixel.y, this.vertexZ);
           }
           else if(this.position.x != 0 || this.position.y != 0 || this.vertexZ != 0)
           {
               GL11.glTranslatef(this.position.x, this.position.y, this.vertexZ);
           }

           if(this.rotation != 0.0f)
           {
               GL11.glRotatef(-this.rotation, 0.0f, 0.0f, 1.0f);
           }

           if(this.scaleX != 1.0f || this.scaleY != 1.0f)
           {
               GL11.glScalef(this.scaleX, this.scaleY, 1.0f);
           }

           if(this.camera != null && !(this.grid != null && this.grid.getActive()))
           {
               this.camera.locate();
           }

           if(this.anchorPointInPixel.x != 0.0f || this.anchorPointInPixel.y != 0.0f)
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
        for(INNode node : this.children)
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
        for(INNode node : this.children)
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

         for(INNode node : this.children)
         {
             node.onExit();
         }
     }

     /**
      * schedule a timer to run
      * @param INSelector selector
      */
     public void schedule(INSelector selector)
     {
         this.schedule(selector, 0);
     }

     /**
      * Schedule a timer to active at the specified interval
      * @param INSelector selector
      * @param float interval
      */
     public void schedule(INSelector selector, float interval)
     {
        if(selector != null)
        {
            if(this.scheduleSelectors.containsKey(selector))
                return;

            INTimer timer = new INTimer(selector, interval);

            if(this.isRunning)
                INScheduler.scheduleTimer(timer);

            this.scheduleSelectors.put(selector, timer);
        }
     }

     /**
      * Unschedules a timer based on selector
      * @param selector
      */
     public void unschedule(INSelector selector)
     {
         if(selector == null) return;

         INTimer timer = null;

         if(this.scheduleSelectors.contains(selector))
         {
             timer = this.scheduleSelectors.get(selector);
             this.scheduleSelectors.remove(selector);
         }

         if(this.isRunning && timer != null)
         {
             INScheduler.unscheduleTimer(timer);
         }
     }

     /**
      * Activates the available timers
      */
     private void activateTimers()
     {
         for(Enumeration e = this.scheduleSelectors.keys(); e.hasMoreElements();)
         {
             INScheduler.scheduleTimer(this.scheduleSelectors.get((INSelector)e.nextElement()));
         }

         INActionManager.resumeAllActions(this);
     }

     /**
      * Deactivates the timers on this node
      */
     private void deactivateTimers()
     {
         for(Enumeration e = this.scheduleSelectors.keys(); e.hasMoreElements();)
         {
             INScheduler.unscheduleTimer(this.scheduleSelectors.get((INSelector)e.nextElement()));
         }         
         
         INActionManager.pauseAllActions(this);
     }

    /**
     * Compares 2 nodes z order to see which is the smallest or largest
     * Of the 2
     * @param INNode node1
     * @param INNode node2
     * @return int
     */
    public int compare(INNode node1, INNode node2)
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

    /**
     * Gets the camera associated with the INNode
     * @return INCamera
     */
    public INCamera getCamera()
    {
        if(this.camera == null)
            this.camera = new INCamera();

        return this.camera;
    }

    /**
     * Gets the z order of the node
     * @return int
     */
    public int getZOrder() { return this.zOrder; }

    /**
     * Gets the vertex z of the node
     * @return float
     */
    public float getVertexZ() { return this.vertexZ; }

    /**
     * Gets the current rotation of the node
     * @return float
     */
    public float getRotation() { return this.rotation; }

    /**
     * Gets the current x scale of the node
     * @return float
     */
    public float getScaleX() { return this.scaleX; }

    /**
     * Gets the current y scale of the node
     * @return float
     */
    public float getScaleY() { return this.scaleY; }

    /**
     * Gets the current position of the node
     * @return Vertex2F
     */
    public Vertex2F getPosition() { return this.position; }

    /**
     * Gets the current visible status of the node
     * @return boolean
     */
    public boolean isVisible() { return this.visible; }

    /**
     * Gets the current running status of the node
     * @return boolean
     */
    public boolean isRunning() { return this.isRunning; }

    /**
     * Gets whether the node is relative to anchor point
     * @return boolean
     */
    public boolean isRelativeAnchorPoint() { return this.isRelativeAnchorePoint; }

    /**
     * Gets the tag of the node
     * @return int
     */
    public int getTag() { return this.tag; }

    /**
     * Gets the current user data associated with the node
     * @return Object
     */
    public Object getData() { return this.userData; }

    /**
     * Gets the anchor point position for the node
     * @return Vertex2F
     */
    public Vertex2F getAnchorPoint() { return this.anchorPoint.clone(); }

    /**
     * Gets the anchor point position for the node in pixels
     * @return Vertex2F
     */
    public Vertex2F getAnchorePointInPixel() { return this.anchorPointInPixel.clone(); }

    /**
     * Gets the current size of the node
     * @return Dimension
     */
    public Dimension getContentSize() { return (Dimension)this.contentSize.clone(); }

    /**
     * Gets the parent of the node
     * @return INNode
     */
    public INNode getParent() { return this.parent; }

    /**
     * Sets the scale of both x and y of the node
     * @param float amount
     */
    public void setScale(float amount) { this.scaleX = this.scaleY = amount; this.isTransformDirty = this.isInverseDirty = true; }
    
    /**
     * Sets the scale for the x only
     * @param float amount
     */
    public void setScaleX(float amount) { this.scaleX = amount; this.isTransformDirty = this.isInverseDirty = true; }
    
    /**
     * Sets the scale for the y only
     * @param float amount
     */
    public void setScaleY(float amount) { this.scaleY = amount; this.isTransformDirty = this.isInverseDirty = true; }

    /**
     * Sets the nodes z order
     * Everytime this is updated all other child nodes of the parent node is
     * sorted to update the z order position of the nodes
     * @param int z
     */
    public void setZOrder(int z)
    {
        this.zOrder = z;

        if(this.parent != null)
        {
            Collections.sort(this.parent.children, this.parent);
        }

        //Check to see if the parent is a sprite sheet and if so
        //Reorder the sprite sheet Atlas
        if(this.parent.getClass() == INSpriteSheet.class)
        {
            INSpriteSheet sheet = (INSpriteSheet)this.parent;
            sheet.reorderAtlas();
        }
    }

    /**
     * Sets the vertex z of the node
     * @param float z
     */
    public void setVertexZ(float z) { this.vertexZ = z; }

    /**
     * Sets the rotation of the node
     * @param float rotation
     */
    public void setRotation(float rotation) { this.rotation = rotation; this.isTransformDirty = this.isInverseDirty = true; }

    /**
     * Sets the position of the node
     * @param Vertex2F point
     */
    public void setPosition(Vertex2F point) { this.position = point; this.isTransformDirty = this.isInverseDirty = true; }

    /**
     * Sets the visibility of the node
     * @param boolean visible
     */
    public void setVisible(boolean visible) { this.visible = visible; }

    /**
     * Set whether the node is relative to the anchor point
     * @param boolean relative
     */
    public void setIsRelativeAnchorePoint(boolean relative)
    {
        this.isRelativeAnchorePoint = relative;
        this.isTransformDirty = this.isInverseDirty = true;
    }

    /**
     * Set the CCGrid to use for the node
     * @param INGridBase grid
     */
    public void setGrid(INGridBase grid)
    {
        this.grid = grid;
    }

    /**
     * Set the custom user data for the node
     * @param Object data
     */
    public void setData(Object data) { this.userData = data; }

    /**
     * Sets the parent of the node
     * @param INNode parent
     */
    public void setParent(INNode parent) { this.parent = parent; }

    /**
     * Sets the anchor point of the node
     * @param Vertex2F point
     */
    public void setAnchorPoint(Vertex2F point)
    {
        if(!this.anchorPoint.equals(point))
        {
            this.anchorPoint = point;
            this.anchorPointInPixel = new Vertex2F(this.contentSize.width * this.anchorPoint.x, this.contentSize.height * this.anchorPoint.y);
            this.isTransformDirty = this.isInverseDirty = true;
        }
    }

    /**
     * Sets the content size of the node
     * Do not use unless you know what you are doing!
     * @param Dimension size
     */
    public void setContentSize(Dimension size)
    {
        if(!this.contentSize.equals(size))
        {
            this.contentSize = size;
            this.anchorPointInPixel = new Vertex2F(size.width * this.anchorPoint.x, size.height * this.anchorPoint.y);
            this.isTransformDirty = this.isInverseDirty = true;
        }
    }

    /**
     * Sets the tag of the node
     * @param int tag
     */
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
            this.transform.rotate(-INMath.degreesToRadians(this.rotation));
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
            e.printStackTrace();
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

        for(INNode nodeParent = this.parent; nodeParent != null; nodeParent = nodeParent.parent)
        {
            t.concatenate(nodeParent.nodeToParentTransform());
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
            e.printStackTrace();
        }

        return new AffineTransform();
    }

    /**
     * Converts the specified point to node space
     * @param Point2D point
     * @return Point2D
     */
    public Point2D convertToNodeSpace(Point2D point)
    {
        return this.worldToNodeTransform().transform(point, point);
    }

    /**
     * Converts the specified point to node space
     * @param Vertex2F point
     * @return Point2D
     */
    public Point2D convertToNodeSpace(Vertex2F point)
    {
        Point2D point2f = point.toPoint2D();

        return this.worldToNodeTransform().transform(point2f, point2f);
    }

    /**
     * Converts the specified point to node space
     * @param Vertex3F point
     * @return Point2D
     */
    public Point2D convertToNodeSpace(Vertex3F point)
    {
        Point2D point3f = point.toPoint2D();

        return this.worldToNodeTransform().transform(point3f, point3f);
    }

    /**
     * Converts the point to world space
     * @param Point2D point
     * @return Point2D
     */
    public Point2D convertToWorldSpace(Point2D point)
    {
        return this.nodeToWorldTransform().transform(point, point);
    }

    /**
     * Converts the point to world space
     * @param Vertex2F point
     * @return Point2D
     */
    public Point2D convertToWorldSpace(Vertex2F point)
    {
        Point2D point2f = point.toPoint2D();
        
        return this.nodeToWorldTransform().transform(point2f, point2f);
    }

    /**
     * Converts the point to world space
     * @param Vertex3F point
     * @return Point2D
     */
    public Point2D convertToWorldSpace(Vertex3F point)
    {
        Point2D point3f = point.toPoint2D();

        return this.nodeToWorldTransform().transform(point3f, point3f);
    }

    /**
     * Converts the point to node space based on anchor position
     * @param Point2D point
     * @return Point2D
     */
    public Point2D convertToNodeSpaceAR(Point2D point)
    {
        point = this.convertToNodeSpace(point);
        return INMath.pointSubtract(point, this.anchorPointInPixel.toPoint2D());
    }

    /**
     * Converts the point to node space based on anchor position
     * @param Vertex2F point
     * @return Point2D
     */
    public Point2D convertToNodeSpaceAR(Vertex2F point)
    {
        Point2D point2f = this.convertToNodeSpace(point);
        return INMath.pointSubtract(point2f, this.anchorPointInPixel.toPoint2D());
    }

    /**
     * Converts the point to node space based on anchor position
     * @param Vertex3F point
     * @return Point2D
     */
    public Point2D convertToNodeSpaceAR(Vertex3F point)
    {
        Point2D point3f = this.convertToNodeSpace(point);

        return INMath.pointSubtract(point3f, this.anchorPointInPixel.toPoint2D());
    }

    /**
     * Converts the point to world space based on anchor position
     * @param Point2D point
     * @return Point2D
     */
    public Point2D convertToWorldSpaceAR(Point2D point)
    {
        point = INMath.pointAdd(point, this.anchorPointInPixel.toPoint2D());
        return this.convertToWorldSpace(point);
    }

    /**
     * Converts the point to world space based on anchor position
     * @param Vertex2F point
     * @return Point2D
     */
    public Point2D convertToWorldSpaceAR(Vertex2F point)
    {
        Point2D point2f = this.convertToNodeSpace(point);
        point2f = INMath.pointAdd(point2f, this.anchorPointInPixel.toPoint2D());
        return this.convertToWorldSpace(point2f);
    }

    /**
     * Converts the point to world space based on anchor position
     * @param Vertex3F point
     * @return Point2D
     */
    public Point2D convertToWorldSpaceAR(Vertex3F point)
    {
        Point2D point3f = this.convertToNodeSpace(point);
        point3f = INMath.pointAdd(point3f, this.anchorPointInPixel.toPoint2D());
        return this.convertToWorldSpace(point3f);
    }

    /**
     * Converts the point to window space
     * It is the same as convertToWorldSpace
     * The Desktop applications only need to worry about world space
     * Because, the world space is the window space
     * @param Point2D point
     * @return Point2D
     */
    public Point2D convertToWindowSpace(Point2D point)
    {
        point = this.convertToWorldSpace(point);
        return point;
    }

    /**
     * Runs the specified action on the node
     * @param INAction action
     * @return INAction
     */
    public INAction runAction(INAction action) throws Exception
    {
        if(action == null) throw new NullPointerException();

        INActionManager.addAction(action, this, false);

        return action;
    }

    /**
     * Stops all actions associated with the node
     */
    public void stopAllActions()
    {
        INActionManager.removeAllActions(this);
    }

    /**
     * Stops the specified action on the node
     * @param INAction action
     */
    public void stopAction(INAction action)
    {
        INActionManager.removeAction(action, this);
    }

    /**
     * Stops the specified action by tag
     * @param int tag
     */
    public void stopActionByTag(int tag)
    {
        INActionManager.removeAction(tag, this);
    }

    /**
     * Gets the action by the tag for the node
     * @param int tag
     * @return INAction
     */
    public INAction getActionByTag(int tag)
    {
        return INActionManager.getAction(tag, this);
    }

    /**
     * Gets the total number of actions running on the node
     * @return int
     */
    public int getTotalRunningActions()
    {
        return INActionManager.totalRunningActions(this);
    }

    /**
     * Determines if the point is within the node
     * @param int x
     * @param int y
     * @return boolean
     */
    public final boolean isInNode(int x, int y)
    {
        Point2D nodeSpace = this.convertToNodeSpace(new Vertex2F(x,y));

        if(nodeSpace.getX() >= 0 && nodeSpace.getX() <= this.contentSize.width && nodeSpace.getY() >= 0 &&
                nodeSpace.getY() <= this.contentSize.height)
        {
            return true;
        }
        else
        {
            return false;
        }
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
                INNode node = (INNode) obj;

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
