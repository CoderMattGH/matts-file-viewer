package com.fileviewer;

import com.fileviewer.controller.Controller;
import com.fileviewer.controller.ControllerImpl;
import com.fileviewer.dataprocessing.DataViewer;
import com.fileviewer.dataprocessing.DataViewerImpl;
import com.fileviewer.dataprocessing.FileLoader;
import com.fileviewer.dataprocessing.FileLoaderImpl;
import com.fileviewer.gui.GUI;
import com.fileviewer.gui.progressbar.ProgressBarFactory;
import com.fileviewer.gui.progressbar.ProgressBarFactoryImpl;
import com.fileviewer.model.Model;
import com.fileviewer.model.ModelImpl;
import com.fileviewer.observer.ProgObserverFactory;
import com.fileviewer.observer.ProgObserverFactoryImpl;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.swing.*;

public class MainApp {
    private static final Logger logger = LogManager.getLogger(MainApp.class);

    public MainApp() {
        logger.debug("Constructing MainApp.");
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

        FileLoader fileLoader = new FileLoaderImpl();
        DataViewer dataViewer = new DataViewerImpl();
        ProgObserverFactory progObserverFactory = new ProgObserverFactoryImpl();
        ProgressBarFactory progressBarFactory = new ProgressBarFactoryImpl();
        Model model = new ModelImpl();

        Controller controller = new ControllerImpl(fileLoader, model, dataViewer,
                progObserverFactory, progressBarFactory);

        GUI gui = new GUI(controller, progObserverFactory);
        controller.setGUI(gui);
    }
}
