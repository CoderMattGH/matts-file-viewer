package com.fileviewer;

public class MainApp {
    public MainApp() {
        System.out.println("Constructing Main App");
    }

    public static void main(String[] args) {
        MainApp mainApp = new MainApp();
        mainApp.runApp();
    }

    public void runApp() {
        System.out.println("Running Fuck About 3");

        GUI gui = new GUI(new FileLoader());

        // WebScraperGUI scraper = new WebScraperGUI();
    }
}
