package com.messcode.transferobjects.util;

import java.beans.PropertyChangeListener;

/**
 *
 */
public interface Subject {
    /**
     * @param eventName
     * @param listener
     */
    void addListener(String eventName, PropertyChangeListener listener);

    /**
     * @param eventName
     * @param listener
     */
    void removeListener(String eventName, PropertyChangeListener listener);
}
