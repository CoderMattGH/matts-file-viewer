package com.fileviewer;

import javax.swing.*;
import java.awt.*;

public class ProgressBar extends JFrame {
    private final JProgressBar progressBar;

    public ProgressBar() {
        System.out.println("Constructing ProgressBar...");

        this.setSize(200, 100);
        this.setResizable(false);
        this.setAlwaysOnTop(true);

        this.setLayout(new BorderLayout());

        progressBar = new JProgressBar(0, 100);
        progressBar.setValue(0);
        progressBar.setStringPainted(true);

        this.add(progressBar, BorderLayout.CENTER);

        this.setVisible(true);
    }

    public void setPercentage(double percentage) {
        int value = (int)percentage;

        progressBar.setValue(value);
    }

    public void setValue(int value) {
        progressBar.setValue(value);
    }

    public void destroyProgressBar() {
        this.setVisible(false);
        this.dispose();
    }
}
