package com.fileviewer.controller;

import com.fileviewer.dataprocessing.DataViewer.DataType;
import com.fileviewer.dto.ChangeViewDTO;import com.fileviewer.dto.LoadFileDTO;import com.fileviewer.dto.PageChangeDTO;
import com.fileviewer.gui.GUI;
import com.fileviewer.model.Model;
import com.fileviewer.observer.ProgObserver;

import javax.swing.*;import java.io.File;

public interface Controller {
    LoadFileDTO loadFile(ProgObserver observer, File file);
    ChangeViewDTO changeViewType(DataType type, ProgObserver progObserver);
    PageChangeDTO showNextPage(ProgObserver progObserver);
    PageChangeDTO showPrevPage(ProgObserver progObserver);
    PageChangeDTO showFirstPage(ProgObserver progObserver);
}
