package com.fileviewer.controller;

import com.fileviewer.dataprocessing.DataViewer;
import com.fileviewer.dataprocessing.FileLoader;
import com.fileviewer.dto.ChangeViewDTO;
import com.fileviewer.dto.LoadFileDTO;
import com.fileviewer.dto.PageChangeDTO;
import com.fileviewer.model.Model;
import com.fileviewer.model.ModelImpl;
import com.fileviewer.observer.ProgObserver;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.*;

/**
 * Unit test for ControllerImpl class.
 */
public class ControllerImplTest {
    private static final Logger logger = LogManager.getLogger(ControllerImplTest.class);

    public ControllerImplTest() {
        logger.debug("Constructing ControllerImplTest");
    }

    private static String getResourcesDir() {
        return "." + File.separator + "src" + File.separator + "test" + File.separator
                + "resources" + File.separator;
    }

    private static File getTestFile1() {
        String path = getResourcesDir() + "testfile1.txt";

        return new File(path);
    }

    private static File getTestFile2() {
        String path = getResourcesDir() + "testfile2.txt";

        return new File(path);
    }

    private static int[] getTestFileData(File testFile) throws IOException {
        if (testFile.length() >= Integer.MAX_VALUE)
            throw new IOException("Test file is too large for test.");

        int[] testFileData = new int[(int)testFile.length()];

        BufferedInputStream is = new BufferedInputStream(new FileInputStream(testFile));
        int readByte;
        int count = 0;
        while ((readByte = is.read()) != -1) {
            testFileData[count] = readByte;
            count++;
        }

        is.close();

        return testFileData;
    }

    private FileLoader fileLoader;
    private Model model;
    private DataViewer dataViewer;
    private ProgObserver observer;

    @BeforeEach
    public void initMocks() {
        fileLoader = Mockito.mock(FileLoader.class);
        model = Mockito.mock(Model.class);
        dataViewer = Mockito.mock(DataViewer.class);
        observer = Mockito.mock(ProgObserver.class);
    }

    @Test
    public void testLoadFile_ValidFile() throws IOException {
        File testFile = getTestFile1();
        int[] testFileData = getTestFileData(testFile);

        Mockito.when(fileLoader.loadFile(testFile, observer)).thenReturn(testFileData);

        Mockito.when(model.getMaxBytesPerPage()).thenReturn(1000);
        Mockito.when(model.getCurrentType()).thenReturn(DataViewer.DataType.Characters);
        Mockito.when(model.getLastFileLoadedData()).thenReturn(testFileData);
        Mockito.when(model.getCurrentPage()).thenReturn(1);

        String testString = "TEST STRING";
        Mockito.when(dataViewer.fetchDisplayData(any(), any(), any(), anyInt(), anyInt())).
                thenReturn(testString);

        Controller controller = new ControllerImpl(fileLoader, model, dataViewer);

        LoadFileDTO dto = controller.loadFile(observer, testFile);

        assertEquals(false, dto.isErrorOccurred());
        assertEquals(testFile.getName(), dto.getFilename());
        assertEquals(testFile.length(), dto.getFileSize());
        assertEquals(testString, dto.getData());
    }

    @Test
    public void testLoadFile_InValidFileTooBig() {
        File testFile = Mockito.mock(File.class);
        Mockito.when(testFile.length()).thenReturn((long)Integer.MAX_VALUE + 1);

        Controller controller = new ControllerImpl(fileLoader, model, dataViewer);

        LoadFileDTO dto = controller.loadFile(observer, testFile);

        assertEquals(true, dto.isErrorOccurred());
        assertEquals("File size was too large.", dto.getErrorMessage());
    }

    @Test
    public void testChangeViewType_Valid() {
        Model model = new ModelImpl();

        String testString = "TEST STRING";
        Mockito.when(dataViewer.fetchDisplayData(any(), any(), any(), anyInt(), anyInt()))
                .thenReturn(testString);

        DataViewer.DataType testChangeType = DataViewer.DataType.Hex;

        Controller controller = new ControllerImpl(fileLoader, model, dataViewer);
        ChangeViewDTO dto = controller.changeViewType(testChangeType, observer);

        assertEquals(false, dto.isErrorOccurred());
        assertEquals(testString, dto.getData());
        assertEquals(testChangeType, model.getCurrentType());
    }

    @Test
    public void testChangeViewType_FetchDataError() {
        Model model = new ModelImpl();
        model.setCurrentType(DataViewer.DataType.Characters);

        Mockito.when(dataViewer.fetchDisplayData(any(), any(), any(), anyInt(), anyInt()))
                .thenReturn(null);

        DataViewer.DataType testChangeType = DataViewer.DataType.UTF8Characters;

        Controller controller = new ControllerImpl(fileLoader, model, dataViewer);
        ChangeViewDTO dto = controller.changeViewType(testChangeType, observer);

        assertEquals(true, dto.isErrorOccurred());
        assertEquals(DataViewer.DataType.Characters, model.getCurrentType());
    }

