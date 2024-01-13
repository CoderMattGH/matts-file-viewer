package com.fileviewer.dataprocessing;

import com.fileviewer.observer.ProgObserver;

public interface DataViewer {
    /**
     * Enum to represent the type of view for the data.
     */
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
     * Function to write characters to the GUI textarea.
     * Note: Start index is inclusive, and endIndex is exclusive.
     * @param data The raw bytes in an int[] array.
     * @param observer The ProgObserver to record progress.
     * @param type The type of view for the data.
     * @param startByteIndex The starting index of the data array to start reading from (inclusive).
     * @param endByteIndex The ending index of the data array to end reading (exclusive).
     * @return A String with the full data string represented in the requested view.
     */
    String fetchDisplayData(int[] data, ProgObserver observer, Enum<DataType> type, int startByteIndex,
            int endByteIndex);
}
