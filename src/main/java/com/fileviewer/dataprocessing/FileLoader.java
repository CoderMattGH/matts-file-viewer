package com.fileviewer.dataprocessing;

import com.fileviewer.observer.ProgObserver;

import java.io.File;

public interface FileLoader {
    /**
     * Reads the file provided, and returns the bytes in an integer array.
     *
     * NOTE: Returns null if an error occurred.
     *
     * Maximum filesize is Integer.MAX_INTEGER bytes.
     */
    int[] loadFile(File file, ProgObserver observer);
}
