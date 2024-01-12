package com.fileviewer.controller;

import com.fileviewer.dataprocessing.DataViewer.DataType;
import com.fileviewer.dto.LoadFileDTO;import com.fileviewer.dto.PageChangeDTO;
import com.fileviewer.gui.GUI;
import com.fileviewer.model.Model;
import com.fileviewer.observer.ProgObserver;

import javax.swing.*;

public interface Controller {
    void setGUI(GUI gui);
    LoadFileDTO loadFile(JFileChooser fileChooser, ProgObserver observer);
    String displayData(DataType type, ProgObserver progObserver);
    void showProgressBar(ProgObserver observer);
    String changeViewType(DataType type, ProgObserver progObserver);
    PageChangeDTO showNextPage(ProgObserver progObserver);
    PageChangeDTO showPrevPage(ProgObserver progObserver);
    PageChangeDTO showFirstPage(ProgObserver progObserver);
}
