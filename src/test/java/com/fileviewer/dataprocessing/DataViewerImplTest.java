package com.fileviewer.dataprocessing;

import com.fileviewer.observer.ProgObserver;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Unit test for DataViewerImpl class.
 */
public class DataViewerImplTest {

    private final static Logger logger = LogManager.getLogger(DataViewerImplTest.class);

    private static int[] getMockDataSmall() {
        int[] data = {33, 35, 36, 34};

        return data;
    }

    @Test
    public void testDisplayData_ValidBytes() {
        ProgObserver observer = Mockito.mock(ProgObserver.class);

        DataViewer dataViewer = new DataViewerImpl();

        int[] data = getMockDataSmall();
        DataViewer.DataType type = DataViewer.DataType.Bytes;
        int startByteIndex = 0;
        int endByteIndex = 4;

        String result =
                dataViewer.fetchDisplayData(data, observer, type, startByteIndex, endByteIndex);

        assertEquals("33 35 36 34 ", result);
    }

    @Test
    public void testDisplayData_ValidChars() {
        ProgObserver observer = Mockito.mock(ProgObserver.class);

        DataViewer dataViewer = new DataViewerImpl();

        int[] data = {'a', 'b', 'f', 'e'};
        DataViewer.DataType type = DataViewer.DataType.Characters;
        int startByteIndex = 0;
        int endByteIndex = 4;

        String result =
                dataViewer.fetchDisplayData(data, observer, type, startByteIndex, endByteIndex);

        assertEquals("abfe", result);
    }

    @Test
    public void testDisplayData_ValidHex() {
        ProgObserver observer = Mockito.mock(ProgObserver.class);

        DataViewer dataViewer = new DataViewerImpl();

        int[] data = getMockDataSmall();
        DataViewer.DataType type = DataViewer.DataType.Hex;
        int startByteIndex = 0;
        int endByteIndex = 4;

        String result =
                dataViewer.fetchDisplayData(data, observer, type, startByteIndex, endByteIndex);

        assertEquals("21 23 24 22 ", result);
    }

    @Test
    public void testDisplayData_ValidUTF8Char() {
        ProgObserver observer = Mockito.mock(ProgObserver.class);

        DataViewer dataViewer = new DataViewerImpl();

        int[] data = getMockDataSmall();
        DataViewer.DataType type = DataViewer.DataType.UTF8Characters;
        int startByteIndex = 0;
        int endByteIndex = 4;

        String result =
                dataViewer.fetchDisplayData(data, observer, type, startByteIndex, endByteIndex);

        assertEquals("!#$\"", result);
    }

    @Test
    public void testDisplayData_ValidUTF8Code() {
        ProgObserver observer = Mockito.mock(ProgObserver.class);

        DataViewer dataViewer = new DataViewerImpl();

        int[] data = getMockDataSmall();
        DataViewer.DataType type = DataViewer.DataType.UTF8Bytes;
        int startByteIndex = 0;
        int endByteIndex = 4;

        String result =
                dataViewer.fetchDisplayData(data, observer, type, startByteIndex, endByteIndex);

        assertEquals("33 35 36 34 ", result);
    }

    @Test
    public void testDisplayData_ValidUTF16Char() {
        ProgObserver observer = Mockito.mock(ProgObserver.class);

        DataViewer dataViewer = new DataViewerImpl();

        int[] data = getMockDataSmall();
        DataViewer.DataType type = DataViewer.DataType.UTF16Characters;
        int startByteIndex = 0;
        int endByteIndex = 4;

        String result =
                dataViewer.fetchDisplayData(data, observer, type, startByteIndex, endByteIndex);

        assertEquals("℣␢", result);
    }

    @Test
    public void testDisplayData_ValidUTF16Code() {
        ProgObserver observer = Mockito.mock(ProgObserver.class);

        DataViewer dataViewer = new DataViewerImpl();

        int[] data = getMockDataSmall();
        DataViewer.DataType type = DataViewer.DataType.UTF16Bytes;
        int startByteIndex = 0;
        int endByteIndex = 4;

        String result =
                dataViewer.fetchDisplayData(data, observer, type, startByteIndex, endByteIndex);

        assertEquals("8483 9250 ", result);
    }
}
