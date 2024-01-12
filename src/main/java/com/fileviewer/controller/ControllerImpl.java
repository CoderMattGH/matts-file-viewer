package com.fileviewer.controller;

import com.fileviewer.dataprocessing.DataViewer;
import com.fileviewer.dataprocessing.FileLoader;
import com.fileviewer.dto.LoadFileDTO;import com.fileviewer.dto.PageChangeDTO;
import com.fileviewer.gui.GUI;
import com.fileviewer.gui.progressbar.ProgressBar;
import com.fileviewer.gui.progressbar.ProgressBarFactory;
import com.fileviewer.model.Model;
import com.fileviewer.observer.ProgObserver;
import com.fileviewer.observer.ProgObserverFactory;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.swing.*;
import java.io.File;
import java.util.Observer;

import static com.fileviewer.dataprocessing.DataViewer.DataType;

public class ControllerImpl implements Controller {
    private static final Logger logger = LogManager.getLogger(ControllerImpl.class);

    private final ProgObserverFactory progObserverFactory;
    private final ProgressBarFactory progressBarFactory;
    private final FileLoader fileLoader;
    private final Model model;
    private final DataViewer dataViewer;

    private GUI gui = null;

    public ControllerImpl(FileLoader fileLoader, Model model, DataViewer dataViewer,
            ProgObserverFactory progObserverFactory, ProgressBarFactory progressBarFactory) {
        logger.debug("Constructing ControllerImpl");

        this.fileLoader = fileLoader;
        this.model = model;
        this.dataViewer = dataViewer;
        this.progObserverFactory = progObserverFactory;
        this.progressBarFactory = progressBarFactory;
    }

    public synchronized void setGUI(GUI gui) {
        this.gui = gui;
    }

    public synchronized LoadFileDTO loadFile(final JFileChooser fileChooser, ProgObserver observer) {
        int returnVal = fileChooser.showOpenDialog(gui);

        if (returnVal == JFileChooser.APPROVE_OPTION) {
            showProgressBar(observer);

            File file = fileChooser.getSelectedFile();

            int[] tempFileData = fileLoader.loadFile(file, observer);

            observer.setPercentage(0);

            // If an error occurred or file was null then return.
            if (tempFileData == null) {
                gui.displayError("File size was too large.");

                return null;
            }

            model.setLastFileLoadedData(tempFileData);
            model.setStartByteIndex(0);

            String dataString = dataViewer.fetchDisplayData(model.getLastFileLoadedData(), observer,
                    model.getCurrentType(), model.getStartByteIndex(),
                    model.getStartByteIndex() + model.getMaxBytesPerPage());

            if (dataString == null) {
                return null;
            }

            LoadFileDTO dto = new LoadFileDTO();
            dto.setCurrentPage(model.getCurrentPage());
            dto.setFileSize(model.getLastFileLoadedData().length);
            dto.setFilename(file.getName());
            dto.setData(dataString);

            return dto;
        }

        return null;
    }

    /**
     * Can return null if there was no data.
     */
    public synchronized String displayData(DataType type, ProgObserver observer) {
        showProgressBar(observer);

        String string = dataViewer.fetchDisplayData(model.getLastFileLoadedData(), observer,
                model.getCurrentType(), model.getStartByteIndex(),
                model.getStartByteIndex() + model.getMaxBytesPerPage());

        return string;
    }

    public void showProgressBar(ProgObserver observer) {
        Thread thread = new Thread(() -> {
                ProgressBar progressBar = progressBarFactory.getInstance(gui, observer);

                while(!observer.isFinished()) {
                    progressBar.setPercentage(observer.getPercentage());

                    try {
                        Thread.sleep(20);
                    } catch (Exception ignored) {}
                }

                logger.debug("Trying to destroy ProgressBar...");
                progressBar.destroyProgressBar();
        });
        thread.start();
    }

    public synchronized String changeViewType(DataType type, ProgObserver observer) {
        model.setStartByteIndex(0);
        gui.setPageLabel(model.getCurrentPage());

        model.setCurrentType(type);
        String data = displayData(model.getCurrentType(), observer);

        return data;
    }

    public synchronized PageChangeDTO showNextPage(ProgObserver observer) {
        logger.debug("Fetching next page.");

        if (model.getLastFileLoadedData() == null)
            return null;

        int tempStartIndex = model.getStartByteIndex() + model.getMaxBytesPerPage();
        if (tempStartIndex >= model.getLastFileLoadedData().length) {
            gui.displayMessage("No more data.");

            return null;
        }

        model.setStartByteIndex(tempStartIndex);
        gui.setPageLabel(model.getCurrentPage());

        String data = displayData(model.getCurrentType(), observer);

        PageChangeDTO pageChangeDTO = new PageChangeDTO();
        pageChangeDTO.setData(data);
        pageChangeDTO.setCurrentPage(model.getCurrentPage());

        return pageChangeDTO;
    }

    public synchronized PageChangeDTO showPrevPage(ProgObserver observer) {
        logger.debug("Fetching previous page.");

        int result = model.getStartByteIndex() - model.getMaxBytesPerPage();

        if (result < 0)
            model.setStartByteIndex(0);
        else
            model.setStartByteIndex(result);

        String data = displayData(model.getCurrentType(), observer);

        PageChangeDTO pageChangeDTO = new PageChangeDTO();
        pageChangeDTO.setData(data);
        pageChangeDTO.setCurrentPage(model.getCurrentPage());

        return pageChangeDTO;
    }

    public synchronized PageChangeDTO showFirstPage(ProgObserver observer) {
        logger.debug("Fetching first page.");

        model.setStartByteIndex(0);

        String data = displayData(model.getCurrentType(), observer);

        PageChangeDTO pageChangeDTO = new PageChangeDTO();
        pageChangeDTO.setData(data);
        pageChangeDTO.setCurrentPage(model.getCurrentPage());

        return pageChangeDTO;
    }
}
