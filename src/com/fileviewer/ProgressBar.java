package com.fileviewer;

import javax.swing.*;
import java.awt.*;

public class ProgressBar extends JDialog {
    private final JProgressBar progressBar;
    private final FileProgObserver observer;

    public ProgressBar(JFrame parent, FileProgObserver observer) {
        super(parent);

        this.observer = observer;

        System.out.println("Constructing ProgressBar...");

        this.setSize(200, 100);
        this.setResizable(false);
        this.setAlwaysOnTop(true);
        this.setTitle("Processing...");
        this.setLayout(new BorderLayout());

        progressBar = new JProgressBar(0, 100);
        progressBar.setValue(0);
        progressBar.setStringPainted(true);

        this.add(progressBar, BorderLayout.CENTER);

        JButton cancelBtn = new JButton("Cancel");
        cancelBtn.addActionListener(e -> {
                cancelTask();
            });
        this.add(cancelBtn, BorderLayout.SOUTH);

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

    private void cancelTask() {
        System.out.println("Trying to cancel...");
        this.observer.setCancelled(true);
    }
}
