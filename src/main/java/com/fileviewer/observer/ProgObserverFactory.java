package com.fileviewer.observer;

/**
 * A factory class to construct ProgObserver objects.
 */
public interface ProgObserverFactory {
    /**
     * Returns a new instance of a ProgObserver object.
     */
    ProgObserver getInstance();
}
