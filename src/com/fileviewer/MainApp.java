package com.fileviewer;

import com.fileviewer.dataprocessing.DataViewer;

import javax.swing.*;

public class MainApp {
    public MainApp() {
        System.out.println("Constructing Main App");
    }

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            System.err.println("Error setting look and feel.");
            e.printStackTrace();
        }

        MainApp mainApp = new MainApp();
        mainApp.runApp();
    }

    public void runApp() {
        System.out.println("Running FileViewer v0.1");

        GUI gui = new GUI(new FileLoader(), new DataViewer());

        // WebScraperGUI scraper = new WebScraperGUI();
    }
}
