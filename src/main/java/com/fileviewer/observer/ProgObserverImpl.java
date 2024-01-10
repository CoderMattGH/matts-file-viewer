package com.fileviewer.observer;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ProgObserverImpl implements ProgObserver {
    private final Logger logger = LogManager.getLogger(ProgObserverImpl.class);

    private double percentage;
    private boolean finished;
    private boolean cancelled;

    protected ProgObserverImpl() {
        logger.info("Constructing ProgObserverImpl.");

        percentage = 0;
        finished = false;
        cancelled = false;
    }

    public double getPercentage() {
        return percentage;
    }

    public void setPercentage(double percentage) {
        this.percentage = percentage;
    }

    public boolean isFinished() {
        return finished;
    }

    public void setIsFinished(boolean finished) {
        this.finished = finished;
    }

    public boolean isCancelled() {
        return cancelled;
    }

    public void setCancelled(boolean cancelled) {
        this.cancelled = cancelled;
    }
}
