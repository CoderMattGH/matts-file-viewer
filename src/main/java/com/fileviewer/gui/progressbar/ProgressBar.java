package com.fileviewer.gui.progressbar;

import javax.swing.*;

public interface ProgressBar {
    void setPercentage(double percentage);

    void setValue(int value);

    void setTitle(String title);

    void destroyProgressBar();
}
