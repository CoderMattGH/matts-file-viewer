package com.fileviewer.controller;

import com.fileviewer.dataprocessing.DataViewer;
import com.fileviewer.dataprocessing.FileLoader;
import com.fileviewer.model.Model;
import com.fileviewer.observer.ProgObserver;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.Mockito;

import java.io.File;

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

    private FileLoader fileLoader;
    private Model model;
    private DataViewer dataViewer;

    @BeforeEach
    public void initMocks() {
        fileLoader = Mockito.mock(FileLoader.class);
        model = Mockito.mock(Model.class);
        dataViewer = Mockito.mock(DataViewer.class);
    }


    public void testLoadFile() {
        ProgObserver observer = Mockito.mock(ProgObserver.class);
        File testFile = getTestFile1();

        Controller controller = new ControllerImpl(fileLoader, model, dataViewer);

    }
}
