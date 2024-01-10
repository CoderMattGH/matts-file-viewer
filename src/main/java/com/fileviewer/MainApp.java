package com.fileviewer;

import com.fileviewer.dataprocessing.DataViewer;
import com.fileviewer.dataprocessing.FileLoader;
import com.fileviewer.gui.GUI;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.swing.*;

public class MainApp {
    private static final Logger logger = LogManager.getLogger(MainApp.class);

    public MainApp() {
        logger.info("Constructing MainApp.");
    }

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            logger.error("Unable to set Look and Feel.");
            e.printStackTrace();
        }

        MainApp mainApp = new MainApp();
        mainApp.runApp();
    }

    public void runApp() {
        logger.info("Running File Viewer.");

        FileLoader fileLoader = new FileLoader();
        DataViewer dataViewer = new DataViewer();

        GUI gui = new GUI(fileLoader, dataViewer);
    }
}
