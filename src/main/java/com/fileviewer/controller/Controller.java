package com.fileviewer.controller;

import com.fileviewer.dataprocessing.DataViewer.DataType;
import com.fileviewer.gui.GUI;
import com.fileviewer.observer.ProgObserver;

public interface Controller {
    void setGUI(GUI gui);
    void loadFile();
    void displayData(DataType type);
    void showProgressBar(ProgObserver observer);
    void changeViewType(DataType type);
    void showNextPage();
    void showPrevPage();
    void showFirstPage();
    void resetTextOutput();
    void appendTextOutput(String string);
}