    @Test
    public void testShowNextPage_SmallFileOnePage() throws IOException {
        File testFile = getTestFile1();
        int[] testData = getTestFileData(testFile);

        Model model = new ModelImpl();
        model.setLastFileLoadedData(testData);
        model.setCurrentType(DataViewer.DataType.Characters);
        model.setStartByteIndex(0);
        model.setLastFileLoadedData(testData);

        Controller controller = new ControllerImpl(fileLoader, model, dataViewer);
        PageChangeDTO dto = controller.showNextPage(observer);

        assertEquals(true, dto.isErrorOccurred());
        assertEquals(1, model.getCurrentPage());
    }

    @Test
    public void testShowNextPage_BigFileMultiplePages() throws IOException {
        File testFileLarge = getTestFile2();
        int[] testData = getTestFileData(testFileLarge);

        Model model = new ModelImpl();
        model.setLastFileLoadedData(testData);
        model.setCurrentType(DataViewer.DataType.Characters);
        model.setStartByteIndex(0);

        String testString = "TEST STRING";
        Mockito.when(dataViewer.fetchDisplayData(any(), any(), any(), anyInt(), anyInt()))
                .thenReturn(testString);

        Controller controller = new ControllerImpl(fileLoader, model, dataViewer);
        PageChangeDTO dto = controller.showNextPage(observer);

        assertEquals(false, dto.isErrorOccurred());
        assertEquals(2, dto.getCurrentPage());
    }

    @Test
    public void testShowPrevPage_InvalidSmallFile() throws IOException {
        File testFile = getTestFile1();
        int[] testData = getTestFileData(testFile);

        Model model = new ModelImpl();
        model.setLastFileLoadedData(testData);
        model.setCurrentType(DataViewer.DataType.Hex);
        model.setStartByteIndex(0);

        String testString = "TEST STRING";
        Mockito.when(dataViewer.fetchDisplayData(any(), any(), any(), anyInt(), anyInt()))
                .thenReturn(testString);

        Controller controller = new ControllerImpl(fileLoader, model, dataViewer);
        PageChangeDTO dto = controller.showPrevPage(observer);

        assertEquals(1, model.getCurrentPage());
    }

    @Test
    public void testShowPrevPage_ValidLargeFile() throws IOException {
        File largeTestFile = getTestFile2();
        int[] testData = getTestFileData(largeTestFile);

        Model model = new ModelImpl();
        model.setLastFileLoadedData(testData);
        model.setCurrentType(DataViewer.DataType.Characters);
        // Start on second page.
        model.setStartByteIndex(model.getMaxBytesPerPage() + 1);

        String testString = "TEST STRING";
        Mockito.when(dataViewer.fetchDisplayData(any(), any(), any(), anyInt(), anyInt()))
                .thenReturn(testString);

        Controller controller = new ControllerImpl(fileLoader, model, dataViewer);

        assertEquals(2, model.getCurrentPage());

        PageChangeDTO dto = controller.showPrevPage(observer);

        assertEquals(1, dto.getCurrentPage());
    }

    @Test
    public void testShowFirstPage_InvalidSmallFile() throws IOException {
        File testFile = getTestFile1();
        int[] testData = getTestFileData(testFile);

        Model model = new ModelImpl();
        model.setLastFileLoadedData(testData);
        model.setCurrentType(DataViewer.DataType.UTF16Bytes);
        model.setStartByteIndex(0);

        String testString = "TEST STRING";
        Mockito.when(dataViewer.fetchDisplayData(any(), any(), any(), anyInt(), anyInt()))
                .thenReturn(testString);

        Controller controller = new ControllerImpl(fileLoader, model, dataViewer);

        assertEquals(1, model.getCurrentPage());

        PageChangeDTO dto = controller.showPrevPage(observer);

        assertEquals(1, dto.getCurrentPage());
    }

    @Test
    public void testShowFirstPage_ValidLargeFile() throws IOException {
        File testFile = getTestFile2();
        int[] testData = getTestFileData(testFile);

        Model model = new ModelImpl();
        model.setLastFileLoadedData(testData);
        model.setCurrentType(DataViewer.DataType.Bytes);
        // Start on page 2.
        model.setStartByteIndex(model.getMaxBytesPerPage() + 1);

        String testString = "TEST STRING";
        Mockito.when(dataViewer.fetchDisplayData(any(), any(), any(), anyInt(), anyInt()))
                .thenReturn(testString);

        assertEquals(2, model.getCurrentPage());

        Controller controller = new ControllerImpl(fileLoader, model, dataViewer);
        PageChangeDTO dto = controller.showFirstPage(observer);

        assertEquals(false, dto.isErrorOccurred());
        assertEquals(1, dto.getCurrentPage());
    }
}
