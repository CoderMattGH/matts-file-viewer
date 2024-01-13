package com.fileviewer.gui.progressbar;

import com.fileviewer.observer.ProgObserver;

import javax.swing.*;

public interface ProgressBarFactory {
    /**
     * Returns a new instance of a ProgressBar class.
     * @param parent The parent JFrame of the ProgressBar.
     * @param observer A ProgObserver object used to record progress.
     * @return A ProgressBar object.
     */
    ProgressBar getInstance(JFrame parent, ProgObserver observer);
}
