package com.fileviewer.gui.progressbar;

import com.fileviewer.observer.ProgObserver;

import javax.swing.*;

public interface ProgressBarFactory {
    ProgressBar getInstance(JFrame parent, ProgObserver observer);
}
