package com.fileviewer.controller;

import com.fileviewer.dataprocessing.DataViewer.DataType;
import com.fileviewer.dto.ChangeViewDTO;
import com.fileviewer.dto.LoadFileDTO;
import com.fileviewer.dto.PageChangeDTO;
import com.fileviewer.observer.ProgObserver;

import java.io.File;

public interface Controller {
    /**
     * Loads the specified file and returns a LoadFileDTO object containing various data
     * or an error message.
     *
     * NOTE: The file size in bytes must be below Integer.MAX_BYTES or an error flag will be set
     * in the response.
     *
     * @param observer The ProgObserver used to record progress.
     * @param file A File object to read.
     * @return A LoadFileDTO object containing the data.
     */
    LoadFileDTO loadFile(ProgObserver observer, File file);

    /**
     * Request a change of data view to a different type.  For example, a change from
     * DataType.Characters to DataType.Hex.  Returns a ChangeViewDTO object containing various data
     * or an error message.
     *
     * @param type The DataType to change to.
     * @param progObserver The ProgObserver used to record progress.
     * @return A ChangeViewDTO object containing the data.
     */
    ChangeViewDTO changeViewType(DataType type, ProgObserver progObserver);

    /**
     * Requests the data for the next page.  If no more data is found, then an error flag will be
     * set in the response.
     *
     * @param progObserver The ProgObserver used to record progress.
     * @return A PageChangeDTO object containing the data.
     */
    PageChangeDTO showNextPage(ProgObserver progObserver);

    /**
     * Requests the data for the previous page.  Errors will be recorded in the response object.
     *
     * @param progObserver The ProgObserver used to record progress.
     * @return A PageChangeDTO object containing the data.
     */
    PageChangeDTO showPrevPage(ProgObserver progObserver);

    /**
     * Requests the data for the first page.  Errors will be recorded in the response object.
     *
     * @param progObserver The ProgObserver used to record progress.
     * @return A PageChangeDTO object containing the data.
     */
    PageChangeDTO showFirstPage(ProgObserver progObserver);
}
