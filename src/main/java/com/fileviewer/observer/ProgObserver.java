package com.fileviewer.observer;

public interface ProgObserver {
    double getPercentage();

    void setPercentage(double percentage);

    boolean isFinished();

    void setIsFinished(boolean finished);

    boolean isCancelled();

    void setCancelled(boolean cancelled);
}
