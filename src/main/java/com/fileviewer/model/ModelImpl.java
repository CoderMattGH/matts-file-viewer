package com.fileviewer.model;

import com.fileviewer.dataprocessing.DataViewer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import static com.fileviewer.dataprocessing.DataViewer.DataType;

public class ModelImpl implements Model {
    private static final Logger logger = LogManager.getLogger(ModelImpl.class);

    private static final int MAX_BYTES_PER_PAGE = 10000;

    private int[] lastFileLoadedData = null;
    private int startByteIndex = 0;
    private DataType currentType = DataType.Characters;

    public ModelImpl() {
        logger.debug("Constructing ModelImpl");
    }

    public void setLastFileLoadedData(int[] lastFileLoadedData) {
        this.lastFileLoadedData = lastFileLoadedData;
    }

    public int[] getLastFileLoadedData() {
        return lastFileLoadedData;
    }

    public void setStartByteIndex(int startByteIndex) {
        this.startByteIndex = startByteIndex;
    }

    public int getStartByteIndex() {
        return startByteIndex;
    }

    public int getCurrentPage() {
        if (startByteIndex == 0)
            return 1;
        else
            return (startByteIndex / MAX_BYTES_PER_PAGE) + 1;
    }

    public DataType getCurrentType() {
        return this.currentType;
    }

    public void setCurrentType(DataType currentType) {
        this.currentType = currentType;
    }

    public int getMaxBytesPerPage() {
        return MAX_BYTES_PER_PAGE;
    }
}
