package com.fileviewer.dataprocessing;

import com.fileviewer.gui.GUI;
import com.fileviewer.observer.ProgObserver;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;

/**
 * Test imperative method displayData() from the DataViewerImpl class.
 *
 * We do not care about the implementation details, just that GUI.appendTextOutput() was called
 * with the correct output.
 */
public class DataViewerImplTest {

    private final static Logger logger = LogManager.getLogger(DataViewerImplTest.class);

    private static int[] getMockDataSmall() {
        int[] data = {33, 35, 36, 34};

        return data;
    }

    /*
    @Test
    public void testDisplayData_ValidBytes() {
        ProgObserver observer = Mockito.mock(ProgObserver.class);
        GUI gui = Mockito.mock(GUI.class);

        DataViewer dataViewer = new DataViewerImpl();
        dataViewer.setGUI(gui);

        int[] data = getMockDataSmall();
        DataViewer.DataType type = DataViewer.DataType.Bytes;
        int startByteIndex = 0;
        int endByteIndex = 4;

        dataViewer.displayData(data, observer, type, startByteIndex, endByteIndex);

        Mockito.verify(gui, times(1)).appendTextOutput("33 35 36 34 ");
        Mockito.verify(gui, times(1)).appendTextOutput(any());
    }

    @Test
    public void testDisplayData_ValidChars() {
        ProgObserver observer = Mockito.mock(ProgObserver.class);
        GUI gui = Mockito.mock(GUI.class);

        DataViewer dataViewer = new DataViewerImpl();
        dataViewer.setGUI(gui);

        int[] data = {'a', 'b', 'f', 'e'};
        DataViewer.DataType type = DataViewer.DataType.Characters;
        int startByteIndex = 0;
        int endByteIndex = 4;

        dataViewer.displayData(data, observer, type, startByteIndex, endByteIndex);

        Mockito.verify(gui, times(1)).appendTextOutput("abfe");
        Mockito.verify(gui, times(1)).appendTextOutput(any());
    }

    @Test
    public void testDisplayData_ValidHex() {
        ProgObserver observer = Mockito.mock(ProgObserver.class);
        GUI gui = Mockito.mock(GUI.class);

        DataViewer dataViewer = new DataViewerImpl();
        dataViewer.setGUI(gui);

        int[] data = getMockDataSmall();
        DataViewer.DataType type = DataViewer.DataType.Hex;
        int startByteIndex = 0;
        int endByteIndex = 4;

        dataViewer.displayData(data, observer, type, startByteIndex, endByteIndex);

        Mockito.verify(gui, times(1)).appendTextOutput("21 23 24 22 ");
        Mockito.verify(gui, times(1)).appendTextOutput(any());
    }

    @Test
    public void testDisplayData_ValidUTF8Char() {
        ProgObserver observer = Mockito.mock(ProgObserver.class);
        GUI gui = Mockito.mock(GUI.class);

        DataViewer dataViewer = new DataViewerImpl();
        dataViewer.setGUI(gui);

        int[] data = getMockDataSmall();
        DataViewer.DataType type = DataViewer.DataType.UTF8Characters;
        int startByteIndex = 0;
        int endByteIndex = 4;

        dataViewer.displayData(data, observer, type, startByteIndex, endByteIndex);

        Mockito.verify(gui, times(1)).appendTextOutput("!#$\"");
        Mockito.verify(gui, times(1)).appendTextOutput(any());
    }

    @Test
    public void testDisplayData_ValidUTF8Code() {
        ProgObserver observer = Mockito.mock(ProgObserver.class);
        GUI gui = Mockito.mock(GUI.class);

        DataViewer dataViewer = new DataViewerImpl();
        dataViewer.setGUI(gui);

        int[] data = getMockDataSmall();
        DataViewer.DataType type = DataViewer.DataType.UTF8Bytes;
        int startByteIndex = 0;
        int endByteIndex = 4;

        dataViewer.displayData(data, observer, type, startByteIndex, endByteIndex);

        Mockito.verify(gui, times(1)).appendTextOutput("33 35 36 34 ");
        Mockito.verify(gui, times(1)).appendTextOutput(any());
    }

    @Test
    public void testDisplayData_ValidUTF16Char() {
        ProgObserver observer = Mockito.mock(ProgObserver.class);
        GUI gui = Mockito.mock(GUI.class);

        DataViewer dataViewer = new DataViewerImpl();
        dataViewer.setGUI(gui);

        int[] data = getMockDataSmall();
        DataViewer.DataType type = DataViewer.DataType.UTF16Characters;
        int startByteIndex = 0;
        int endByteIndex = 4;

        dataViewer.displayData(data, observer, type, startByteIndex, endByteIndex);

        Mockito.verify(gui, times(1)).appendTextOutput("℣␢");
        Mockito.verify(gui, times(1)).appendTextOutput(any());
    }

    @Test
    public void testDisplayData_ValidUTF16Code() {
        ProgObserver observer = Mockito.mock(ProgObserver.class);
        GUI gui = Mockito.mock(GUI.class);

        DataViewer dataViewer = new DataViewerImpl();
        dataViewer.setGUI(gui);

        int[] data = getMockDataSmall();
        DataViewer.DataType type = DataViewer.DataType.UTF16Bytes;
        int startByteIndex = 0;
        int endByteIndex = 4;

        dataViewer.displayData(data, observer, type, startByteIndex, endByteIndex);

        Mockito.verify(gui, times(1)).appendTextOutput("8483 9250 ");
        Mockito.verify(gui, times(1)).appendTextOutput(any());
    }
    */
}
