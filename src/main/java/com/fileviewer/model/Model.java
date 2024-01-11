package com.fileviewer.model;

import static com.fileviewer.dataprocessing.DataViewer.DataType;

public interface Model {
    void setLastFileLoadedData(int[] lastFileLoadedData);
    int[] getLastFileLoadedData();

    void setStartByteIndex(int startByteIndex);
    int getStartByteIndex();

    void setCurrentType(DataType currentType);
    DataType getCurrentType();

    int getCurrentPage();

    int getMaxBytesPerPage();
}
