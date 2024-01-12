package com.fileviewer.gui.progressbar;

import com.fileviewer.observer.ProgObserver;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.swing.*;
import java.awt.*;

public class ProgressBarImpl extends JDialog implements ProgressBar {
    private final static Logger logger = LogManager.getLogger(ProgressBarImpl.class);

    private final JProgressBar progressBar;
    private final ProgObserver observer;

    protected ProgressBarImpl(JFrame parent, ProgObserver observer) {
        super(parent);

        logger.debug("Constructing ProgressBarImpl");

        this.observer = observer;

        this.setSize(200, 100);
        this.setResizable(false);
        this.setAlwaysOnTop(true);

        this.setLocationRelativeTo(parent);
        this.setLocation(this.getX(), this.getY() - 80);

        this.setTitle("Processing...");
        this.setLayout(new BorderLayout());

        this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);

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

    @Override
    public void setTitle(String title) {
        super.setTitle(title);
    }

    private void cancelTask() {
        logger.info("Trying to cancel task...");

        this.observer.setCancelled(true);
    }
}
