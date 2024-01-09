package com.fileviewer;

public class FileProgObserver {
    private double percentage;
    private boolean finished;
    private boolean cancelled;

    public FileProgObserver() {
        System.out.println("Constructing File Progress Observer.");

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
