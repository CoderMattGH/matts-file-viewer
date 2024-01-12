package com.fileviewer.controller;

import com.fileviewer.dataprocessing.DataViewer;
import com.fileviewer.dataprocessing.FileLoader;
import com.fileviewer.dto.ChangeViewDTO;
import com.fileviewer.dto.LoadFileDTO;
import com.fileviewer.dto.PageChangeDTO;
import com.fileviewer.exception.FetchDataException;
import com.fileviewer.model.Model;
import com.fileviewer.observer.ProgObserver;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;

import static com.fileviewer.dataprocessing.DataViewer.DataType;

public class ControllerImpl implements Controller {
    private static final Logger logger = LogManager.getLogger(ControllerImpl.class);

    private final FileLoader fileLoader;
    private final Model model;
    private final DataViewer dataViewer;

    public ControllerImpl(FileLoader fileLoader, Model model, DataViewer dataViewer) {
        logger.debug("Constructing ControllerImpl");

        this.fileLoader = fileLoader;
        this.model = model;
        this.dataViewer = dataViewer;
    }

    public synchronized LoadFileDTO loadFile(ProgObserver observer, File file) {
        int[] tempFileData = fileLoader.loadFile(file, observer);

        observer.setPercentage(0);

        // If an error occurred or file was null then return.
        if (tempFileData == null) {
            LoadFileDTO dto = new LoadFileDTO();
            dto.setErrorOccurred(true);
            dto.setErrorMessage("File size was too large.");

            return dto;
        }

        String dataString;
        try {
            dataString = fetchData(tempFileData, model.getCurrentType(), observer, 0,
                    model.getMaxBytesPerPage());
        } catch (Exception e) {
            LoadFileDTO dto = new LoadFileDTO();
            dto.setErrorOccurred(true);
            dto.setErrorMessage("Error fetching data string.");

            return dto;
        }

        model.setLastFileLoadedData(tempFileData);
        model.setStartByteIndex(0);

        LoadFileDTO dto = new LoadFileDTO();
        dto.setCurrentPage(model.getCurrentPage());
        dto.setFileSize(model.getLastFileLoadedData().length);
        dto.setFilename(file.getName());
        dto.setData(dataString);

        return dto;
    }

    private synchronized String fetchData(DataType type, ProgObserver observer,
            int startByteIndex, int endByteIndex) throws FetchDataException {
        String dataString = fetchData(model.getLastFileLoadedData(), type, observer,
                startByteIndex, endByteIndex);

        return dataString;
    }

    private synchronized String fetchData(int[] data, DataType type, ProgObserver observer,
            int startByteIndex, int endByteIndex) throws FetchDataException {
        String dataString = dataViewer.fetchDisplayData(data, observer, type, startByteIndex,
                endByteIndex);

        if (dataString == null) {
            throw new FetchDataException();
        }

        return dataString;
    }

    public synchronized ChangeViewDTO changeViewType(DataType type, ProgObserver observer) {
        String data;

        try {
            data = fetchData(type, observer, 0, model.getMaxBytesPerPage());
        } catch (Exception e) {
            logger.error("Error trying to fetch data string.");

            ChangeViewDTO dto = new ChangeViewDTO();
            dto.setErrorOccurred(true);
            dto.setErrorMessage("Unable to fetch data.");

            return dto;
        }

        model.setCurrentType(type);
        model.setStartByteIndex(0);

        ChangeViewDTO dto = new ChangeViewDTO();
        dto.setCurrentPage(model.getCurrentPage());
        dto.setData(data);

        return dto;
    }

    public synchronized PageChangeDTO showNextPage(ProgObserver observer) {
        logger.debug("Fetching next page.");

        if (model.getLastFileLoadedData() == null)
            return null;

        int tempStartIndex = model.getStartByteIndex() + model.getMaxBytesPerPage();
        if (tempStartIndex >= model.getLastFileLoadedData().length) {
            PageChangeDTO dto = new PageChangeDTO();
            dto.setErrorOccurred(true);
            dto.setErrorMessage("No more data.");

            return dto;
        }

        String data;
        try {
            data = fetchData(model.getCurrentType(), observer, tempStartIndex,
                    tempStartIndex + model.getMaxBytesPerPage());
        } catch (Exception e) {
            logger.error("Error trying to fetch data string.");

            PageChangeDTO dto = new PageChangeDTO();
            dto.setErrorOccurred(true);
            dto.setErrorMessage("Unable to fetch data.");

            return dto;
        }

        model.setStartByteIndex(tempStartIndex);

        PageChangeDTO pageChangeDTO = new PageChangeDTO();
        pageChangeDTO.setData(data);
        pageChangeDTO.setCurrentPage(model.getCurrentPage());

        return pageChangeDTO;
    }

    public synchronized PageChangeDTO showPrevPage(ProgObserver observer) {
        logger.debug("Fetching previous page.");

        int startByteIndex = model.getStartByteIndex() - model.getMaxBytesPerPage();

        if (startByteIndex < 0)
            startByteIndex = 0;

        String data;
        try {
            data = fetchData(model.getCurrentType(), observer, startByteIndex,
                    startByteIndex + model.getMaxBytesPerPage());
        } catch (Exception e) {
            logger.error("Unable to fetch data string.");

            PageChangeDTO dto = new PageChangeDTO();
            dto.setErrorOccurred(true);
            dto.setErrorMessage("Unable to fetch data.");

            return dto;
        }

        model.setStartByteIndex(startByteIndex);

        PageChangeDTO pageChangeDTO = new PageChangeDTO();
        pageChangeDTO.setData(data);
        pageChangeDTO.setCurrentPage(model.getCurrentPage());

        return pageChangeDTO;
    }

    public synchronized PageChangeDTO showFirstPage(ProgObserver observer) {
        logger.debug("Fetching first page.");

        String data;
        try {
            data = fetchData(model.getCurrentType(), observer, 0,
                    model.getMaxBytesPerPage());
        } catch (Exception e) {
            logger.error("Unable to fetch data string.");

            PageChangeDTO dto = new PageChangeDTO();
            dto.setErrorOccurred(true);
            dto.setErrorMessage("Unable to fetch data.");

            return dto;
        }

        model.setStartByteIndex(0);

        PageChangeDTO pageChangeDTO = new PageChangeDTO();
        pageChangeDTO.setData(data);
        pageChangeDTO.setCurrentPage(model.getCurrentPage());

        return pageChangeDTO;
    }
}
