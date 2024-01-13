package com.fileviewer.observer;

/**
 * The ProgObserver class is used for communication between the Progress Bar and any long-running
 * tasks in order to display task progress to the user.
 */
public interface ProgObserver {
    double getPercentage();

    void setPercentage(double percentage);

    boolean isFinished();

    void setIsFinished(boolean finished);

    boolean isCancelled();

    void setCancelled(boolean cancelled);
}
