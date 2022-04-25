package com.messcode.transferobjects;

import java.io.Serializable;

/**
 *
 */
public class Container implements Serializable {

    private Object object;
    private ClassName className;

    /**
     * @param object
     * @param className
     */
    public Container(Object object, ClassName className) {
        this.object = object;
        this.className = className;
    }

    /**
     * Returns the stored object.
     *
     * @return Object object
     */
    public Object getObject() {
        return this.object;
    }

    /**
     * Sets the object.
     *
     * @param object Object
     */
    public void setObject(Object object) {
        this.object = object;
    }

    /**
     * Returns the class name of the object
     *
     * @return String className
     */
    public ClassName getClassName() {
        return className;
    }

    /**
     * Sets the class name for the object.
     *
     * @param className String
     */
    public void setClassName(ClassName className) {
        this.className = className;
    }
}
