package com.fileviewer.controller;

import com.fileviewer.dataprocessing.DataViewer;
import com.fileviewer.dataprocessing.FileLoader;
import com.fileviewer.gui.GUI;
import com.fileviewer.gui.progressbar.ProgressBar;
import com.fileviewer.gui.progressbar.ProgressBarFactory;
import com.fileviewer.model.Model;
import com.fileviewer.observer.ProgObserver;
import com.fileviewer.observer.ProgObserverFactory;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;

import javax.swing.*;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;

public class ControllerImplTest {
    private static final Logger logger = LogManager.getLogger(ControllerImplTest.class);

    public ControllerImplTest() {
        logger.debug("Constructing ControllerImplTest");
    }

    private File getTestFile1() {
        String path = "." + File.separator + "src" + File.separator + "test" + File.separator
                + "resources" + File.separator + "testfile1.txt";

        return new File(path);
    }

    /*
    private FileLoader fileLoader;
    private Model model;
    private DataViewer dataViewer;
    private ProgObserverFactory progObserverFactory;
    private ProgressBarFactory progressBarFactory;
    private GUI gui;

    @BeforeEach
    public void initMocks() {
        fileLoader = Mockito.mock(FileLoader.class);
        model = Mockito.mock(Model.class);
        dataViewer = Mockito.mock(DataViewer.class);
        progObserverFactory = Mockito.mock(ProgObserverFactory.class);
        progressBarFactory = Mockito.mock(ProgressBarFactory.class);
        gui = Mockito.mock(GUI.class);
    }

    @Test
    public void loadFileTest_ValidFile() throws IOException {
        File testFile = getTestFile1();

        BufferedInputStream fis = new BufferedInputStream(new FileInputStream(testFile));

        int[] testData = new int[(int)testFile.length()];

        int byteRead;
        int count = 0;
        while((byteRead = fis.read()) != -1) {
            testData[count] = byteRead;
            count++;
        }

        JFileChooser fileChooser = Mockito.mock(JFileChooser.class);
        Mockito.when(fileChooser.showOpenDialog(any())).thenReturn(JFileChooser.APPROVE_OPTION);
        Mockito.when(fileChooser.getSelectedFile()).thenReturn(testFile);

        ProgObserver observer = Mockito.mock(ProgObserver.class);
        Mockito.when(progObserverFactory.getInstance()).thenReturn(observer);

        ProgressBar progressBar = Mockito.mock(ProgressBar.class);
        Mockito.when(progressBarFactory.getInstance(any(), any())).thenReturn(progressBar);

        Mockito.when(fileLoader.loadFile(any(), any())).thenReturn(testData);

        Mockito.when(model.getLastFileLoadedData()).thenReturn(testData);
        Mockito.when(model.getStartByteIndex()).thenReturn(0);
        Mockito.when(model.getMaxBytesPerPage()).thenReturn(1000);

        Mockito.when(dataViewer.fetchDisplayData(any(), any(ProgObserver.class),
                any(DataViewer.DataType.class), anyInt(), anyInt())).thenReturn(
                "This is a test string.");

        Controller controller = new ControllerImpl(fileLoader, model, dataViewer, progObserverFactory,
                progressBarFactory);
        controller.setGUI(gui);

        controller.loadFile(fileChooser);

        Mockito.verify(gui, Mockito.times(1)).appendTextOutput("This is a test string.");
        // Mockito.verify(gui, Mockito.times(1)).appendTextOutput(any());
    }
    */
}
