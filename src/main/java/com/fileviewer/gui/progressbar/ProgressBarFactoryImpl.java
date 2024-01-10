package com.fileviewer.gui.progressbar;

import com.fileviewer.observer.ProgObserver;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.swing.*;

public class ProgressBarFactoryImpl implements ProgressBarFactory {
    private final static Logger logger = LogManager.getLogger(ProgressBarFactoryImpl.class);

    public ProgressBarFactoryImpl() {
        logger.debug("Constructing ProgressBarFactoryImpl.");
    }

    public ProgressBar getInstance(JFrame parent, ProgObserver observer) {
        return new ProgressBarImpl(parent, observer);
    }
}
