package com.fileviewer.model;

import static com.fileviewer.dataprocessing.DataViewer.DataType;

/**
 * The class where the main application data will be stored.
 */
public interface Model {
    /**
     * Sets the data for the main application.
     * This should be an int array containing the raw bytes of the file loaded.
     * @param lastFileLoadedData An int array containing the raw bytes of the file loaded.
     */
    void setLastFileLoadedData(int[] lastFileLoadedData);

    /**
     * Returns the data from the last loaded file.
     * @return The raw byte data in an int array.
     */
    int[] getLastFileLoadedData();

    /**
     * Sets the index to start reading the data from.
     * @param startByteIndex An int specifying the byte number to stat reading the data.
     */
    void setStartByteIndex(int startByteIndex);

    /**
     * Returns the index to start reading the data from.
     * @return An int representing the byte number to start reading the data.
     */
    int getStartByteIndex();

    /**
     * Sets the current DataType to display.
     * @param currentType A DataType representing the view of the data.
     */
    void setCurrentType(DataType currentType);

    /**
     * @return The current DataType to display.
     */
    DataType getCurrentType();

    /**
     * @return An int representing the current page of the data.
     */
    int getCurrentPage();

    /**
     * @return The maximum bytes of data to display per page.
     */
    int getMaxBytesPerPage();
}
