package com.fileviewer.gui.progressbar;

/**
 * A ProgressBar class that will be displayed on top of the parent window.
 * Constructing the object will automatically display the progress bar.
 */
public interface ProgressBar {
    void setPercentage(double percentage);

    void setValue(int value);

    void setTitle(String title);

    /**
     * Calls dispose on the Progress Bar and hides it.
     */
    void destroyProgressBar();
}
