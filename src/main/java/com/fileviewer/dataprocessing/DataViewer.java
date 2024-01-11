package com.fileviewer.dataprocessing;

import com.fileviewer.controller.Controller;
import com.fileviewer.gui.GUI;
import com.fileviewer.observer.ProgObserver;

public interface DataViewer {
    public static enum DataType {
        Bytes,
        Hex,
        Characters,
        UTF8Bytes,
        UTF8Characters,
        UTF16Bytes,
        UTF16Characters,
    }

    /**
     *  Function to write characters to the GUI textarea.
     *
     *  Note: Start index is inclusive, and endIndex is exclusive.
     */
    String fetchDisplayData(int[] data, ProgObserver observer, Enum<DataType> type, int startByteIndex,
            int endByteIndex);
}
