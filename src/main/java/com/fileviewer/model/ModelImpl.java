package com.fileviewer.model;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import static com.fileviewer.dataprocessing.DataViewer.DataType;

public class ModelImpl implements Model {
    private static final Logger logger = LogManager.getLogger(ModelImpl.class);

    private static final int MAX_BYTES_PER_PAGE = 10000;

    private int[] lastFileLoadedData = null;
    private int startByteIndex = 0;
    private DataType currentType = DataType.Characters;
    private String data = null;

    public ModelImpl() {
        logger.debug("Constructing ModelImpl");
    }

    public synchronized void setLastFileLoadedData(int[] lastFileLoadedData) {
        this.lastFileLoadedData = lastFileLoadedData;
    }

    public synchronized int[] getLastFileLoadedData() {
        return lastFileLoadedData;
    }

    public synchronized void setStartByteIndex(int startByteIndex) {
        this.startByteIndex = startByteIndex;
    }

    public synchronized int getStartByteIndex() {
        return startByteIndex;
    }

    public synchronized int getCurrentPage() {
        if (startByteIndex == 0)
            return 1;
        else
            return (startByteIndex / MAX_BYTES_PER_PAGE) + 1;
    }

    public synchronized DataType getCurrentType() {
        return this.currentType;
    }

    public synchronized void setCurrentType(DataType currentType) {
        this.currentType = currentType;
    }

    public synchronized int getMaxBytesPerPage() {
        return MAX_BYTES_PER_PAGE;
    }

    public synchronized String getData() {
        return data;
    }

    public synchronized void setData(String data) {
        this.data = data;
    }
}
