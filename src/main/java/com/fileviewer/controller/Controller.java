package com.fileviewer.controller;

import com.fileviewer.dataprocessing.DataViewer.DataType;
import com.fileviewer.gui.GUI;
import com.fileviewer.observer.ProgObserver;

import javax.swing.*;

public interface Controller {
    void setGUI(GUI gui);
    String loadFile(JFileChooser fileChooser, ProgObserver observer);
    String displayData(DataType type, ProgObserver progObserver);
    void showProgressBar(ProgObserver observer);
    String changeViewType(DataType type, ProgObserver progObserver);
    void showNextPage(ProgObserver progObserver);
    void showPrevPage(ProgObserver progObserver);
    void showFirstPage(ProgObserver progObserver);
    void resetTextOutput();
    void appendTextOutput(String string);
}
