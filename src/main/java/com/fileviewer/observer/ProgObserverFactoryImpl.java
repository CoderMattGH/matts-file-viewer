package com.fileviewer.observer;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ProgObserverFactoryImpl implements ProgObserverFactory {
    private static final Logger logger = LogManager.getLogger(ProgObserverFactoryImpl.class);

    public ProgObserverFactoryImpl() {
        logger.debug("Constructing ProgObserverFactoryImpl.");
    }

    public ProgObserver getInstance() {
        return new ProgObserverImpl();
    }
}
